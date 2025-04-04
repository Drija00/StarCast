package com.example.Stars.apis.api

import com.example.Stars.queries.read_model.Notification
import com.example.Stars.queries.read_model.NotificationStatus
import com.example.Stars.write_model.User
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserRegisteredEvent(val userId: UUID, val username: String, val email: String, val password: String, val joinDate: LocalDateTime,val active: Boolean, val firstname: String, val lastname: String, val description: String)
data class UserLogingEvent(val userId: UUID, val active: Boolean)
data class UserUserFollowedEvent(val followerId: UUID, val followeeId: UUID)
data class UserUserUnfollowedEvent(val followerId: UUID, val followeeId: UUID)
data class UserSetProfileImageEvent(val userId: UUID, val profileImageUrl: String)
data class UserSetDescriptionEvent(val userId: UUID, val description: String)
data class UserSetBackgroundImageEvent(val userId: UUID, val backgroundImage: String)

data class UserFollowedEvent(val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UserUnfollowedEvent(val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)

data class MessageEvent(val messageId: UUID, val content: String,val userId: UUID ,val timestamp: LocalDateTime, val status: NotificationStatus, val seen: Boolean)
data class MessageStatusChangeEvent(val messageId: UUID)

data class NotificationAddedEvent (val userId: UUID,val notificationId: UUID, val content: String, val status: NotificationStatus)
data class NotificationsSeenEvent (val userId: UUID, val notificationIds: List<UUID>)

data class StarPostedEvent(val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime, val active: Boolean, val images: List<String>)
data class StarUpdatedEvent(val starId: UUID, val content: String, val user: User, val timestamp : LocalDateTime)
data class StarDeletedEvent(val starId: UUID, val active: Boolean)

data class StarLikedEvent(val likeId: UUID, val userId: UUID, val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class StarUnlikedEvent(val likeId: UUID, val userId: UUID,val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)