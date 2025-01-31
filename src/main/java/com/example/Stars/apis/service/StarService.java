package com.example.Stars.apis.service;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.DTOs.StarPostDTO;
import com.example.Stars.apis.api.DeleteStarCommand;
import com.example.Stars.apis.api.PostStarCommand;
import com.example.Stars.apis.api.UpdateStarCommand;
import com.example.Stars.queries.query.*;
import com.example.Stars.queries.read_model.PageResult;
import com.example.Stars.queries.read_model.StarSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.Pageable;
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

    public void handle(StarPostDTO starPostDTO) throws Exception {
        //UserSummary u = queryGateway.query(new GetUserByUsernameQuery(star.getUser().getUsername()), ResponseTypes.instanceOf(UserSummary.class)).join();
            try{
            PostStarCommand cmd = new PostStarCommand(
                    UUID.randomUUID(),
                    starPostDTO.getContent(),
                    starPostDTO.getUser_id(),
                    LocalDateTime.now(),
                    true,
                    starPostDTO.getImages()
            );
            commandGateway.send(cmd);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
    }
    public void deleteStar(UUID userId, UUID starId) throws Exception {
        StarDTO s = queryGateway.query(new GetStarQuery(starId), ResponseTypes.instanceOf(StarDTO.class)).join();
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

    public void updateStar(UUID userId,StarDTO star) throws Exception {
        StarDTO s = queryGateway.query(new GetStarQuery(star.getStarId()), ResponseTypes.instanceOf(StarDTO.class)).join();

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

    public CompletableFuture<ResponseEntity<PageResult<StarDTO>>> getStars(int offset, int limit) {
        return queryGateway.query(
                        new GetStarsQuery(offset, limit),
                        ResponseTypes.instanceOf(PageResult.class))
                .thenApply(result -> {
                    PageResult<StarDTO> pageResult = (PageResult<StarDTO>) result;
                    return ResponseEntity.ok(pageResult);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }



    public CompletableFuture<ResponseEntity<List<StarDTO>>> getUserStars(UUID userId) {
        return queryGateway.query(new GetUserStarsQuery(userId), ResponseTypes.multipleInstancesOf(StarDTO.class))
                .thenApply(stars -> ResponseEntity.ok(stars))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    public CompletableFuture<ResponseEntity<List<StarDTO>>> getUserForYouStars(UUID userId) {
        return queryGateway.query(new GetUserForYouStarsQuery(userId), ResponseTypes.multipleInstancesOf(StarDTO.class))
                .thenApply(stars -> ResponseEntity.ok(stars))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
