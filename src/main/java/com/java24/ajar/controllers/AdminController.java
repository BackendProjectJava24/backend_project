package com.java24.ajar.controllers;

import com.java24.ajar.models.User;
import com.java24.ajar.services.AdminService;
import com.java24.ajar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService; // ✅ Inject UserService to retrieve authenticated admin

    // ✅ Show all users
    @GetMapping("/showallusers")
    public ResponseEntity<?> showAllUsers() {
        List<User> userList = adminService.findAllUsers("USER");
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    //
    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String adminId) {
        // Get currently logged-in admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedAdminId = userService.getAuthenticatedUser().getId(); // ✅ Get logged-in admin ID

        // Prevent deletion
        if (adminId.equals(authenticatedAdminId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admins cannot delete their own account.");
        }

        // Proceed with deletion only if it's another admin
        adminService.deleteUser(adminId); // ✅ Only delete if it's another admin
        return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully.");
    }
}
