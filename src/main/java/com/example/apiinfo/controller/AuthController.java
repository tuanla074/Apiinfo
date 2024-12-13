package com.example.apiinfo.controller;

import com.example.apiinfo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    // Mock login endpoint
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // This is just a mock example. In real applications, validate username and password against the database.
        if ("admin".equals(username) && "password".equals(password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
