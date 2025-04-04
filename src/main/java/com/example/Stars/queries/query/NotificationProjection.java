package com.example.Stars.queries.query;

import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.apis.api.MessageEvent;
import com.example.Stars.apis.api.MessageStatusChangeEvent;
import com.example.Stars.apis.api.NotificationsSeenEvent;
import com.example.Stars.converter.impl.NotificationConverter;
import com.example.Stars.queries.read_model.Notification;
import com.example.Stars.queries.read_model.PageResult;
import com.example.Stars.queries.read_model.UserSummary;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile("query_notification")
@ProcessingGroup("notificationProcessor")
public class NotificationProjection {

    private final NotificationRepository notificationRepository;
    private final UserSummaryRepository userSummaryRepository;
    private final NotificationConverter notificationConverter;

    public NotificationProjection(NotificationRepository notificationRepository, UserSummaryRepository userSummaryRepository, NotificationConverter notificationConverter) {
        this.notificationRepository = notificationRepository;
        this.userSummaryRepository = userSummaryRepository;
        this.notificationConverter = notificationConverter;
    }

    @EventHandler
    public void handle(MessageEvent event) {
        Optional<UserSummary> user = userSummaryRepository.findById(event.getUserId());

        if (user.isEmpty()) {
            System.err.println("User not found for event " );
            return;
        }
        Notification notification = new Notification(
            event.getMessageId(),
            event.getStatus(),
            new UserSummary(event.getUserId()),
            event.getContent(),
            event.getSeen(),
            event.getTimestamp()
        );

        notificationRepository.save(notification);
    }

    @EventHandler
    public void handle(NotificationsSeenEvent evt){

        for (UUID id : evt.getNotificationIds()) {
            Notification notification = notificationRepository.findById(id).orElseThrow(()->new RuntimeException("Notification not found"));

            notification.setSeen(true);

            notificationRepository.save(notification);
        }
    }

    @QueryHandler
    public PageResult on(GetNotiicationsForUser gry){
        try {
            Pageable pageable = PageRequest.of(gry.getPageNumber(), gry.getPageSize());
            LocalDateTime date = LocalDateTime.now().minusDays(7);
            Page<Notification> items = notificationRepository.findRecentNotifications(new UserSummary(gry.getUser_id()),date,pageable);
            List<NotificationDTO> itemsDtos = items.getContent().stream().map(notificationConverter::toDto).collect(Collectors.toList());

            return new PageResult<>(itemsDtos, items.getTotalElements());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch notifications", e);
        }
    }
}
