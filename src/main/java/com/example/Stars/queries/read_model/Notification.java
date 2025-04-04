package com.example.Stars.queries.read_model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "Notifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Notification {
    @Id
    @Column(name = "notification_id")
    private UUID notificationId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserSummary user;
    private String message;
    private boolean seen = false;
    private LocalDateTime timestamp;

}
