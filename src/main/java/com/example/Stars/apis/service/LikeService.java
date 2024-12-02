package com.example.Stars.apis.service;

import com.example.Stars.apis.api.LikeStarCommand;
import com.example.Stars.apis.api.UnlikeStarCommand;
import com.example.Stars.queries.query.*;
import com.example.Stars.queries.read_model.LikeSummary;
import com.example.Stars.queries.read_model.StarSummary;
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

    public LikeService(CommandGateway commandGateway, QueryGateway queryGateway, LikeRepository likeRepository, UserSummaryRepository userSummaryRepository, StarSummaryRepository starSummaryRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.likeRepository = likeRepository;
        this.userSummaryRepository = userSummaryRepository;
        this.starSummaryRepository = starSummaryRepository;
    }

    public void handle(UUID userId, UUID starId) throws Exception {
        //UserSummary u = queryGateway.query(new GetUserQuery(likeSummary.getUser().getUsername()), ResponseTypes.instanceOf(UserSummary.class)).join();
        StarSummary s = queryGateway.query(new GetStarQuery(starId), ResponseTypes.instanceOf(StarSummary.class)).join();

        if(s!=null){
            LikeStarCommand cmd = new LikeStarCommand(
                    UUID.randomUUID(),
                    userId,
                    s.getStarId(),
                    LocalDateTime.now(),
                    true
            );
            commandGateway.send(cmd);
        }else{
            throw new Exception("Error trying to like a star");
        }
    }

    public void unlike(UUID userId, UUID starId) throws Exception {
        StarSummary s = queryGateway.query(new GetStarQuery(starId), ResponseTypes.instanceOf(StarSummary.class)).join();
        LikeSummary l = queryGateway.query(new GetLikeQuery(userId,starId), ResponseTypes.instanceOf(LikeSummary.class)).join();

        if(l!=null && s!=null && l.getStar().getStarId().equals(s.getStarId()) && l.getUser().getUserId().equals(userId)){
            UnlikeStarCommand cmd = new UnlikeStarCommand(
                    l.getLikeId(),
                    l.getUser().getUserId(),
                    l.getStar().getStarId(),
                    LocalDateTime.now(),
                    false
            );
            commandGateway.send(cmd);
        }else{
            throw new Exception("Error trying to like a star");
        }
    }

    public CompletableFuture<ResponseEntity<List<LikeSummary>>> getLikes(){
        return queryGateway.query(new GetLikesQuery(), ResponseTypes.multipleInstancesOf(LikeSummary.class))
                .thenApply(likeSummaries -> ResponseEntity.ok(likeSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public CompletableFuture<ResponseEntity<List<LikeSummary>>> getStarLikes(UUID starid){
        return queryGateway.query(new GetStarLikesQuery(starid), ResponseTypes.multipleInstancesOf(LikeSummary.class))
                .thenApply(likeSummaries -> ResponseEntity.ok(likeSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
