package com.java24.ajar.services;

import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.models.Role;
import com.java24.ajar.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
public class UserService implements UserServiceImp{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // register user
    public void registerUser(User user) {
        // hash password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // ensure the user has at least default role USER
        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }

        userRepository.save(user);
    }

    // find user by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // check if username already exists
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    // update a user informantion
    public User updateUser(String username, User user) {
        User userToUpdate = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(user.getUsername() + " not found"));

        userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        userToUpdate.setRoles(user.getRoles());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPhone(user.getPhone());
        user.setAddress(user.getAddress());
        return userRepository.save(userToUpdate);
    }

    public void deleteUser(String username) {
        User user = findByUsername(username);
        if(user != null) {
            userRepository.delete(user);
        } else {
            throw new UsernameNotFoundException(user.getUsername() + " not found");
        }
    }




















}
