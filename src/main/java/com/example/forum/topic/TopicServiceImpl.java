package com.example.forum.topic;

import com.example.forum.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private TopicRepository topicRepository;

    @Override
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    @Override
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Set<Topic> findByUser(User user) {
        return topicRepository.findByUser(user);
    }

    @Override
    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    @Override
    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }

    @Override
    public void delete(Topic topic) {
        topicRepository.delete(topic);
    }
}
