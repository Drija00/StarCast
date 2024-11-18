package com.example.Stars.write_model;

import com.example.Stars.api.FollowUserCommand;
import com.example.Stars.api.UserFollowedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
public class Follow {
    @AggregateIdentifier
    private UUID followId;
    private UUID followerId;
    private UUID followeeId;
    private LocalDateTime timestamp;

    public Follow() {
    }

    @CommandHandler
    public Follow(FollowUserCommand command) {
        AggregateLifecycle.apply(
                new UserFollowedEvent(
                    command.getFollowId(),
                    command.getFollowerId(),
                    command.getFolloweeId(),
                    command.getTimestamp()
                )
        );
    }

    @EventSourcingHandler
    public void on(UserFollowedEvent event) {
        this.followId = event.getFollowId();
        this.followerId = event.getFollowerId();
        this.followeeId = event.getFolloweeId();
        this.timestamp = event.getTimestamp();
    }

}
