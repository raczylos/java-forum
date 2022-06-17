package com.example.forum.topic;

import com.example.forum.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TopicRepository extends CrudRepository<Topic, Long> {
    public List<Topic> findByName(String name);
    public Set<Topic> findByUser(User user);
}
