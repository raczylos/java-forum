package com.example.forum;

import com.example.forum.user.User;
import com.example.forum.user.UserRepository;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
class ForumApplicationTests {

//	@Test
//	void contextLoads() {
//	}
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void registrationTest() {
		User user1 = new User(
				"username",
				"password",
				"username@example.com",
				UserRole.USER
		);

		userService.signUpUser(user1);

		boolean isUserCreated = userRepository.findByUsername(user1.getUsername()).isPresent();

		assertTrue(isUserCreated);
	}







}
