package com.example.Stars.apis.service;

import com.example.Stars.DTOs.*;
import com.example.Stars.apis.api.*;
import com.example.Stars.queries.read_model.Notification;
import com.example.Stars.apis.service.notification.NotificationService;
import com.example.Stars.queries.read_model.NotificationStatus;
import com.example.Stars.queries.query.*;
import com.example.Stars.queries.read_model.PageResult;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads";
    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private UserSummaryRepository userSummaryRepository;
    private final NotificationService notificationService;

    public UserService(CommandGateway commandGateway, QueryGateway queryGateway, UserSummaryRepository userSummaryRepository, NotificationService notificationService) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.userSummaryRepository = userSummaryRepository;
        this.notificationService = notificationService;
    }

    public UUID handle(UserPostDTO user) {

        UserDTO u = queryGateway.query(new GetUserForRegistrationQuery(user.getUsername(),user.getEmail()), ResponseTypes.instanceOf(UserDTO.class)).join();

        if (u == null) {

        RegisterUserCommand cmd = new RegisterUserCommand(
                UUID.randomUUID(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                LocalDateTime.now(),
                false,
                user.getFirstName(),
                user.getLastName(),
                ""

        );
        return commandGateway.sendAndWait(cmd);
        } else{
            throw new RuntimeException("User already exists");
        }

    }

    public CompletableFuture<ResponseEntity<?>> follow(UUID followerId, String followeeUsername) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByUsernameQuery(followeeUsername),ResponseTypes.instanceOf(UserDTO.class)).join();
        UserDTO follower = queryGateway.query(new GetUserByIdQuery(followerId),ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());

            UserFollowedCommand cmd = new UserFollowedCommand(
                    followerId,
                    u.getUserId()
            );
            commandGateway.sendAndWait(cmd);

            commandGateway.sendAndWait(cmd);
            MessageCommand cmdMsg = new MessageCommand(
                    UUID.randomUUID(),
                    "User " + follower.getUsername() + "joined your galaxy!",
                    u.getUserId(),
                    LocalDateTime.now(),
                    NotificationStatus.FOLLOW,
                    false
            );
            commandGateway.sendAndWait(cmdMsg);

            notificationService.sendNotification(u.getUserId(),
                    Notification.builder()
                            .status(NotificationStatus.FOLLOW)
                            .message("User " + follower.getUsername() + "joined your galaxy!")
                            .build()
                    );

