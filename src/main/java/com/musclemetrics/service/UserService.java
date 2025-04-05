package com.musclemetrics.service;

import com.musclemetrics.dto.user.PasswordChangeRequest;
import com.musclemetrics.dto.user.UserProfileRequest;
import com.musclemetrics.model.User;

public interface UserService {
    User getCurrentUser();

    User updateProfile(UserProfileRequest request);

    boolean changePassword(PasswordChangeRequest request);

    User findByEmail(String email);

    User findById(String id);
}