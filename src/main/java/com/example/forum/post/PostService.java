package com.example.forum.post;

import com.example.forum.email.EmailSender;
import com.example.forum.topic.Topic;
import com.example.forum.user.User;

import java.util.List;
import java.util.Set;

public interface PostService {
    Post findOneById(Long id);

    List<Post> findAll();

    Set<Post> findByUser(User user);

    Set<Post> findByTopic(Topic topic);

    void save(Post post);

    void delete(int id);

    void delete(Post post);

    void save(String content, String username);
}
