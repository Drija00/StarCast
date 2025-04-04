package com.example.Stars.apis.controllers;

import com.example.Stars.DTOs.LikeDTO;
import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.apis.service.NotificationDBService;
import com.example.Stars.apis.service.notification.NotificationService;
import com.example.Stars.queries.read_model.PageResult;
import com.example.Stars.write_model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@Profile("client_notification")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    private final NotificationDBService mNotificationService;

    public NotificationController(NotificationDBService mNotificationService) {
        this.mNotificationService = mNotificationService;
    }

    @GetMapping("/notifications")
    public CompletableFuture<ResponseEntity<PageResult<NotificationDTO>>> getLikes(@RequestParam UUID userId, int offset, int limit){
        return mNotificationService.getNotificatonsFrUser(userId, offset, limit);
    }

    @PutMapping("/notifications/status")
    public void updateStatuses(@RequestParam UUID userId, @RequestBody List<UUID> notifications) throws Exception {
        if (notifications != null || !notifications.isEmpty()) {
            mNotificationService.changeSeenStatus(userId,notifications);
        }
    }

}