//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to follow user");
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }
    public CompletableFuture<ResponseEntity<?>> unfollow(UUID followerId, String followeeUsername) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByUsernameQuery(followeeUsername),ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null) {
            System.out.println(u.getUserId());

            UserUnfollowedCommand cmd = new UserUnfollowedCommand(
                    followerId,
                    u.getUserId()
            );
            commandGateway.sendAndWait(cmd);

//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to unfollow user");
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.OK));
    }

    public CompletableFuture<ResponseEntity<?>> setProfileImage(UUID userID, MultipartFile profileImage) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID),ResponseTypes.instanceOf(UserDTO.class)).join();

        String imagePath = "";
        if(u!=null) {
            System.out.println(u.getUserId());


            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            if (profileImage != null && !profileImage.isEmpty()) {
                String imageName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
                File file = new File(uploadDir, imageName);
                try {
                    profileImage.transferTo(file);
                    imagePath = "/uploads/" + imageName;
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            }

            u.setProfileImage(imagePath);

            UserSetProfileImageCommand cmd = new UserSetProfileImageCommand(
                    u.getUserId(),
                    imagePath
            );
            commandGateway.sendAndWait(cmd);

            return CompletableFuture.completedFuture(new ResponseEntity<>(new SetProfileResponse(imagePath), HttpStatus.OK));
//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to set user profile image");
        }
    }
    public CompletableFuture<ResponseEntity<?>> setDescription(UUID userID, String description) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID),ResponseTypes.instanceOf(UserDTO.class)).join();

            if(u!=null) {
            UserSetDescriptionCommand cmd = new UserSetDescriptionCommand(
                    u.getUserId(),
                    description
            );
            commandGateway.sendAndWait(cmd);

            return CompletableFuture.completedFuture(new ResponseEntity<>(new SetDescriptionResponse(description), HttpStatus.OK));
//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to set user description");
        }
    }
    public CompletableFuture<ResponseEntity<?>> setBackgroundImage(UUID userID, MultipartFile backgroundImage) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID),ResponseTypes.instanceOf(UserDTO.class)).join();

        String imagePath = "";
        if(u!=null) {
            System.out.println(u.getUserId());


            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            if (backgroundImage != null && !backgroundImage.isEmpty()) {
                String imageName = System.currentTimeMillis() + "_" + backgroundImage.getOriginalFilename();
                File file = new File(uploadDir, imageName);
                try {
                    backgroundImage.transferTo(file);
                    imagePath = "/uploads/" + imageName;
                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                }
            }


            u.setBackgroundImage(imagePath);

            UserSetBackgroundImageCommand cmd = new UserSetBackgroundImageCommand(
                    u.getUserId(),
                    imagePath
            );
            commandGateway.sendAndWait(cmd);

            return CompletableFuture.completedFuture(new ResponseEntity<>(new SetBackgroundResponse(imagePath),HttpStatus.OK));
//            FollowUserCommand cmd = new FollowUserCommand(
//                    UUID.randomUUID(),
//                    followerId,
//                    u.getUserId(),
//                    LocalDateTime.now(),
//                    true
//            );
//            commandGateway.sendAndWait(cmd);
        }else{
            throw new Exception("Error trying to set user background image");
        }
    }

    public CompletableFuture<ResponseEntity<?>> login(String username, String password) {
        UserDTO u = queryGateway.query(new GetLoginUserQuery(username, password), ResponseTypes.instanceOf(UserDTO.class)).join();
        if (u == null) {
            return CompletableFuture.completedFuture(ResponseEntity.notFound().build());
        }
        if(u.isActive()){
            return CompletableFuture.completedFuture(new ResponseEntity<>(HttpStatus.FORBIDDEN));
        }
        commandGateway.sendAndWait(new LoggingCommand(u.getUserId(), true));
        return queryGateway.query(
                new GetUserByIdQuery(u.getUserId()),
                ResponseTypes.instanceOf(UserDTO.class)
        ).thenApply(updatedUser -> ResponseEntity.ok(updatedUser));
    }

    public CompletableFuture<ResponseEntity<?>> logout(UUID userId) {
        /*return queryGateway.query(new GetLoginUserQuery(username, password), ResponseTypes.instanceOf(UserSummary.class))
                .thenCompose(user -> {
                    if (user != null) {
                        return commandGateway.send(new UserLogingEvent(user.getUserId(), true))
                                .thenApply(success -> ResponseEntity.ok(user));
                    } else {
                        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });*/
        return commandGateway.send(new LoggingCommand(userId, false)).thenApply(success -> ResponseEntity.ok(success));
    }


    public CompletableFuture<ResponseEntity<List<UserDTO>>> getUsers() {
        return queryGateway.query(new GetUsersQuery(), ResponseTypes.multipleInstancesOf(UserDTO.class))
                .thenApply(users -> ResponseEntity.ok(users))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    public CompletableFuture<ResponseEntity<UserDTO>> getUserById(UUID userId) {
        return queryGateway.query(new GetUserByIdQuery(userId), ResponseTypes.instanceOf(UserDTO.class))
                .thenApply(user -> ResponseEntity.ok(user))
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


    public CompletableFuture<ResponseEntity<PageResult<UserDTO>>> getFitleredUsers(String filter, int offset, int limit) {
        return queryGateway.query(new GetFilteredUsers(filter,offset,limit),ResponseTypes.instanceOf(PageResult.class))
                .thenApply(users -> {
                    PageResult<UserDTO> pageResult = (PageResult<UserDTO>) users;
                    return ResponseEntity.ok(pageResult);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
