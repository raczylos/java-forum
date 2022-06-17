package com.example.forum.post;

import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, String> {
//    List<Post> findByLastName(String lastName);

}