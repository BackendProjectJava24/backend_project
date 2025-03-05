package com.java24.ajar.controllers;

import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.AuthRequest;
import com.java24.ajar.dto.RegisterRequest;
import com.java24.ajar.dto.RegisterResponse;
import com.java24.ajar.models.User;
import com.java24.ajar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
@Autowired
private UserService userService;
@Autowired
private  UserRepository userRepository;









    // update user
    @PatchMapping("/userUpdate/{username}")
    public ResponseEntity<?> userUpdate(@Valid @PathVariable String username, @RequestBody RegisterRequest registerRequest) {
        User usetToUpdate = userService.findByUsername(username);

        RegisterResponse response1 = new RegisterResponse(
                "User updated successfully",
                registerRequest.getUsername(),
                registerRequest.getRoles(),
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getEmail(),
                registerRequest.getPhone(),
                registerRequest.getAddress(),
                registerRequest.getCreated_at()
        );
        usetToUpdate.setRoles(registerRequest.getRoles());
        usetToUpdate.setPassword(registerRequest.getPassword());
        usetToUpdate.setFirstName(registerRequest.getFirstName());
        usetToUpdate.setLastName(registerRequest.getLastName());
        usetToUpdate.setEmail(registerRequest.getEmail());
        usetToUpdate.setPhone(registerRequest.getPhone());
        usetToUpdate.setAddress(registerRequest.getAddress());
        usetToUpdate.setCreated_at(registerRequest.getCreated_at());
        userService.updateUser(username, usetToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(response1);
    }

    @DeleteMapping("/userDelete/{username}")
    public ResponseEntity<?> deteteUser(@Valid @PathVariable String username, @RequestBody AuthRequest authRequest) {
        User user = userService.findByUsername(username);
        RegisterResponse response = new RegisterResponse(
                "user deleted successfully.",
                user.getUsername(),
                user.getRoles(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getCreated_at()
        );
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
