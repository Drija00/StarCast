package com.example.Stars.read_model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "Follows")
@NoArgsConstructor
public class FollowSummary {

    @Id
    @Column(name = "follow_id")
    private UUID followId;

    @ManyToOne
    @JoinColumn(name = "follower_id",nullable = false)
    private UserSummary follower;

    @ManyToOne
    @JoinColumn(name = "followee_id",nullable = false)
    private UserSummary followee;

    @Column(nullable = false)
    private LocalDate timestamp;

    public FollowSummary(UUID followId, UserSummary follower, UserSummary followee, LocalDate timestamp) {
        this.followId = followId;
        this.follower = follower;
        this.followee = followee;
        this.timestamp = timestamp;
    }
}
