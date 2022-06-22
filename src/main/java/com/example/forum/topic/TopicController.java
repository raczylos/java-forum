package com.example.forum.topic;

import com.example.forum.post.Post;
import com.example.forum.post.PostService;
import com.example.forum.post.PostServiceImpl;
import com.example.forum.security.JwtFilter;
import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TopicController {

    private static final Logger log = LoggerFactory.getLogger(TopicController.class);
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postServiceImpl;

    @Autowired
    private TopicService topicServiceImpl;

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
            topicServiceImpl.save(topic);
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
        return topicServiceImpl.findAll();
    }
    
    @CrossOrigin(origins="http://localhost:3000")
    @PutMapping(path="api/v1/topics/{topicId}")
    public String updateTopic(@PathVariable String topicId, @RequestBody TopicUpdateRequest request){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put();

        Optional<Topic> topic = topicServiceImpl.findById(Long.parseLong(topicId));

        boolean isTopicPresent = topic.isPresent();

        if(isTopicPresent) {

            topic.get().setTitle(request.getTitle());

            topicServiceImpl.save(topic.get());
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
        boolean isTopicPresent = topicServiceImpl.findById(Long.parseLong(topicId)).isPresent();
        if(isTopicPresent) {
            topicServiceImpl.deleteById(Long.parseLong(topicId));
            return "success";
        }
        log.error("failed to delete topic " + topicId);
        return "failure";
    }

    @CrossOrigin(origins="http://localhost:3000")
    @GetMapping(path="api/v1/topics/{topicId}")
    public Topic getTopic(@PathVariable String topicId){
        log.info("reading topic " + topicId);
        boolean isTopicPresent = topicServiceImpl.findById(Long.parseLong(topicId)).isPresent();
        if(isTopicPresent) {
            return topicServiceImpl.findById(Long.parseLong(topicId)).get();
        }
        log.error("cannot read topic " + topicId);
        return null;
    }

    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/{topicId}/report")
    public String reportTopic(@PathVariable String topicId){
        log.info("sending report topic " + topicId);

        boolean isTopicPresent = topicServiceImpl.findById(Long.parseLong(topicId)).isPresent();
        if(isTopicPresent) {
            for (User u : userRepository.findAll()) {
                if(u.getUserRole() == UserRole.ADMIN){
                    String pathString = String.format("http://localhost:3000/topics/%s", topicId);
                    String body = "Reported topic: ";
                    String content = topicServiceImpl.findById(Long.parseLong(topicId)).get().getTitle() + " ";

                    postServiceImpl.sendEmail(u, pathString, body, content);
                }

            }
            return "success";
        }
        log.error("cannot send report topic " + topicId);
        return null;
    }



    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/{topicId}/follow")
    public String followTopic(@PathVariable String topicId, Authentication auth){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put();

        Optional<Topic> topic = topicServiceImpl.findById(Long.parseLong(topicId));
        Optional<User> user = userRepository.findByUsername(auth.getName());

        boolean isTopicPresent = topic.isPresent();
        boolean isUserPresent = user.isPresent();

        if(isTopicPresent && isUserPresent) {

//            topic.get().setFollowedByUser((List<User>) user.get());
            topic.get().addFollow(user.get());
            topicServiceImpl.save(topic.get());
            log.info("following topic " + topicId);
            return "success";

        }
        log.info(String.format("following topic: %s failure", topicId));
        return null;
    }

}
