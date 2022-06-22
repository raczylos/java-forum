package com.example.forum.topic;

import com.example.forum.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    public List<Topic> findByTitle(String title);
    public Set<Topic> findByUser(User user);
    public void deleteById(Long id);


}
