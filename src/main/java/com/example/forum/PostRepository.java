package com.example.forum;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, String> {
    List<Post> findByLastName(String lastName);

}