package com.example.forum.registration;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.user.User;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
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




    public String register(RegistrationRequest request) {
        boolean validatedEmail =  emailValidator.test(request.getEmail());

        if(!validatedEmail){
            throw new IllegalStateException(String.format("%s is not valid", request.getEmail()));
        }
//        String text = String.format(Objects.requireNonNull(template.getText()));
        emailSender.send(request.getEmail(), templateSimpleMessage().getText());
        return userService.signUpUser(
                new User(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail(),
                        UserRole.valueOf(request.getRole())  // to provide UserRole type (.getRole() is String)
                )
        );

    }


    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
                "This is the test email template for your email:\n%s\n");
        return message;
    }
}
