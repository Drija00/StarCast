package com.example.Stars.service;

import com.example.Stars.api.FollowUserCommand;
import com.example.Stars.api.UnfollowUserCommand;
import com.example.Stars.query.*;
import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
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
public class FollowService {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private FollowSummaryRepository followSummaryRepository;
    private UserSummaryRepository userSummaryRepository;

    public FollowService(CommandGateway commandGateway, QueryGateway queryGateway, FollowSummaryRepository followSummaryRepository, com.example.Stars.query.UserSummaryRepository userSummaryRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.followSummaryRepository = followSummaryRepository;
        this.userSummaryRepository = userSummaryRepository;
    }

    public void handle(FollowSummary follow) throws Exception {
        UserSummary u = queryGateway.query(new GetUserByUsernameQuery(follow.getFollowee().getUsername()),ResponseTypes.instanceOf(UserSummary.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());
            FollowUserCommand cmd = new FollowUserCommand(
                    UUID.randomUUID(),
                    follow.getFollower().getUserId(),
                    u.getUserId(),
                    LocalDateTime.now(),
                    true
            );
            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
    }

    public void unfollow(UUID followerId, String followeeUsername) throws Exception {

        UserSummary followee = queryGateway.query(new GetUserByUsernameQuery(followeeUsername),ResponseTypes.instanceOf(UserSummary.class)).join();
        FollowSummary follow = queryGateway.query(new GetFollowQuery(followerId, followee.getUserId()), ResponseTypes.instanceOf(FollowSummary.class)).join();


        if(follow!=null
                && follow.getFollowee().getUserId().equals(followee.getUserId())) {
            //System.out.println(u.getUserId());
            UnfollowUserCommand cmd = new UnfollowUserCommand(
                    follow.getFollowId(),
                    followerId,
                    followee.getUserId(),
                    LocalDateTime.now(),
                    false
            );
            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
    }

    public CompletableFuture<ResponseEntity<List<FollowSummary>>> getFollows(){
        return queryGateway.query(new GetFollowsQuery(), ResponseTypes.multipleInstancesOf(FollowSummary.class))
                .thenApply(followSummaries -> ResponseEntity.ok(followSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
