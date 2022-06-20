package com.example.forum.registration;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.security.JwtProvider;
import com.example.forum.user.User;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserService userService;
    private EmailValidator emailValidator;
    private EmailSender emailSender;
    private JwtProvider tokenProvider;




    public JSONObject register(RegistrationRequest request) {
        boolean validatedEmail =  emailValidator.test(request.getEmail());

        if(!validatedEmail){
            throw new IllegalStateException(String.format("%s is not valid", request.getEmail()));
        }
        JSONObject jsonObject = new JSONObject();
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                UserRole.valueOf(request.getRole()));  // to provide UserRole type (.getRole() is String))
        userService.signUpUser(user);
        try{
            jsonObject.put("message", "user registered successfully");
        }
        catch(JSONException e){
            try {
                jsonObject.put("exception", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
//        String text = String.format(Objects.requireNonNull(template.getText()));
        emailSender.send(request.getEmail(), templateSimpleMessage().getText());
        return jsonObject;
//        return userService.signUpUser(
//                new User(
//                        request.getUsername(),
//                        request.getPassword(),
//                        request.getEmail(),
//                        UserRole.valueOf(request.getRole())  // to provide UserRole type (.getRole() is String)
//                )
//        );

    }


    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }
}
