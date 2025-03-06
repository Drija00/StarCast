package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.*;
import com.example.Stars.apis.service.UserService;
import com.example.Stars.queries.read_model.PageResult;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Profile("client_user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public UUID handle(@RequestBody UserPostDTO user) {
        if(user != null) {
            return userService.handle(user);
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
    @PatchMapping(value = "/user/profileimage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<?>> setProfileImage(
            @RequestPart(name = "user") UUID userId,
            @RequestPart(value = "image") MultipartFile image) {
            try {
                return userService.setProfileImage(userId,image);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

    }
    @PatchMapping(value = "/user/description")
    public CompletableFuture<ResponseEntity<?>> setDescription(
            @RequestParam UUID userId,
            @RequestParam String description) {
            try {
                return userService.setDescription(userId,description);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

    }

    @PatchMapping(value = "/user/backgroundimage",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<?>> setBackgroundImage(
            @RequestPart(name = "user") UUID userId,
            @RequestPart(value = "image") MultipartFile image) {
        try {
            return userService.setBackgroundImage(userId,image);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

    }

    @PutMapping("/user/follow")
    public CompletableFuture<ResponseEntity<?>> follow(@RequestParam UUID followerId, @RequestParam String followeeUsername) {
        if(followerId != null && followeeUsername != null) {
            try {
                return userService.follow(followerId, followeeUsername);


            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/unfollow")
    public CompletableFuture<ResponseEntity<?>> unfollow(@RequestParam UUID followerId, @RequestParam String followeeUsername) {
        if(followerId != null && followeeUsername != null) {
            try {
                return userService.unfollow(followerId, followeeUsername);


            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public CompletableFuture<ResponseEntity<List<UserDTO>>> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user")
    public CompletableFuture<ResponseEntity<UserDTO>> getUserById(@RequestParam UUID userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/users/filter")
    public CompletableFuture<ResponseEntity<PageResult<UserDTO>>> getUsers(@RequestParam String filter, int offset, int limit) {
        return userService.getFitleredUsers(filter,offset,limit);
    }

}
