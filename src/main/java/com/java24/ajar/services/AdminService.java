package com.java24.ajar.services;

import com.java24.ajar.models.User;

import java.util.List;

public interface AdminService {
    List<User> findAllUsers(String role);

}
