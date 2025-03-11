package com.java24.ajar.services;


import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements AdminServiceImp {
    @Autowired
    private UserRepository userRepository;






    // get only users but do not get the admins
    public List<User> findAllUsers(String role) {
        List<User> users = userRepository.findByRoles(role);
        if (users.isEmpty() || users == null){
            throw  new IllegalArgumentException("there is no users associated with this role");
        }
        return users;
    }

    public void deleteUser(String adminId) {
    }
}
