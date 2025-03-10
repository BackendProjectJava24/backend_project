package com.java24.ajar.controllers;

import com.java24.ajar.models.User;
import com.java24.ajar.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/showallusers")
    public ResponseEntity<?> showAllUsers() {
        List<User> userList = adminService.findAllUsers("USER");
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }
}
