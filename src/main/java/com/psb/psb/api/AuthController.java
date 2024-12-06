package com.psb.psb.api;

import com.psb.psb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> creds) {
        String token = authService.login(creds.get("username"), creds.get("password"));
        return Map.of("token", token);
    }
}
