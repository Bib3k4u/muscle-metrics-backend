package com.musclemetrics.controller;

import com.musclemetrics.dto.user.PasswordChangeRequest;
import com.musclemetrics.dto.user.UserProfileRequest;
import com.musclemetrics.model.User;
import com.musclemetrics.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentUserProfile() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UserProfileRequest request) {
        User updatedUser = userService.updateProfile(request);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        boolean success = userService.changePassword(request);
        
        if (success) {
            return ResponseEntity.ok().body(new MessageResponse("Password changed successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Current password is incorrect"));
        }
    }
    
    static class MessageResponse {
        private String message;
        
        public MessageResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
} 