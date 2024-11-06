package com.example.Stars.read_model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.Name;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "Stars")
@NoArgsConstructor
public class StarSummary {
    @Id
    @Column(name = "star_id")
    private UUID starId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserSummary user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public StarSummary(UUID starId) {
        this.starId = starId;
    }

    public StarSummary(UUID starId, String content, UserSummary user, LocalDateTime timestamp) {
        this.starId = starId;
        this.content = content;
        this.user = user;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StarSummary{" +
                "starId=" + starId +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", timestamp=" + timestamp +
                '}';
    }
}
