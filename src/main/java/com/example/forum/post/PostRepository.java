package com.example.forum.post;

import com.example.forum.topic.Topic;
import com.example.forum.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PostRepository extends CrudRepository<Post, Long> {
    Set<Post> findByUser(User user);
    Set<Post> findByTopic(Topic topic);
}