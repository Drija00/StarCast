package com.example.Stars.dto;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDTO {
    private UUID userId;
    private String username;
    private String email;
    private boolean active;

    public UserDTO(UUID userId, String username, String email, boolean active) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.active = active;
    }
}
