package com.example.Stars.apis.service.notification;

import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.queries.query.GetNotiicationsForUser;
import com.example.Stars.queries.query.GetUserForYouStarsQuery;
import com.example.Stars.queries.read_model.Notification;
import com.example.Stars.queries.read_model.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(UUID userId, Notification notification) {
        System.out.println("Sending notification to user " + userId + " with notification " + notification.getMessage());
        messagingTemplate.convertAndSendToUser(userId.toString(), "/notification", notification);
    }

}
