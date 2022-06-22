package com.example.forum.post;

import com.example.forum.email.EmailSender;
import com.example.forum.topic.Topic;
import com.example.forum.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostService {
    Optional<Post> findById(Long id);
    Set<Post> findByUser(User user);
    List<Post> findAll();
    void save(Post post);
    void delete(Post post);
    void deleteById(Long id);
    public void sendEmail(User user, String pathString, String body, String content);

}
