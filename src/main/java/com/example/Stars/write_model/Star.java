package com.example.Stars.write_model;

import com.example.Stars.api.PostStarCommand;
import com.example.Stars.api.StarPostedEvent;
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

    public Star() {
    }

    @CommandHandler
    public Star(PostStarCommand cmd){
        AggregateLifecycle.apply(
                new StarPostedEvent(
                    cmd.getStarId(),
                    cmd.getContent(),
                    cmd.getUserId(),
                    cmd.getTimestamp()
                ));
    }

    @EventSourcingHandler
    public void on(StarPostedEvent event){
        this.starId = event.getStarId();
        this.content = event.getContent();
        this.user_id = event.getUserId();
        this.timestamp = event.getTimestamp();
    }
}
