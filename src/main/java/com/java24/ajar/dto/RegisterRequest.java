package com.java24.ajar.dto;

import com.java24.ajar.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class RegisterRequest {
    @NotBlank(message = "the username connot be empty or null")
    private String username;
    @NotBlank(message = "the password connot be empty or null")
    private String password;
    private Set<Role> roles;
    @NotBlank(message = "the firstname connot be empty or null")
    private String firstName;
    @NotBlank(message = "the llstname connot be empty or null")
    private String lastName;
    @NotBlank(message = "the Email connot be empty or null")
    private String email;
    @NotBlank(message = "the email connot be empty or null")
    private String phone;
    private String address;
    @NotBlank
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

    public RegisterRequest() {
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

    public void setUsername(@NotBlank(message = "the username connot be empty or null") String username) {
        this.username = username;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(@Email @NotBlank String email) {
        this.email = email;
    }

    public void setPhone(@NotBlank String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreated_at(@NotBlank String created_at) {
        this.created_at = created_at;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
