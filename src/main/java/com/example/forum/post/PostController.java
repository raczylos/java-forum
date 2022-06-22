package com.example.forum.post;

import com.example.forum.email.EmailSender;
import com.example.forum.topic.Topic;
import com.example.forum.topic.TopicRepository;
import com.example.forum.topic.TopicUpdateRequest;
import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class PostController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PostServiceImpl postServiceImpl;

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @CrossOrigin(origins="http://localhost:3000")
    @PostMapping(path="api/v1/topics/{topicId}/reply")
    public String replyToTopic(Authentication auth, @PathVariable String topicId, @RequestBody Post post) {
        logger.info("replying to topic " + topicId);
        User user;
        Topic topic;
        boolean isUserPresent = userRepository.findByUsername(auth.getName()).isPresent();
        boolean isTopicPresent = topicRepository.findById(Long.parseLong(topicId)).isPresent();
        if(isUserPresent && isTopicPresent) {
            user = userRepository.findByUsername(auth.getName()).get();
            topic = topicRepository.findById(Long.parseLong(topicId)).get();
            post.setTopic(topic);
            post.setUser(user);
            postRepository.save(post);

//            logger.info(user.getEmail());
            String pathString = String.format("http://localhost:3000/topics/%s", topicId);
//            emailSender.send(user.getEmail(), templateSimpleMessage().getText());
            for (User u : topic.getFollows()) {
                postServiceImpl.sendEmail(u, pathString);
            }

            return "success";
        }
        logger.error("failed to reply to topic " + topicId);
        return "failure";
    }

    @CrossOrigin(origins="http://localhost:3000")
    @PutMapping(path="api/v1/posts/{postId}")
    public String updateReplyToTopic(@PathVariable String postId,@RequestBody String content){
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put();

        Optional<Post> post = postRepository.findById(Long.valueOf(postId));

        boolean isPostPresent = post.isPresent();

        if(isPostPresent) {

            post.get().setContent(content);

            postRepository.save(post.get());
            logger.info("updating post " + postId);
            return "success";

        }
        logger.info(String.format("updating post: %s failure", postId));
        return "failure";
    }


    @CrossOrigin(origins="http://localhost:3000")
    @DeleteMapping(path="api/v1/posts/{postId}")
    public String deletePost(Authentication auth, @PathVariable String postId) {
        logger.info("deleting post " + postId);

        boolean isPostPresent = postRepository.findById(Long.parseLong(postId)).isPresent();
        if(isPostPresent) {
            postRepository.deleteById(Long.parseLong(postId));
            return "success";
        }
        logger.error("failed to delete post " + postId);
        return "failure";
    }




}


