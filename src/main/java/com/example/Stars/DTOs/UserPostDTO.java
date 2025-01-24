package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserPostDTO {

    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean active = false;

    public UserPostDTO(String username, String email, String password, String firstName, String lastName, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserPostDTO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", active=" + active +
                '}';
    }
}
