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

    private UserDTO user;

    private StarDTO star;

    private LocalDateTime timestamp;

    public LikeDTO(UUID likeId, UserDTO user, StarDTO star, LocalDateTime timestamp) {
        this.likeId = likeId;
        this.user = user;
        this.star = star;
        this.timestamp = timestamp;
    }
}
