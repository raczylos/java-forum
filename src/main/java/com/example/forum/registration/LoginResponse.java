package com.example.forum.registration;

import lombok.*;

@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginResponse {
    private final String username;
    private final String email;
    private final String role;
    private final String token;
}
