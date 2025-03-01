package com.java24.ajar.services;

import com.java24.ajar.models.User;

public interface UserServiceImp {
    void registerUser(User user);
    User findByUsername(String username);
    boolean existsByUsername(String username);
    User updateUser(String username, User user);
    void deleteUser(String username);
}
