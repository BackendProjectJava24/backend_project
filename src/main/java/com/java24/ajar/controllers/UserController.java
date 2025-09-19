package com.java24.ajar.controllers;

import com.java24.ajar.dto.*;
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


@GetMapping("/viewuser")
public  ResponseEntity<UserUpdateResponse> viewUser() {
    UserUpdateResponse userUpdateResponse = userService.userDeteils();
    return new ResponseEntity<>(userUpdateResponse, HttpStatus.OK);
}


    // update user
    @PutMapping("/userUpdate")
    public ResponseEntity<UserUpdateResponse> updateUser(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        UserUpdateResponse userUpdateResponse = userService.updateUser(userUpdateRequest);
        return new ResponseEntity<>(userUpdateResponse, HttpStatus.OK);
    }


    @DeleteMapping("/userDelete/{username}")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable String username, @RequestBody AuthRequest authRequest) {
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
