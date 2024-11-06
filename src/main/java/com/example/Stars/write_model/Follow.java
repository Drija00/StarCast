package com.example.Stars.write_model;

import com.example.Stars.api.FollowUserCommand;
import com.example.Stars.api.UserFollowedEvent;
import org.axonframework.commandhandling.CommandHandler;
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

}
