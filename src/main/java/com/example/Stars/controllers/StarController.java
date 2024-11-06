package com.example.Stars.controllers;

import com.example.Stars.api.PostStarCommand;
import com.example.Stars.query.GetStarsQuery;
import com.example.Stars.read_model.StarSummary;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class StarController {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;

    public StarController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/star")
    public void handle(@RequestBody StarSummary star) {
        PostStarCommand cmd = new PostStarCommand(
                UUID.randomUUID(),
                star.getContent(),
                star.getUser().getUserId(),
                LocalDateTime.now()
        );
        commandGateway.sendAndWait(cmd);
    }

    @GetMapping("/stars")
    public CompletableFuture<ResponseEntity<List<StarSummary>>> getStars() {
        return queryGateway.query(new GetStarsQuery(), ResponseTypes.multipleInstancesOf(StarSummary.class))
                .thenApply(starSummaries -> ResponseEntity.ok(starSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
