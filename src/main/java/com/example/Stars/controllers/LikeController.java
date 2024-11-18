package com.example.Stars.controllers;

import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.LikeSummary;
import com.example.Stars.service.LikeService;
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
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/like")
    public void handle(@RequestBody LikeSummary like){
        if(like != null) {
            try {
                likeService.handle(like);
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
