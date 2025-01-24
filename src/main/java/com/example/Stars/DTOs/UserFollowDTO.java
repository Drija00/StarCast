package com.example.Stars.DTOs;

import com.example.Stars.queries.read_model.UserSummary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UserFollowDTO {
    private UUID userId;
    private String username;
    private String email;
    private String password;
    private boolean active = false;
    private String firstName;
    private String lastName;
    private LocalDateTime joinDate;
    private String profileImage;
    private String backgroundImage;

    public UserFollowDTO(UUID userId) {
        this.userId = userId;
    }

    public UserFollowDTO(UUID userId, String username, String email, String password, boolean active, String firstName, String lastName, LocalDateTime joinDate, String profileImage, String backgroundImage) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
    }

    @Override
    public String toString() {
        return "UserFollowDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", joinDate=" + joinDate +
                ", profileImage='" + profileImage + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                '}';
    }
}
