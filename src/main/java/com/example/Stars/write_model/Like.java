package com.example.Stars.write_model;

import com.example.Stars.apis.api.LikeStarCommand;
import com.example.Stars.apis.api.StarLikedEvent;
import com.example.Stars.apis.api.StarUnlikedEvent;
import com.example.Stars.apis.api.UnlikeStarCommand;
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
@Profile("write_like")
@ProcessingGroup("likeProcessor1")
public class Like {
    @AggregateIdentifier
    private UUID likeId;
    private UUID userId;
    private UUID starId;
    private LocalDateTime timestamp;
    private Boolean active;

    public Like() {
    }

    @CommandHandler
    public Like(LikeStarCommand cmd){
        AggregateLifecycle.apply(
                new StarLikedEvent(
                        cmd.getLikeId(),
                        cmd.getUserId(),
                        cmd.getStarId(),
                        cmd.getTimestamp(),
                        cmd.getActive()
                )
        );
    }

    @CommandHandler
    public void handle(UnlikeStarCommand cmd ){
        AggregateLifecycle.apply(
                new StarUnlikedEvent(
                        cmd.getLikeId(),
                        cmd.getUserId(),
                        cmd.getStarId(),
                        cmd.getTimestamp(),
                        cmd.getActive()
                )
        );
    }

    @EventSourcingHandler
    public void on(StarUnlikedEvent event){
        this.likeId = event.getLikeId();
        this.userId = event.getUserId();
        this.starId = event.getStarId();
        this.timestamp = event.getTimestamp();
        this.active = event.getActive();
    }

    @EventSourcingHandler
    public void on(StarLikedEvent event){
        this.likeId = event.getLikeId();
        this.userId = event.getUserId();
        this.starId = event.getStarId();
        this.timestamp = event.getTimestamp();
        this.active = event.getActive();
    }

}
