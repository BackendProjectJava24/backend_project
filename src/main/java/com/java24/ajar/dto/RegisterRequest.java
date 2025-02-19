package com.java24.ajar.dto;

import com.java24.ajar.models.Role;
import jakarta.validation.constraints.NotBlank;


import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<Role> roles;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String created_at;

    public RegisterRequest(String username, String password, Set<Role> roles, String firstName, String lastName, String email, String phone, String address, String created_at) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.created_at = created_at;
    }



    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
LocalDate date = LocalDate.now();

    public String getCreated_at() {
        return date.toString();
    }
}
