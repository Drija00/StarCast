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
    private String firstName;
    private String lastName;
    private String profileImage;

    public UserFollowDTO(UUID userId) {
        this.userId = userId;
    }

    public UserFollowDTO(UUID userId, String username, String firstName, String lastName, String profileImage) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserFollowDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePicture='" + profileImage + '\'' +
                '}';
    }
}
