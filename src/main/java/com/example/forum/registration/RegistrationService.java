package com.example.forum.registration;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.security.JwtProvider;
import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
import net.bytebuddy.pool.TypePool;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.eclipse.persistence.sessions.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserService userService;
    private UserRepository userRepository;
    private EmailValidator emailValidator;
    private EmailSender emailSender;
    private JwtProvider tokenProvider;
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;




    public String register(RegistrationRequest request) {
        boolean validatedEmail =  emailValidator.test(request.getEmail());
        String successMessage = "User registered successfully";
        if(!request.getPassword().equals(request.getPasswordRepeat())){
            throw new IllegalStateException();
        }
        if(!validatedEmail){
            throw new IllegalStateException(String.format("%s is not valid", request.getEmail()));
        }
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                UserRole.valueOf(request.getRole()));  // to provide UserRole type (.getRole() is String))
        userService.signUpUser(user);
//        String text = String.format(Objects.requireNonNull(template.getText()));
        emailSender.send(request.getEmail(), templateSimpleMessage().getText());
        return successMessage;
//        return userService.signUpUser(
//                new User(
//                        request.getUsername(),
//                        request.getPassword(),
//                        request.getEmail(),
//                        UserRole.valueOf(request.getRole())  // to provide UserRole type (.getRole() is String)
//                )
//        );

    }

    public String login(LoginRequest request){
        Authentication authentication = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(authentication.isAuthenticated()){
            JSONObject jsonObject = new JSONObject();
            User user = userRepository.findByUsername(request.getUsername())
                    .or(() -> userRepository.findByEmail(request.getUsername()))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

            try{
                String username = user.getUsername();

                jsonObject.put("message", "success");
                jsonObject.put("username", user.getUsername());
                jsonObject.put("authorities", authentication.getAuthorities());
                jsonObject.put("token", tokenProvider.createToken(username, user.getUserRole()));
                return jsonObject.toString();
            }
            catch(JSONException e){
                try {
                    jsonObject.put("exception", e.getMessage());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return jsonObject.toString();
            }
        }

        return null;
    }


    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }
}
