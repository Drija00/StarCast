package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class LikeDTO {
    private UUID likeId;

    private UserFollowDTO user;

    private StarDTO star;

    private LocalDateTime timestamp;
    private boolean active;

    public LikeDTO(UUID likeId, UserFollowDTO user, StarDTO star, LocalDateTime timestamp, boolean active) {
        this.likeId = likeId;
        this.user = user;
        this.star = star;
        this.timestamp = timestamp;
        this.active = active;
    }
}
