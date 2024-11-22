package com.example.Stars.controllers;

import com.example.Stars.api.PostStarCommand;
import com.example.Stars.query.GetStarsQuery;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.service.StarService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
public class StarController {

    private final StarService starService;

    public StarController(StarService starService) {
        this.starService = starService;
    }

    @PostMapping("/star")
    public void handle(@RequestBody StarSummary star) {
        if(star != null) {
            try {
                starService.handle(star);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stars")
    public CompletableFuture<ResponseEntity<List<StarSummary>>> getStars() {
        return starService.getStars();
    }

    @PutMapping("/star")
    public void handle(@RequestParam UUID userId, @RequestBody StarSummary star) {
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
