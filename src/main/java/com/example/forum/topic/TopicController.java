package com.example.forum.topic;

import com.example.forum.security.JwtFilter;
import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
public class TopicController {
    private TopicService topicService;
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(TopicController.class);


    // CREATE A TOPIC
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/create")
    public String createTopic(Authentication auth, @RequestBody TopicCreateRequest request) {
        JSONObject jsonObject = new JSONObject();
        log.info(auth.getName());
        User user;
        Topic topic;
        boolean isUserPresent = userRepository.findByUsername(request.getUsername()).isPresent();
        if(isUserPresent) {
            user = userRepository.findByUsername(request.getUsername()).get();
            topic = new Topic(request.getTitle());
            topic.setUser(user);
            topicService.save(topic);
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
            return jsonObject.toString();
        }
    }


    // REPLY TO A TOPIC
    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/{topicId}/reply")
    public void replyToTopic(@PathVariable String topicId) {

    }
    //TODO: control topics
}
