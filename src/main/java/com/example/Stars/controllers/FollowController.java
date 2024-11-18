package com.example.Stars.controllers;

import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public void handle(@RequestBody FollowSummary followSummary) {
        if(followSummary != null) {
            try {
                followService.handle(followSummary);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/follows")
    public CompletableFuture<ResponseEntity<List<FollowSummary>>> getFollows(){
        return followService.getFollows();
    }
}
