package com.musclemetrics.service.impl;

import com.musclemetrics.dto.user.PasswordChangeRequest;
import com.musclemetrics.dto.user.UserProfileRequest;
import com.musclemetrics.model.User;
import com.musclemetrics.repository.UserRepository;
import com.musclemetrics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + principal.getUsername()));
    }

    @Override
    public User updateProfile(UserProfileRequest request) {
        User currentUser = getCurrentUser();

        if (request.getUsername() != null) {
            currentUser.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(currentUser.getEmail())) {
            // Check if the new email is already taken
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email is already in use!");
            }
            currentUser.setEmail(request.getEmail());
        }

        if (request.getWeight() != null) {
            currentUser.setWeight(request.getWeight());
        }

        if (request.getHeight() != null) {
            currentUser.setHeight(request.getHeight());
        }

        return userRepository.save(currentUser);
    }

    @Override
    public boolean changePassword(PasswordChangeRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            return false;
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

        return true;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}