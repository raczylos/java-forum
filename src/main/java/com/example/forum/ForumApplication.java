package com.example.forum;

import com.example.forum.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@RestController
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

//	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/for-user")
	public String hello1() {
		return String.format("Hello user!");
	}


	@GetMapping("/for-admin")
	public String hello2() {
		return String.format("Hello admin!");
	}

	@GetMapping("/bye")
	public String hello3() {
		return String.format("bye!");
	}

	@CrossOrigin(origins="*", maxAge=3600)
	@GetMapping("api/v1/username")
	public String currentUserName(Principal principal) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String name = user.getEmail();
		return String.format("hello %s", name);
	}

}