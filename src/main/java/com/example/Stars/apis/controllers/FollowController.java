package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.FollowDTO;
import com.example.Stars.queries.read_model.FollowSummary;
import com.example.Stars.apis.service.FollowService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Profile("client_follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public void handle(@RequestParam UUID followerId, @RequestParam String followeeUsername) {
        if(followerId != null && followeeUsername != null) {
            try {
                followService.handle(followerId, followeeUsername);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/unfollow")
    public void unfollow(@RequestParam UUID followerId,@RequestParam String followeeUsername) {
        if(followerId != null && followeeUsername != null) {
            try {
                followService.unfollow(followerId,followeeUsername);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/follows")
    public CompletableFuture<ResponseEntity<List<FollowDTO>>> getFollows(){
        return followService.getFollows();
    }
}
