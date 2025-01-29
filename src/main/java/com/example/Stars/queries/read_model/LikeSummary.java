package com.example.Stars.queries.read_model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "Likes")
@NoArgsConstructor
@Getter
@Setter
public class LikeSummary {
    @Id
    @Column(name = "like_id")
    private UUID likeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserSummary user;

    @ManyToOne
    @JoinColumn(name = "star_id", nullable = false)
    @JsonBackReference
    private StarSummary star;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "active")
    private boolean active;

    public LikeSummary(UUID likeId, UserSummary user, StarSummary star, LocalDateTime timestamp, boolean active) {
        this.likeId = likeId;
        this.user = user;
        this.star = star;
        this.timestamp = timestamp;
        this.active = active;
    }


}
