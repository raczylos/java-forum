package com.example.forum.topic;

import com.example.forum.post.Post;
import com.example.forum.security.JwtFilter;
import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TopicController {

//    private TopicService topicService;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TopicController.class);


    // CREATE A TOPIC
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/create")
    public String createTopic(Authentication auth, @RequestBody TopicCreateRequest request) {
        log.info("creating new topic");
        JSONObject jsonObject = new JSONObject();
        User user;
        Topic topic;
        boolean isUserPresent = userRepository.findByUsername(auth.getName()).isPresent();
        if(isUserPresent) {
            user = userRepository.findByUsername(auth.getName()).get();
            topic = new Topic(request.getTitle());
            topic.setUser(user);
            topicRepository.save(topic);
        }
        try{
            jsonObject.put("title", request.getTitle());
            jsonObject.put("username", auth.getName());
//            jsonObject.put("username", request.getUsername());
            return jsonObject.toString();
        }
        catch(JSONException e){
            try {
                jsonObject.put("exception", e.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            log.error("failed to create new topic");
            return jsonObject.toString();
        }
    }

    @CrossOrigin(origins="http://localhost:3000")
    @GetMapping(path="api/v1/topics")
    public List<Topic> getAllTopics(){
        log.info("reading all topics");
        return topicRepository.findAll();
    }
    
    @CrossOrigin(origins="http://localhost:3000")
    @PutMapping(path="api/v1/topics/{topicId}")
    public String updateTopic(@PathVariable String topicId, @RequestBody TopicUpdateRequest request){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put();

        Optional<Topic> topic = topicRepository.findById(Long.parseLong(topicId));

        boolean isTopicPresent = topic.isPresent();

        if(isTopicPresent) {

            topic.get().setTitle(request.getTitle());

            topicRepository.save(topic.get());
            log.info("updating topic " + topicId);
            return "success";

        }
        log.info(String.format("updating topic: %s failure", topicId));
        return "failure";
    }



    @CrossOrigin(origins="http://localhost:3000")
    @DeleteMapping(path="api/v1/topics/{topicId}")
    public String deleteTopic(@PathVariable String topicId) {
        log.info("deleting topic " + topicId);
        boolean isTopicPresent = topicRepository.findById(Long.parseLong(topicId)).isPresent();
        if(isTopicPresent) {
            topicRepository.deleteById(Long.parseLong(topicId));
            return "success";
        }
        log.error("failed to delete topic " + topicId);
        return "failure";
    }

    @CrossOrigin(origins="http://localhost:3000")
    @GetMapping(path="api/v1/topics/{topicId}")
    public Topic getTopic(@PathVariable String topicId){
        log.info("reading topic " + topicId);
        boolean isTopicPresent = topicRepository.findById(Long.parseLong(topicId)).isPresent();
        if(isTopicPresent) {
            return topicRepository.findById(Long.parseLong(topicId)).get();
        }
        log.error("cannot read topic " + topicId);
        return null;
    }
    // REPLY TO A TOPIC

}
