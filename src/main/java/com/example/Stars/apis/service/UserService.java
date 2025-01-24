package com.example.Stars.apis.service;

import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.DTOs.UserFollowDTO;
import com.example.Stars.DTOs.UserPostDTO;
import com.example.Stars.apis.api.*;
import com.example.Stars.queries.query.*;
import com.example.Stars.queries.read_model.UserSummary;
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
public class UserService {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private UserSummaryRepository userSummaryRepository;

    public UserService(CommandGateway commandGateway, QueryGateway queryGateway, UserSummaryRepository userSummaryRepository) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.userSummaryRepository = userSummaryRepository;
    }

    public void handle(UserPostDTO user) {

        UserDTO u = queryGateway.query(new GetUserForRegistrationQuery(user.getUsername(),user.getEmail()), ResponseTypes.instanceOf(UserDTO.class)).join();

        if (u == null) {

        RegisterUserCommand cmd = new RegisterUserCommand(
                UUID.randomUUID(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                LocalDateTime.now(),
                false,
                user.getFirstName(),
                user.getLastName()
        );
        commandGateway.sendAndWait(cmd);
        } else{
            throw new RuntimeException("User already exists");
        }

    }

    public CompletableFuture<ResponseEntity<?>> follow(UUID followerId, String followeeUsername) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByUsernameQuery(followeeUsername),ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());

            UserFollowedCommand cmd = new UserFollowedCommand(
                    followerId,
                    u.getUserId()
            );
            commandGateway.sendAndWait(cmd);

//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    public CompletableFuture<ResponseEntity<?>> setProfileImage(UUID userID, String profileImage) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID),ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());

            UserSetProfileImageCommand cmd = new UserSetProfileImageCommand(
                    u.getUserId(),
                    profileImage
            );
            commandGateway.sendAndWait(cmd);

//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
    public CompletableFuture<ResponseEntity<?>> setBackgroundImage(UUID userID, String backgroundImage) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID),ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());

            UserSetBackgroundImageCommand cmd = new UserSetBackgroundImageCommand(
                    u.getUserId(),
                    backgroundImage
            );
            commandGateway.sendAndWait(cmd);

//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    public CompletableFuture<ResponseEntity<?>> login(String username, String password) {
        UserDTO u = queryGateway.query(new GetLoginUserQuery(username, password), ResponseTypes.instanceOf(UserDTO.class)).join();
        if (u != null) {
            return commandGateway.send(new LoggingCommand(u.getUserId(), true))
                    .thenCompose(result -> queryGateway.query(
                            new GetUserByIdQuery(u.getUserId()),
                            ResponseTypes.instanceOf(UserDTO.class)
                    ))
                    .thenApply(updatedUser -> ResponseEntity.ok(updatedUser));
        }
        return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
    }

    public CompletableFuture<ResponseEntity<?>> logout(UUID userId) {
        /*return queryGateway.query(new GetLoginUserQuery(username, password), ResponseTypes.instanceOf(UserSummary.class))
                .thenCompose(user -> {
                    if (user != null) {
                        return commandGateway.send(new UserLogingEvent(user.getUserId(), true))
                                .thenApply(success -> ResponseEntity.ok(user));
                    } else {
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });*/
        return commandGateway.send(new LoggingCommand(userId, false)).thenApply(success -> ResponseEntity.ok(success));
    }


    public CompletableFuture<ResponseEntity<List<UserDTO>>> getUsers() {
        return queryGateway.query(new GetUsersQuery(), ResponseTypes.multipleInstancesOf(UserDTO.class))
                .thenApply(users -> ResponseEntity.ok(users))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
