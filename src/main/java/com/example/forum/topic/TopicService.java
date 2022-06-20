package com.example.forum.topic;

import com.example.forum.post.Post;
import com.example.forum.user.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TopicService {
    Optional<Topic> findOneById(Long id);
    Iterable<Topic> findAll();
    Set<Topic> findByUser(User user);
    void save(Topic topic);
    void deleteById(Long id);
    void delete(Topic topic);
}
