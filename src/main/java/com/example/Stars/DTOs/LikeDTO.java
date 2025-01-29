package com.example.Stars.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return "LikeDTO{" +
                "likeId=" + likeId +
                ", user=" + user +
                ", star=" + star +
                ", timestamp=" + timestamp +
                ", active=" + active +
                '}';
    }
}
