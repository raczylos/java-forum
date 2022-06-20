package com.example.forum.registration;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.security.JwtProvider;
import com.example.forum.user.User;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
import net.bytebuddy.pool.TypePool;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserService userService;
    private EmailValidator emailValidator;
    private EmailSender emailSender;
    private JwtProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;




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

    public LoginResponse login(LoginRequest request){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    }


    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }
}
