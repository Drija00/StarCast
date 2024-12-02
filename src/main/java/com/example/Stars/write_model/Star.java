package com.example.Stars.write_model;

import com.example.Stars.api.*;
import com.example.Stars.apis.api.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Aggregate
public class Star {
    @AggregateIdentifier
    private UUID starId;
    private String content;
    private UUID user_id;
    private LocalDateTime timestamp;
    private Boolean active;

    public Star() {
    }

    @CommandHandler
    public Star(PostStarCommand cmd){
        AggregateLifecycle.apply(
                new StarPostedEvent(
                    cmd.getStarId(),
                    cmd.getContent(),
                    cmd.getUserId(),
                    cmd.getTimestamp(),
                    cmd.getActive()
                ));
    }

    @CommandHandler
    public void handle(UpdateStarCommand command){
        AggregateLifecycle.apply(
                new StarUpdatedEvent(
                    command.getStarId(),
                    command.getContent(),
                    new User(command.getUserId()),
                    command.getTimestamp()
                )
        );
    }

    @CommandHandler
    public void handle(DeleteStarCommand command){
        AggregateLifecycle.apply(
                new StarDeletedEvent(
                        command.getStarId(),
                        command.getActive()
                )
        );
    }

    @EventSourcingHandler
    public void on(StarDeletedEvent event){
        this.starId = event.getStarId();
        this.active = event.getActive();
    }

    @EventSourcingHandler
    public void on(StarPostedEvent event){
        this.starId = event.getStarId();
        this.content = event.getContent();
        this.user_id = event.getUserId();
        this.timestamp = event.getTimestamp();
        this.active = event.getActive();
    }

    @EventSourcingHandler
    public void on(StarUpdatedEvent event){
        this.starId = event.getStarId();
        this.content = event.getContent();
        this.user_id = event.getUser().getUser_id();
        this.timestamp = event.getTimestamp();
    }
}
