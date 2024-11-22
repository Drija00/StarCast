package com.example.Stars.service;

import com.example.Stars.api.DeleteStarCommand;
import com.example.Stars.api.PostStarCommand;
import com.example.Stars.api.UpdateStarCommand;
import com.example.Stars.query.*;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
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
    public void deleteStar(UUID userId, UUID starId) throws Exception {
        StarSummary s = queryGateway.query(new GetStarQuery(starId), ResponseTypes.instanceOf(StarSummary.class)).join();
        if(s!=null && s.getUser().getUserId().equals(userId)) {
            DeleteStarCommand cmd = new DeleteStarCommand(
                    s.getStarId(),
                    false
            );
            commandGateway.send(cmd);

        } else {
            throw new Exception("Error while deleting star");
        }
    }

    public void updateStar(UUID userId,StarSummary star) throws Exception {
        StarSummary s = queryGateway.query(new GetStarQuery(star.getStarId()), ResponseTypes.instanceOf(StarSummary.class)).join();

        if(s!=null && s.getUser().getUserId().equals(userId)) {
            UpdateStarCommand cmd = new UpdateStarCommand(
                    s.getStarId(),
                    star.getContent(),
                    userId,
                    LocalDateTime.now()
            );
            commandGateway.send(cmd);

        } else {
            throw new Exception("Error while updating star");
        }
    }

    public CompletableFuture<ResponseEntity<List<StarSummary>>> getStars() {
        return queryGateway.query(new GetStarsQuery(), ResponseTypes.multipleInstancesOf(StarSummary.class))
                .thenApply(starSummaries -> ResponseEntity.ok(starSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
