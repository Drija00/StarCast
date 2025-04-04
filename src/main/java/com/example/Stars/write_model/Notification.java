package com.example.Stars.write_model;

import com.example.Stars.apis.api.*;
import com.example.Stars.queries.read_model.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
@Profile("write_notification")
@ProcessingGroup("notificationProcessor1")
@Getter
public class Notification {
    @AggregateIdentifier
    private UUID notificationId;
    private UUID userId;
    private NotificationStatus status;
    private String message;
    private LocalDateTime timestamp;
    private boolean seen;

    public Notification() {
    }

    @CommandHandler
    public Notification(MessageCommand cmd){
        AggregateLifecycle.apply(
                new MessageEvent(
                        cmd.getMessageId(),
                        cmd.getContent(),
                        cmd.getUserId(),
                        cmd.getTimestamp(),
                        cmd.getStatus(),
                        cmd.getSeen()
                )
        );
    }

    @EventSourcingHandler
    public void on(MessageEvent event) {
        this.notificationId = event.getMessageId();
        this.message = event.getContent();
        this.userId = event.getUserId();
        this.status = event.getStatus();
        this.timestamp = event.getTimestamp();
        this.seen = event.getSeen();
    }

    @CommandHandler
    public void handle(MessageStatusChangeCommand cmd) {
        if (!seen) {
            AggregateLifecycle.apply(new MessageStatusChangeEvent(cmd.getMessageId()));
        }
    }

    @EventSourcingHandler
    public void on(MessageStatusChangeEvent event) {
        this.seen = true;
    }

}
