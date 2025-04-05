package com.musclemetrics.service.impl;

import com.musclemetrics.dto.auth.JwtResponse;
import com.musclemetrics.dto.auth.LoginRequest;
import com.musclemetrics.dto.auth.SignupRequest;
import com.musclemetrics.model.User;
import com.musclemetrics.repository.UserRepository;
import com.musclemetrics.security.JwtTokenProvider;
import com.musclemetrics.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userDetails.getUsername()));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles);
    }

    @Override
    public JwtResponse registerUser(SignupRequest signupRequest) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException(
                    "Email is already in use! Please use a different email address or try to log in.");
        }

        // Create new user's account
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        // Set roles
        Set<String> roles = signupRequest.getRoles();
        if (roles == null) {
            user.getRoles().add("USER");
        } else {
            user.setRoles(roles);
        }

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage());
        }

        // Auto login after registration
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(signupRequest.getEmail());
        loginRequest.setPassword(signupRequest.getPassword());

        return authenticateUser(loginRequest);
    }
}