package com.example.Stars.DTOs;

import com.example.Stars.queries.read_model.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {
    private UUID notificationID;
    private String content;
    private LocalDateTime timestamp;
    private NotificationStatus status;
    private UserFollowDTO user;

    public NotificationDTO(UUID notificationID, String content, LocalDateTime timestamp, NotificationStatus status, UserFollowDTO user) {
        this.notificationID = notificationID;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
        this.user = user;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "notificationID=" + notificationID +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}
