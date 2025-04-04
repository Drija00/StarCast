package com.example.Stars.DTOs;

import com.example.Stars.queries.read_model.NotificationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Name;
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
    @JsonProperty("notificationId")
    private UUID notificationID;
    @JsonProperty("message")
    private String content;
    private LocalDateTime timestamp;
    private NotificationStatus status;
    private UserFollowDTO user;
    private boolean seen;

    public NotificationDTO(UUID notificationID, String content, LocalDateTime timestamp, NotificationStatus status, UserFollowDTO user, boolean seen) {
        this.notificationID = notificationID;
        this.content = content;
        this.timestamp = timestamp;
        this.status = status;
        this.user = user;
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "notificationID=" + notificationID +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                ", user=" + user +
                ", seen=" + seen +
                '}';
    }
}
