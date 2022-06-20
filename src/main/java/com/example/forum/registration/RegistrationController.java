package com.example.forum.registration;

import lombok.AllArgsConstructor;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @CrossOrigin(origins= "http://localhost:3000")  // CORS
    @PostMapping(path = "api/v1/registration")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @CrossOrigin(origins= "http://localhost:3000")  // CORS
    @PostMapping(path = "api/v1/login")
    public String login(@RequestBody RegistrationRequest request){
        return registrationService.login(request);
    }

}
