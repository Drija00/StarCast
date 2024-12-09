package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.queries.read_model.UserSummary;
import com.example.Stars.apis.service.UserService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Profile("client")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public void handle(@RequestBody UserDTO user) {
        if(user != null) {
            userService.handle(user);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/login")
    public CompletableFuture<ResponseEntity<?>> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }
    @PutMapping("/user/logout")
    public CompletableFuture<ResponseEntity<?>> logout(@RequestParam UUID user_id) {
        return userService.logout(user_id);
    }

    @GetMapping("/users")
    public CompletableFuture<ResponseEntity<List<UserDTO>>> getUsers() {
        return userService.getUsers();
    }

}
