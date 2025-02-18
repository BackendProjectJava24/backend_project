package com.java24.ajar.controllers;

import com.java24.ajar.util.JwtUtil;
import com.java24.ajar.dto.RegissterRequest;
import com.java24.ajar.dto.RegisterResponse;
import com.java24.ajar.models.Role;
import com.java24.ajar.models.User;
import com.java24.ajar.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil  jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> regiserUser(@Valid @RequestBody RegissterRequest registerRequest ) {
        // check if username already exists
        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        // map the authRequest to a User entity
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());

        // assign roles
        if (registerRequest.getRoles() != null || registerRequest.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        } else {
            user.setRoles(registerRequest.getRoles());
        }

        // register the user useing UserSeervice
        userService.registerUser(user);

        // create response object
        RegisterResponse response = new RegisterResponse(
                "User registered successfully"
                , user.getUsername()
                , user.getRoles()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
