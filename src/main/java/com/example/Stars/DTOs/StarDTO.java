package com.example.Stars.DTOs;

import com.example.Stars.write_model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class StarDTO {
    private UUID starId;

    private String content;

    private UserDTO user;

    private LocalDateTime timestamp;
    private Set<UserDTO> userLikes;

    public StarDTO(UUID starId) {
        this.starId = starId;
    }

    public StarDTO(UUID starId, String content, UserDTO user, LocalDateTime timestamp, Set<UserDTO> userLikes) {
        this.starId = starId;
        this.content = content;
        this.user = user;
        this.timestamp = timestamp;
        this.userLikes = userLikes;
    }

    @Override
    public String toString() {
        return "StarDTO{" +
                "starId=" + starId +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", userLikes=" + userLikes +
                '}';
    }
}
