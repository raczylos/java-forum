package com.example.forum.topic;

import com.example.forum.post.Post;
import com.example.forum.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Topic {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToOne()
    private User user;

    @JsonIgnore
    @ManyToMany(mappedBy = "followedTopics")
    Set<User> follows;

    @OneToMany(mappedBy="topic", orphanRemoval=true, cascade=CascadeType.PERSIST)
    private List<Post> posts;

    public Topic() {}
    public Topic(String title) {
        this.title = title;
    }
    public void addFollow(User user){
        follows.add(user);
        user.getFollowedTopics().add(this);
    }
}
