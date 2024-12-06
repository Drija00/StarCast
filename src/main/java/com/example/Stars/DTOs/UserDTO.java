package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private UUID userId;
    private String username;
    private String email;
    private String password;
    private boolean active = false;

    public UserDTO(UUID userId) {
        this.userId = userId;
    }

    public UserDTO(UUID userId, String username, String email, String password, boolean active) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }


    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active+
                '}';
    }
}
