package com.jobportal.job_portal.controller;

import com.jobportal.job_portal.dto.LoginRequest;
import com.jobportal.job_portal.dto.LoginResponse;
import com.jobportal.job_portal.dto.RegisterRequest;
import com.jobportal.job_portal.dto.UserResponse;
import com.jobportal.job_portal.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}