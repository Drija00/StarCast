package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class FollowDTO {

    private UUID followId;

    private UserDTO follower;

    private UserDTO followee;

    private LocalDateTime timestamp;

    public FollowDTO(UUID followId, UserDTO follower, UserDTO followee, LocalDateTime timestamp) {
        this.followId = followId;
        this.follower = follower;
        this.followee = followee;
        this.timestamp = timestamp;
    }

}


