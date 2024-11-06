package com.example.Stars.controllers;

import com.example.Stars.api.RegisterUserCommand;
import com.example.Stars.query.GetUsersQuery;
import com.example.Stars.read_model.UserSummary;
import com.example.Stars.write_model.User;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;

    public UserController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/user")
    public void handle(@RequestBody UserSummary user) {
        RegisterUserCommand cmd = new RegisterUserCommand(
                UUID.randomUUID(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                false
        );
        commandGateway.sendAndWait(cmd);
    }

    @GetMapping("/users")
    public CompletableFuture<ResponseEntity<List<UserSummary>>> getUsers() {
        return queryGateway.query(new GetUsersQuery(), ResponseTypes.multipleInstancesOf(UserSummary.class))
                .thenApply(userSummaries -> ResponseEntity.ok(userSummaries))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

}
