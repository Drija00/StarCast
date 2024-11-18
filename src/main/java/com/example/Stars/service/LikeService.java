package com.example.Stars.service;

import com.example.Stars.api.LikeStarCommand;
import com.example.Stars.query.*;
import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.LikeSummary;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
import com.example.Stars.write_model.Like;
import com.example.Stars.write_model.Star;
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
public class LikeService {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private LikeRepository likeRepository;
    private UserSummaryRepository userSummaryRepository;
    private StarSummaryRepository starSummaryRepository;

    public LikeService(CommandGateway commandGateway, QueryGateway queryGateway, LikeRepository likeRepository, UserSummaryRepository userSummaryRepository, StarSummaryRepository starSummaryRepository, Like like) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.likeRepository = likeRepository;
        this.userSummaryRepository = userSummaryRepository;
        this.starSummaryRepository = starSummaryRepository;
    }

    public void handle(LikeSummary likeSummary){
        UserSummary u = userSummaryRepository.findByUsername(likeSummary.getUser().getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        StarSummary s = starSummaryRepository.findByStarId(likeSummary.getStar().getStarId()).orElseThrow(() -> new RuntimeException("Star not found"));

        LikeStarCommand cmd = new LikeStarCommand(
                UUID.randomUUID(),
                u.getUserId(),
                s.getStarId(),
                LocalDateTime.now()
        );
        commandGateway.send(cmd);
    }

    public CompletableFuture<ResponseEntity<List<LikeSummary>>> getLikes(){
        return queryGateway.query(new GetLikesQuery(), ResponseTypes.multipleInstancesOf(LikeSummary.class))
                .thenApply(likeSummaries -> ResponseEntity.ok(likeSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
