package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.NotificationDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter implements DtoEntityConverter<NotificationDTO, Notification> {

    @Autowired
    private UserFollowConverter userConverter;

    @Override
    public NotificationDTO toDto(Notification notification) {
        return new NotificationDTO(notification.getNotificationId(),notification.getMessage(),notification.getTimestamp(),notification.getStatus(),userConverter.toDto(notification.getUser()),notification.isSeen());
    }

    @Override
    public Notification toEntity(NotificationDTO notificationDTO) {
        return new Notification(notificationDTO.getNotificationID(),notificationDTO.getStatus(),userConverter.toEntity(notificationDTO.getUser()), notificationDTO.getContent(), notificationDTO.isSeen(), notificationDTO.getTimestamp());
    }
}
