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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private String profileImage;
    private String backgroundImage;

    private Set<UUID> following = new HashSet<>();
    //private Set<UUID> followers = new HashSet<>();

    public User() {}

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
                        command.getLastname()
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
    public void handle(UserSetBackgroundImageCommand cmd) {
        AggregateLifecycle.apply(
          new UserSetBackgroundImageEvent(
                  cmd.getUserId(),
                  cmd.getBackgroundImage()
          )
        );
    }

    @EventSourcingHandler public User on(@NotNull UserRegisteredEvent event) {
        this.user_id = event.getUserId();
        this.username = event.getUsername();
        this.email = event.getEmail();
        this.password = event.getPassword();
        this.active = event.getActive();

        return this;
    }

    @EventSourcingHandler public void on(@NotNull UserLogingEvent event) {
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

    // Event handler for following a user
    @EventSourcingHandler
    public void on(@NotNull UserUserFollowedEvent event) {
        this.user_id = event.getFollowerId();
        this.following.add(event.getFolloweeId());
    }// Event handler for following a user
    @EventSourcingHandler
    public void on(@NotNull UserSetBackgroundImageEvent event) {
        this.user_id = event.getUserId();
        this.backgroundImage= event.getBackgroundImage();
    }// Event handler for following a user
    @EventSourcingHandler
    public void on(@NotNull UserSetProfileImageEvent event) {
        this.user_id = event.getUserId();
        this.profileImage= event.getProfileImageUrl();
    }

    // Command to unfollow a user
    @CommandHandler
    public void handle(UserUnfollowedCommand command) {
        if (this.following.contains(command.getFolloweeId())) {
            AggregateLifecycle.apply(new UserUserUnfollowedEvent(
                    command.getFollowerId(),
                    command.getFolloweeId()
            ));
        }
    }

    // Event handler for unfollowing a user
    @EventSourcingHandler
    public void on(@NotNull UserUserUnfollowedEvent event) {

        this.user_id = event.getFollowerId();
        this.following.remove(event.getFolloweeId());
    }
}
