package com.example.forum.security;

//import org.springframework.contex.annotation.Configuration

import com.example.forum.user.UserRepository;
import com.example.forum.user.UserRole;
import com.example.forum.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        User user = new User("Jan",
//                getBcryptPasswordEncoder().encode("jan123"),
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
//        User admin = new User("admin",
//                getBcryptPasswordEncoder().encode("admin123"),
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
//
//        auth.inMemoryAuthentication().withUser(user);
//        auth.inMemoryAuthentication().withUser(admin);
//    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // we can use postman now (it should be enabled to prevent some attacks)
                .authorizeRequests()
                .antMatchers("/api/v*/registration/**").permitAll()
//                .antMatchers("/for-user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/for-user", "/username").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/for-admin").hasAuthority( "ADMIN")
                .anyRequest().hasRole("ADMIN")
                .and()
                .formLogin().permitAll()
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("key")
                .and()
                .logout() // we can logout using path /logout
//                .logoutSuccessUrl("/bye").permitAll();
                .permitAll()
                .deleteCookies("JSESSIONID", "remember-me");


    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

}

