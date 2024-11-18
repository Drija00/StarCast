package com.example.Stars.service;

import com.example.Stars.api.RegisterUserCommand;
import com.example.Stars.query.GetLoginUserQuery;
import com.example.Stars.query.GetUserForRegistrationQuery;
import com.example.Stars.query.GetUsersQuery;
import com.example.Stars.query.UserSummaryRepository;
import com.example.Stars.read_model.UserSummary;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

    public void handle(UserSummary user) {

        UserSummary u = queryGateway.query(new GetUserForRegistrationQuery(user.getUsername(),user.getEmail()), ResponseTypes.instanceOf(UserSummary.class)).join();

        if (u == null) {

        RegisterUserCommand cmd = new RegisterUserCommand(
                UUID.randomUUID(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                false
        );
        commandGateway.sendAndWait(cmd);
        } else{
            throw new RuntimeException("User already exists");
        }

    }

    public CompletableFuture<ResponseEntity<UserSummary>> login(String username, String password) {
        return queryGateway.query(new GetLoginUserQuery(username, password), ResponseTypes.instanceOf(UserSummary.class))
                .thenApply(u -> ResponseEntity.ok(u)).exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public CompletableFuture<ResponseEntity<List<UserSummary>>> getUsers() {
        return queryGateway.query(new GetUsersQuery(), ResponseTypes.multipleInstancesOf(UserSummary.class))
                .thenApply(userSummaries -> ResponseEntity.ok(userSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
