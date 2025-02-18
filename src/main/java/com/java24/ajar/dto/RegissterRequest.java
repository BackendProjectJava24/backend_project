package com.java24.ajar.dto;

import com.java24.ajar.models.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class RegissterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Set<Role> roles;

    public RegissterRequest(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public @NotBlank String getUsername() {
        return username;
    }



    public @NotBlank String  getPassword() {
        return password;
    }


    public Set<Role> getRoles() {
        return roles;
    }


}
