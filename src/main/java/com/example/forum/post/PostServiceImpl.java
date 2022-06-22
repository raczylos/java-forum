package com.example.forum.post;

import com.example.forum.email.EmailSender;
import com.example.forum.email.EmailService;
import com.example.forum.user.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{

    private final EmailSender emailSender;
    private PostRepository postRepository;


    @Async
    public void sendEmail(User user, String pathString, String body, String content){
        emailSender.send(user.getEmail(), templateSimpleMessage(pathString, body, content).getText(), body);
    }

    public SimpleMailMessage templateSimpleMessage(String pathString, String body, String content) {
//        pathString.replaceAll("(\\A|\\s)((http|https|ftp|mailto):\\S+)(\\s|\\z)",
//                "$1<a href=\"$2\">$2</a>$4");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(body + content + pathString );
        return message;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Set<Post> findByUser(User user) {
        return postRepository.findByUser(user);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }
}
