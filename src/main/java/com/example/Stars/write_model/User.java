package com.example.Stars.write_model;

import com.example.Stars.api.*;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;

import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Aggregate
@Getter
@Setter
public class User {

    @AggregateIdentifier
    private UUID user_id;
    private String username;
    private String email;
    private String password;
    private boolean active;

    public User() {}

    public User(UUID user_id) {
        this.user_id = user_id;
    }

    @CommandHandler
    public User(RegisterUserCommand command) {
        AggregateLifecycle.apply(
                new UserRegisteredEvent(
                        command.getUserId(),
                        command.getUsername(),
                        command.getEmail(),
                        command.getPassword(),
                        command.getActive()
                ));
    }

    @CommandHandler
    public void handle(LoggingCommand cmd) {
        AggregateLifecycle.apply(
          new UserLogingEvent(
                  cmd.getUserId(),
                  cmd.getActive()
          )
        );
    }

    @EventSourcingHandler public void on(@NotNull UserRegisteredEvent event) {
        this.user_id = event.getUserId();
        this.username = event.getUsername();
        this.email = event.getEmail();
        this.password = event.getPassword();
        this.active = event.getActive();
    }

    @EventSourcingHandler public void on(@NotNull UserLogingEvent event) {
        this.active = event.getActive();
    }
}
