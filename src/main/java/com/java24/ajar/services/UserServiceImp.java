package com.java24.ajar.services;

import com.java24.ajar.dto.UpdateUserDTO;
import com.java24.ajar.models.User;

public interface UserServiceImp {
    void registerUser(User user);

    User findByUsername(String username);

    User findById(String userId); // ✅ Changed to use userId instead of username
    boolean existsByUsername(String username);

    // ✅ Update user
    User updateUser(String username, User user);

    // Removed updateUserDTO
    User updateUserById(String userId, UpdateUserDTO updateUserDTO); // ✅ Changed method signature to match implementation
    void deleteUser(String userId); // ✅ Changed username to userId for consistency
}
