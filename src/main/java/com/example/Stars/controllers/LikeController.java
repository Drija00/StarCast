package com.example.Stars.controllers;

import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.LikeSummary;
import com.example.Stars.service.LikeService;
import com.example.Stars.write_model.Like;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class LikeController {
    private final LikeService likeService;
    private final Like like;

    public LikeController(LikeService likeService, Like like) {
        this.likeService = likeService;
        this.like = like;
    }

    @PostMapping("/like")
    public void handle(@RequestParam UUID userId, @RequestParam UUID starId){
        if(like != null) {
            try {
                likeService.handle(userId,starId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/unlike")
    public void unlike(@RequestParam UUID userId, @RequestParam UUID starId){
        if(userId != null && starId != null) {
            try {
                likeService.unlike(userId, starId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Like not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/likes")
    public CompletableFuture<ResponseEntity<List<LikeSummary>>> getLikes(){
        return likeService.getLikes();
    }

}
