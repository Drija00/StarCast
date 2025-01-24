package com.example.Stars.queries.read_model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class FollowSummary {

    private UUID followId;

    private UserSummary follower;

    private UserSummary followee;

    private LocalDateTime timestamp;

    public FollowSummary(UUID followId, UserSummary follower, UserSummary followee, LocalDateTime timestamp) {
        this.followId = followId;
        this.follower = follower;
        this.followee = followee;
        this.timestamp = timestamp;
    }
}
