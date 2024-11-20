package com.example.Stars.service;

import com.example.Stars.api.PostStarCommand;
import com.example.Stars.query.*;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class StarService {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private StarSummaryRepository summaryRepository;
    private UserSummaryRepository userSummaryRepository;

    public StarService(CommandGateway commandGateway, QueryGateway queryGateway, StarSummaryRepository summaryRepository, UserSummaryRepository userSummaryRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.summaryRepository = summaryRepository;
        this.userSummaryRepository = userSummaryRepository;
    }

    public void handle(StarSummary star) throws Exception {
        UserSummary u = queryGateway.query(new GetUserByUsernameQuery(star.getUser().getUsername()), ResponseTypes.instanceOf(UserSummary.class)).join();

        if(u!=null) {
            PostStarCommand cmd = new PostStarCommand(
                    UUID.randomUUID(),
                    star.getContent(),
                    u.getUserId(),
                    LocalDateTime.now(),
                    true
            );
            commandGateway.send(cmd);
        } else {
          throw new Exception("Error while posting star");
        }

    }

    public CompletableFuture<ResponseEntity<List<StarSummary>>> getStars() {
        return queryGateway.query(new GetStarsQuery(), ResponseTypes.multipleInstancesOf(StarSummary.class))
                .thenApply(starSummaries -> ResponseEntity.ok(starSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
