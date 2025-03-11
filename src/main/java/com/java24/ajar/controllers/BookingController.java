package com.java24.ajar.controllers;

import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.AuthRequest;
import com.java24.ajar.dto.RegisterRequest;
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
public class BookingController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // ✅ Update user method fixed
    @PatchMapping("/update/{userId}")
    public ResponseEntity<?> userUpdate(@Valid @PathVariable String userId, @RequestBody RegisterRequest registerRequest) {
        User userToUpdate = userService.findById(userId);

        // ✅ Ensure only non-null fields are updated
        if (registerRequest.getFirstName() != null) userToUpdate.setFirstName(registerRequest.getFirstName());
        if (registerRequest.getLastName() != null) userToUpdate.setLastName(registerRequest.getLastName());
        if (registerRequest.getEmail() != null) userToUpdate.setEmail(registerRequest.getEmail());
        if (registerRequest.getPhone() != null) userToUpdate.setPhone(registerRequest.getPhone());
        if (registerRequest.getAddress() != null) userToUpdate.setAddress(registerRequest.getAddress());

        UpdateUserDTO UpdateUserDTO = new UpdateUserDTO();
        userService.updateUserById(userId, UpdateUserDTO);

        RegisterResponse response = new RegisterResponse(
                "User updated successfully",
                userToUpdate.getUsername(),
                userToUpdate.getRoles(),
                userToUpdate.getFirstName(),
                userToUpdate.getLastName(),
                userToUpdate.getEmail(),
                userToUpdate.getPhone(),
                userToUpdate.getAddress(),
                userToUpdate.getCreated_at()
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // ✅ Fixed delete user method
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable String userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.deleteUser(userId);

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
