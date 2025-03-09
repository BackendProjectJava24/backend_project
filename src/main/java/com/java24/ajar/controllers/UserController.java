package com.java24.ajar.controllers;

import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.RegisterResponse;
import com.java24.ajar.dto.UpdateUserDTO;
import com.java24.ajar.models.User;
import com.java24.ajar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> userUpdate(
            @Valid @PathVariable String userId,
            @RequestBody UpdateUserDTO updateUserDTO // Use UpdateUserDTO instead of RegisterRequest
    ) {
        // Find the user to update
        User userToUpdate = userService.findById(userId);
        if (userToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Update the user with the data from UpdateUserDTO
        User updatedUser = userService.updateUserById(userId, updateUserDTO);

        // Create a response object
        RegisterResponse response = new RegisterResponse(
                "User updated successfully",
                updatedUser.getUsername(),
                updatedUser.getRoles(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getEmail(),
                updatedUser.getPhone(),
                updatedUser.getAddress(),
                updatedUser.getCreated_at()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable String userId) {
        // Find the user to delete
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Delete the user
        userService.deleteUser(userId);

        // Create a response object
        RegisterResponse response = new RegisterResponse(
                "User deleted successfully.",
                user.getUsername(),
                user.getRoles(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCreated_at()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}