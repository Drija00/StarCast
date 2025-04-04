package com.example.Stars.apis.service;

import com.example.Stars.DTOs.LikeDTO;
import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.apis.api.MarkNotificationsSeenCommand;
import com.example.Stars.apis.api.MessageStatusChangeCommand;
import com.example.Stars.apis.api.UnlikeStarCommand;
import com.example.Stars.converter.impl.NotificationConverter;
import com.example.Stars.queries.query.GetLikeQuery;
import com.example.Stars.queries.query.GetNotiicationsForUser;
import com.example.Stars.queries.query.GetStarQuery;
import com.example.Stars.queries.query.GetUserByIdQuery;
import com.example.Stars.queries.read_model.Notification;
import com.example.Stars.queries.read_model.PageResult;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NotificationDBService {


    private final QueryGateway queryGateway;
    private final CommandGateway commandGateway;
    private final NotificationConverter notificationConverter;

    public NotificationDBService(QueryGateway queryGateway, CommandGateway commandGateway, NotificationConverter notificationConverter) {
        this.queryGateway = queryGateway;
        this.commandGateway = commandGateway;
        this.notificationConverter = notificationConverter;
    }

    public CompletableFuture<ResponseEntity<PageResult<NotificationDTO>>> getNotificatonsFrUser(UUID userId, int offset, int limit) {
        return queryGateway.query(new GetNotiicationsForUser(userId, offset, limit), ResponseTypes.instanceOf(PageResult.class))
                .thenApply(notifications -> {
                    PageResult<NotificationDTO> pageResult = (PageResult<NotificationDTO>) notifications;
                    return ResponseEntity.ok(pageResult);
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    public void changeSeenStatus(UUID userID,List<UUID> notifications) throws Exception {
        UserDTO u = queryGateway.query(new GetUserByIdQuery(userID), ResponseTypes.instanceOf(UserDTO.class)).join();

        if(u!=null){

            MarkNotificationsSeenCommand cmd = new MarkNotificationsSeenCommand(u.getUserId(),notifications);
            commandGateway.sendAndWait(cmd);

        }else{
            throw new Exception("Error trying to change notifications statuses");
        }
    }
}
