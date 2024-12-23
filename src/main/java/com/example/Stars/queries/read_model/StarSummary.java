package com.example.Stars.queries.read_model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "Stars")
@NoArgsConstructor
@Getter
@Setter
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "star_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserSummary> likes = new HashSet<UserSummary>();

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
