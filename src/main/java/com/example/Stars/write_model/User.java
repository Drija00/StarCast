package com.example.Stars.write_model;

import com.example.Stars.apis.api.*;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;

import org.axonframework.spring.stereotype.Aggregate;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.*;

@Aggregate
@Profile("write_user")
@ProcessingGroup("userProcessor2")
@Getter
@Setter
public class User {

    @AggregateIdentifier
    private UUID user_id;
    private String username;
    private String email;
    private String password;
    private boolean active;
    private String firstName;
    private String lastName;
    private LocalDateTime joinDate;
    private String description;
    private String profileImage;
    private String backgroundImage;

    private Set<UUID> following = new HashSet<>();
    private List<Notification> notifications = new ArrayList<>();
    //private Set<UUID> followers = new HashSet<>();

    public User() {
    }

    public User(UUID user_id) {
        this.user_id = user_id;
    }

    @CommandHandler
    public User(RegisterUserCommand command) {
        AggregateLifecycle.apply(
                new UserRegisteredEvent(
                        command.getUserId(),
                        command.getUsername(),
                        command.getEmail(),
                        command.getPassword(),
                        command.getJoinDate(),
                        command.getActive(),
                        command.getFirstname(),
                        command.getLastname(),
                        command.getDescription()
                ));
    }

    @CommandHandler
    public void handle(LoggingCommand cmd) {
        AggregateLifecycle.apply(
                new UserLogingEvent(
                        cmd.getUserId(),
                        cmd.getActive()
                )
        );
    }

    @CommandHandler
    public void handle(UserSetProfileImageCommand cmd) {
        AggregateLifecycle.apply(
                new UserSetProfileImageEvent(
                        cmd.getUserId(),
                        cmd.getProfileImageUrl()
                )
        );
    }

    @CommandHandler
    public void handle(UserSetDescriptionCommand cmd) {
        AggregateLifecycle.apply(
                new UserSetDescriptionEvent(
                        cmd.getUserId(),
                        cmd.getDescription()
                )
        );
    }

    @CommandHandler
    public void handle(UserSetBackgroundImageCommand cmd) {
        AggregateLifecycle.apply(
                new UserSetBackgroundImageEvent(
                        cmd.getUserId(),
                        cmd.getBackgroundImage()
                )
        );
    }

    @EventSourcingHandler
    public User on(@NotNull UserRegisteredEvent event) {

        System.out.println("Event stored: " + event);
        this.user_id = event.getUserId();
        this.username = event.getUsername();
        this.email = event.getEmail();
        this.password = event.getPassword();
        this.active = event.getActive();

        return this;
    }

    @EventSourcingHandler
    public void on(@NotNull UserLogingEvent event) {
        this.active = event.getActive();
    }

    @CommandHandler
    public void handle(UserFollowedCommand command) {
        if (!this.following.contains(command.getFolloweeId())) {
            AggregateLifecycle.apply(new UserUserFollowedEvent(
                    command.getFollowerId(),
                    command.getFolloweeId()
            ));
        }
    }

    @EventSourcingHandler
    public void on(@NotNull UserUserFollowedEvent event) {
        this.user_id = event.getFollowerId();
        this.following.add(event.getFolloweeId());
    }

    @EventSourcingHandler
    public void on(@NotNull UserSetBackgroundImageEvent event) {
        this.user_id = event.getUserId();
        this.backgroundImage = event.getBackgroundImage();
    }

    @EventSourcingHandler
    public void on(@NotNull UserSetProfileImageEvent event) {
        this.user_id = event.getUserId();
        this.profileImage = event.getProfileImageUrl();
    }

    @CommandHandler
    public void handle(UserUnfollowedCommand command) {
        if (this.following.contains(command.getFolloweeId())) {
            AggregateLifecycle.apply(new UserUserUnfollowedEvent(
                    command.getFollowerId(),
                    command.getFolloweeId()
            ));
        }
    }

    @CommandHandler
    public void handle(AddNotificationCommand cmd) {
        AggregateLifecycle.apply(new NotificationAddedEvent(cmd.getUserId(), cmd.getNotificationId(), cmd.getContent(), cmd.getStatus()));
    }

    @CommandHandler
    public void handle(MarkNotificationsSeenCommand cmd) {
        AggregateLifecycle.apply(new NotificationsSeenEvent(cmd.getUserId(), cmd.getNotificationIds()));
    }

    @EventSourcingHandler
    public void on(@NotNull UserUserUnfollowedEvent event) {
        this.user_id = event.getFollowerId();
        this.following.remove(event.getFolloweeId());
    }

    @EventSourcingHandler
    public void on(NotificationAddedEvent event) {
        if (this.user_id == event.getUserId()) {
            notifications.add(new Notification(new MessageCommand(event.getNotificationId(), event.getContent(), event.getUserId(), LocalDateTime.now(), event.getStatus(), false)));
        }
    }

    @EventSourcingHandler
    public void on(NotificationsSeenEvent event) {
        for (UUID id : event.getNotificationIds()) {
            //new MessageStatusChangeCommand(id);
            notifications.removeIf(n -> n.getNotificationId().equals(id));

        }
    }
}
