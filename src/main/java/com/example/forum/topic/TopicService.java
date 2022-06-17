package com.example.forum.topic;

import com.example.forum.post.Post;
import com.example.forum.user.User;

import java.util.List;
import java.util.Set;

public interface TopicService {
    Topic findOneById(Long id);
    List<Topic> findAll();
    Set<Topic> findByUser(User user);
    void save(Topic topic);
    void delete(Long id);
    void delete(Topic topic);
}
