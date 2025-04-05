package com.musclemetrics.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserProfileRequest {

    @Size(min = 3, max = 20)
    private String username;

    @Size(max = 50)
    @Email
    private String email;

    private Double weight;

    private Double height;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}