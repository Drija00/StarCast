package com.example.Stars.apis.service;

import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.queries.query.GetNotiicationsForUser;
import com.example.Stars.queries.read_model.PageResult;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationDBService {


    private final QueryGateway queryGateway;

    public NotificationDBService(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    public CompletableFuture<ResponseEntity<PageResult<NotificationDTO>>> getNotificatonsFrUser(UUID userId, int offset, int limit) {
        return queryGateway.query(new GetNotiicationsForUser(userId, offset, limit), ResponseTypes.instanceOf(PageResult.class))
                .thenApply(notifications -> {
                    PageResult<NotificationDTO> pageResult = (PageResult<NotificationDTO>) notifications;
                    return ResponseEntity.ok(pageResult);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
