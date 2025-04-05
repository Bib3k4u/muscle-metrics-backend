package com.musclemetrics.controller;

import com.musclemetrics.dto.auth.JwtResponse;
import com.musclemetrics.dto.auth.LoginRequest;
import com.musclemetrics.dto.auth.SignupRequest;
import com.musclemetrics.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auth API is working");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            JwtResponse response = authService.registerUser(signupRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}