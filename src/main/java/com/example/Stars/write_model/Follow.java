package com.example.Stars.write_model;

import com.example.Stars.apis.api.FollowUserCommand;
import com.example.Stars.apis.api.UnfollowUserCommand;
import com.example.Stars.apis.api.UserFollowedEvent;
import com.example.Stars.apis.api.UserUnfollowedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
@Profile("write")
public class Follow {
    @AggregateIdentifier
    private UUID followId;
    private UUID followerId;
    private UUID followeeId;
    private LocalDateTime timestamp;
    private boolean active;

    public Follow() {
    }

    @CommandHandler
    public Follow(FollowUserCommand command) {
        AggregateLifecycle.apply(
                new UserFollowedEvent(
                    command.getFollowId(),
                    command.getFollowerId(),
                    command.getFolloweeId(),
                    command.getTimestamp(),
                    command.getActive()
                )
        );
    }

    @CommandHandler
    public void handle(UnfollowUserCommand cmd) {
        AggregateLifecycle.apply(
                new UserUnfollowedEvent(
                        cmd.getFollowId(),
                        cmd.getFollowerId(),
                        cmd.getFolloweeId(),
                        cmd.getTimestamp(),
                        cmd.getActive()
                )
        );
    }

    @EventSourcingHandler
    public void on(UserUnfollowedEvent event) {
        this.followId = event.getFollowId();
        this.followerId = event.getFollowerId();
        this.followeeId = event.getFolloweeId();
        this.timestamp = event.getTimestamp();
        this.active = event.getActive();
    }

    @EventSourcingHandler
    public void on(UserFollowedEvent event) {
        this.followId = event.getFollowId();
        this.followerId = event.getFollowerId();
        this.followeeId = event.getFolloweeId();
        this.timestamp = event.getTimestamp();
        this.active = event.getActive();
    }

}
