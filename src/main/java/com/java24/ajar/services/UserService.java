package com.java24.ajar.services;

import com.java24.ajar.dto.UpdateUserDTO;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.models.Role;  // ✅ Added missing import
import com.java24.ajar.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService implements UserServiceImp {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Register user
    public void registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null.");
        }

        // Hash password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Ensure user has at least a default role
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER)); // ✅ Fixed missing Role import
        }

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return null;
    }

    // ✅ Find user by ID
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    // ✅ Check if username already exists
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User updateUser(String username, User user) {
        return null;
    }

    // ✅ Update user by userId
    @Override
    public User updateUserById(String userId, UpdateUserDTO updateUserDTO) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        // Update only allowed fields
        if (updateUserDTO.getFirstName() != null) userToUpdate.setFirstName(updateUserDTO.getFirstName());
        if (updateUserDTO.getLastName() != null) userToUpdate.setLastName(updateUserDTO.getLastName());
        if (updateUserDTO.getEmail() != null) userToUpdate.setEmail(updateUserDTO.getEmail());
        if (updateUserDTO.getPhone() != null) userToUpdate.setPhone(updateUserDTO.getPhone());
        if (updateUserDTO.getAddress() != null) userToUpdate.setAddress(updateUserDTO.getAddress());

        // Save updated user
        return userRepository.save(userToUpdate);
    }

    // ✅ Delete user by userId
    public void deleteUser(String userId) {
        User user = findById(userId);
        userRepository.delete(user);
    }
}
