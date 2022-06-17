package com.example.forum.registration;

import com.example.forum.user.User;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserService userService;
    private EmailValidator emailValidator;
    public String register(RegistrationRequest request) {
        boolean validatedEmail =  emailValidator.test(request.getEmail());

        if(!validatedEmail){
            throw new IllegalStateException(String.format("%s is not valid", request.getEmail()));
        }
        return userService.signUpUser(
                new User(
                        request.getUsername(),
                        request.getPassword(),
                        request.getEmail(),
                        UserRole.USER
                )
        );
//        return "work";
    }
}
