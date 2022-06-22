package com.example.forum.topic;

import com.example.forum.user.User;
import lombok.Getter;

import java.util.List;

@Getter
public class TopicCreateRequest {
    private String title;
    private String username;
}
