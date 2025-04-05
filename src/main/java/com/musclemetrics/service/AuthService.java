package com.musclemetrics.service;

import com.musclemetrics.dto.auth.JwtResponse;
import com.musclemetrics.dto.auth.LoginRequest;
import com.musclemetrics.dto.auth.SignupRequest;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    JwtResponse registerUser(SignupRequest signupRequest);
}