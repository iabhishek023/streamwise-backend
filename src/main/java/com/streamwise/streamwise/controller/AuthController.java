package com.streamwise.streamwise.controller;

import com.streamwise.streamwise.dto.ApiResponse;
import com.streamwise.streamwise.dto.AuthResponse;
import com.streamwise.streamwise.dto.LoginRequest;
import com.streamwise.streamwise.dto.RegisterRequest;
import com.streamwise.streamwise.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(
                ApiResponse.success("Registration successful", response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }
}