package com.example.Stars.queries.read_model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "Likes")
@NoArgsConstructor
public class LikeSummary {
    @Id
    @Column(name = "like_id")
    private UUID likeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserSummary user;

    @ManyToOne
    @JoinColumn(name = "star_id",nullable = false)
    private StarSummary star;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public LikeSummary(UUID likeId, UserSummary user, StarSummary star, LocalDateTime timestamp) {
        this.likeId = likeId;
        this.user = user;
        this.star = star;
        this.timestamp = timestamp;
    }
}
