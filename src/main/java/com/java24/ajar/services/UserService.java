package com.java24.ajar.services;

import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.UserUpdateRequest;
import com.java24.ajar.dto.UserUpdateResponse;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.Role;
import com.java24.ajar.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
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
        LocalDate date = LocalDate.now();

        user.setCreated_at(date.toString());


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
    public UserUpdateResponse updateUser(UserUpdateRequest userUpdateRequest) {
        User updatedUser = getCurrentAuthenticatedUser();


        updatedUser.setEmail(userUpdateRequest.getEmail());
        updatedUser.setFirstName(userUpdateRequest.getFirstName());
        updatedUser.setLastName(userUpdateRequest.getLastName());
        updatedUser.setPhone(userUpdateRequest.getPhone());
        updatedUser.setAddress(userUpdateRequest.getAddress());


        return  convertUserToUserUpdateResponse(userRepository.save(updatedUser));
    }



    // this method gandle the resonse anfer user update
    private UserUpdateResponse convertUserToUserUpdateResponse(User user) {
        UserUpdateResponse userUpdateResponse = new UserUpdateResponse();
        userUpdateResponse.setEmail(user.getEmail());
        userUpdateResponse.setFirstName(user.getFirstName());
        userUpdateResponse.setLastName(user.getLastName());
        userUpdateResponse.setPhone(user.getPhone());
        userUpdateResponse.setAddress(user.getAddress());
        userUpdateResponse.setCreated_at(user.getCreated_at());


        return userUpdateResponse;
    }

    public void deleteUser(String username) {
        User user = findByUsername(username);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new UsernameNotFoundException(user.getUsername() + " not found");
        }
    }

    @Override
    public UserUpdateResponse viewUser() {
        User viewUser  = getCurrentAuthenticatedUser();
        return convertUserToUserUpdateResponse(viewUser);
    }


    public User getCurrentAuthenticatedUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                    authentication instanceof AnonymousAuthenticationToken) {
                throw new UnauthorizedException("User is not authenticated");
            }

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }
    }





















