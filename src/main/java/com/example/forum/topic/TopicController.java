package com.example.forum.topic;

import org.springframework.web.bind.annotation.*;

@RestController
public class TopicController {
    private TopicService topicService;

    // CREATE A TOPIC
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/create")
    public void createTopic() {

    }


    // REPLY TO A TOPIC
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/{topicId}/reply")
    public void replyToTopic(@PathVariable String topicId) {

    }
    //TODO: control topics
}
