package com.example.Stars.controllers;

import com.example.Stars.api.RegisterUserCommand;
import com.example.Stars.query.GetUsersQuery;
import com.example.Stars.read_model.UserSummary;
import com.example.Stars.service.UserService;
import com.example.Stars.write_model.User;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void handle(@RequestBody UserSummary user) {
        if(user != null) {
            userService.handle(user);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login")
    public CompletableFuture<ResponseEntity<UserSummary>> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    @GetMapping("/users")
    public CompletableFuture<ResponseEntity<List<UserSummary>>> getUsers() {
        return userService.getUsers();
    }

}
