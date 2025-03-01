package com.java24.ajar.services;

import com.java24.ajar.models.User;

import java.util.List;

public interface AdminServiceImp {
    List<User> findAllUsers(String role);

}
