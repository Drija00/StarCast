package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.DTOs.StarPostDTO;
import com.example.Stars.queries.read_model.PageResult;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.apis.service.StarService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Profile("client_star")
@CrossOrigin(origins = "http://localhost:4200")
public class StarController {

    private final StarService starService;

    public StarController(StarService starService) {
        this.starService = starService;
    }

    @PostMapping("/star")
    public void handle(@RequestBody StarPostDTO starPostDTO) {
        if(starPostDTO.getUser_id() != null && starPostDTO.getContent() != null) {
            try {
                starService.handle(starPostDTO);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/stars")
    public CompletableFuture<ResponseEntity<PageResult<StarDTO>>> getStars(int offset, int limit) {
        return starService.getStars(offset, limit);
    }

    @GetMapping("/user/stars")
    public CompletableFuture<ResponseEntity<PageResult<StarDTO>>> getUserStars(@RequestParam UUID userId, int offset, int limit) {
        return starService.getUserStars(userId, offset, limit);
    }
    @GetMapping("/user/stars/foryou")
    public CompletableFuture<ResponseEntity<PageResult<StarDTO>>> getUserForYouStars(@RequestParam UUID userId, int offset, int limit) {
        return starService.getUserForYouStars(userId, offset, limit);
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
