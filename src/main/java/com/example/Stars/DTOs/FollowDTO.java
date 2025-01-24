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

    private UserFollowDTO follower;

    private UserFollowDTO followee;

    private LocalDateTime timestamp;

    public FollowDTO(UUID followId, UserFollowDTO follower, UserFollowDTO followee, LocalDateTime timestamp) {
        this.followId = followId;
        this.follower = follower;
        this.followee = followee;
        this.timestamp = timestamp;
    }
}


