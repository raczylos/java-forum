package com.example.forum.user;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository ;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByEmail(s)
                .or(() -> userRepository.findByUsername(s))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email/username %s not found!", s)));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if(userExists){
            throw new IllegalStateException("user/email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        userRepository.save(user);  // juz nie wywala bledu ;]

        String message = String.format("password: %s email: %s username: %s role: %s", user.getPassword(), user.getEmail(), user.getUsername(), user.getUserRole());
        return message;

    }
}
