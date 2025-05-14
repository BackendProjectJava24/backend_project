package com.java24.ajar.dto;



import com.java24.ajar.models.Role;

import java.util.Set;

public class AuthResponse {
    private String jwtToken;
    private String username;
    private Set<Role> roles;
    private String firstName;
    private String lastName;


    public AuthResponse(String jwtToken, String username, Set<Role> roles ) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;

    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
