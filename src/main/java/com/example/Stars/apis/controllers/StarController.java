package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.apis.service.StarService;
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
public class StarController {

    private final StarService starService;

    public StarController(StarService starService) {
        this.starService = starService;
    }

    @PostMapping("/star")
    public void handle(@RequestParam UUID userID, @RequestBody String content) {
        if(userID != null && content != null) {
            try {
                starService.handle(userID, content);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stars")
    public CompletableFuture<ResponseEntity<List<StarDTO>>> getStars() {
        return starService.getStars();
    }

    @GetMapping("/user/stars")
    public CompletableFuture<ResponseEntity<List<StarDTO>>> getUserStars(@RequestParam UUID userId) {
        return starService.getUserStars(userId);
    }
    @GetMapping("/user/stars/foryou")
    public CompletableFuture<ResponseEntity<List<StarDTO>>> getUserForYouStars(@RequestParam UUID userId) {
        return starService.getUserForYouStars(userId);
    }

    @PutMapping("/star")
    public void handle(@RequestParam UUID userId, @RequestBody StarDTO star) {
        if(star != null && userId != null) {
            try {
                starService.updateStar(userId, star);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Star not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/star")
    public void delete(@RequestParam UUID userId, @RequestParam UUID starId) {
        if(starId != null && userId != null) {
            try {
                starService.deleteStar(userId, starId);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Star not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
