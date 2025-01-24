package com.example.Stars.apis.api

import com.example.Stars.write_model.User
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.springframework.cglib.core.Local
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RegisterUserCommand(@TargetAggregateIdentifier val userId: UUID, val username: String,val email: String, val password: String, val joinDate: LocalDateTime, val active: Boolean, val firstname: String, val lastname: String)
data class LoggingCommand(@TargetAggregateIdentifier val userId: UUID, val active: Boolean)
data class UserFollowedCommand(@TargetAggregateIdentifier val followerId: UUID, val followeeId: UUID)
data class UserUnfollowedCommand(@TargetAggregateIdentifier val followerId: UUID, val followeeId: UUID)
data class UserSetProfileImageCommand(@TargetAggregateIdentifier val userId: UUID, val profileImageUrl: String)
data class UserSetBackgroundImageCommand(@TargetAggregateIdentifier val userId: UUID, val backgroundImage: String)


data class FollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UnfollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime, val active: Boolean)


data class PostStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UpdateStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime)
data class DeleteStarCommand(@TargetAggregateIdentifier val starId: UUID, val active: Boolean)


data class LikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID, val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)
data class UnlikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID,val starId: UUID, val timestamp : LocalDateTime, val active: Boolean)