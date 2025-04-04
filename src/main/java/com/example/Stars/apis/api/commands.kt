package com.example.Stars.apis.api

import com.example.Stars.queries.read_model.Notification
import com.example.Stars.queries.read_model.NotificationStatus
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDateTime
import java.util.UUID

data class RegisterUserCommand(@TargetAggregateIdentifier val userId: UUID, val username: String,val email: String, val password: String, val joinDate: LocalDateTime, val active: Boolean, val firstname: String, val lastname: String, val description: String)
data class LoggingCommand(@TargetAggregateIdentifier val userId: UUID, val active: Boolean)
data class UserFollowedCommand(@TargetAggregateIdentifier val followerId: UUID, val followeeId: UUID)
data class UserUnfollowedCommand(@TargetAggregateIdentifier val followerId: UUID, val followeeId: UUID)
data class UserSetDescriptionCommand(@TargetAggregateIdentifier val userId: UUID, val description: String)
data class UserSetProfileImageCommand(@TargetAggregateIdentifier val userId: UUID, val profileImageUrl: String)
data class UserSetBackgroundImageCommand(@TargetAggregateIdentifier val userId: UUID, val backgroundImage: String)


data class FollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UnfollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)

data class MessageCommand(@TargetAggregateIdentifier val messageId: UUID, val content: String,val userId: UUID ,val timestamp: LocalDateTime, val status: NotificationStatus, val seen: Boolean)
data class MessageStatusChangeCommand(@TargetAggregateIdentifier val messageId: UUID)

data class AddNotificationCommand (@TargetAggregateIdentifier val userId: UUID,val notificationId: UUID, val content: String, val status: NotificationStatus)
data class MarkNotificationsSeenCommand(@TargetAggregateIdentifier val userId: UUID, val notificationIds: List<UUID>)

data class PostStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime, val active: Boolean, val images: List<String>)
data class UpdateStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime)
data class DeleteStarCommand(@TargetAggregateIdentifier val starId: UUID, val active: Boolean)


data class LikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID, val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UnlikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID,val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)