package com.example.forum.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @CrossOrigin(origins= "http://localhost:3000")
    @PostMapping(path = "api/v1/registration")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

}
