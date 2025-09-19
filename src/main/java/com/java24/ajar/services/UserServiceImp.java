package com.java24.ajar.services;

import com.java24.ajar.dto.UserUpdateRequest;
import com.java24.ajar.dto.UserUpdateResponse;
import com.java24.ajar.models.User;

public interface UserServiceImp {
    void registerUser(User user);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    UserUpdateResponse updateUser( UserUpdateRequest  userUpdateRequest);
    void deleteUser(String username);
    UserUpdateResponse userDeteils();
}
