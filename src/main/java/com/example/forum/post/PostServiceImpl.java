package com.example.forum.post;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.user.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    private final EmailSender emailSender;


    @Async
    public void sendEmail(User user, String pathString){
        emailSender.send(user.getEmail(), templateSimpleMessage(pathString).getText());
    }

    public SimpleMailMessage templateSimpleMessage(String pathString) {
//        pathString.replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
//                "$1<a href=\"$2\">$2</a>$4");
//        String pathString = "o";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("New post: " + pathString);
        return message;
    }
}
