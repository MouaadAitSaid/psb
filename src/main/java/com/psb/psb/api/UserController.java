package com.psb.psb.api;

import com.psb.psb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> createUser(@RequestBody Map<String,String> userInfo) {
        userService.createUser(
                userInfo.get("username"),
                userInfo.get("firstName"),
                userInfo.get("lastName"),
                userInfo.get("email"),
                userInfo.get("password")
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
