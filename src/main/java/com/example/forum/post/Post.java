package com.example.forum.post;

import com.example.forum.topic.Topic;
import com.example.forum.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String content;

    @ManyToOne()
    private User user;

    @JsonIgnore
    @ManyToOne()
    private Topic topic;

    public Post() {}

    public Post(String content) {
        this.content = content;
    }

//    @Override
//    public String toString() {
//        return String.format(
//                "Customer[id=%d, firstName='%s', lastName='%s']",
//                id, firstName, lastName);
//    }
}