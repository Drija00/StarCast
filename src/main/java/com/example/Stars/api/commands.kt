package com.example.Stars.api

import com.example.Stars.write_model.User
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.springframework.cglib.core.Local
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class RegisterUserCommand(@TargetAggregateIdentifier val userId: UUID, val username: String,val email: String, val password: String,val active: Boolean)
data class LoginCommand(@TargetAggregateIdentifier val userId: UUID, val email: String, val password: String,val active: Boolean)
data class LogoutCommand(@TargetAggregateIdentifier val userId: UUID,val active: Boolean)


data class FollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val userId: UUID, val followeeUsername: String, val timestamp : LocalDateTime)
data class UnfollowUserCommand(@TargetAggregateIdentifier val followId: UUID, val userId: UUID, val followeeUsername: String, val timestamp : LocalDateTime)


data class PostStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime)
data class UpdateStarCommand(@TargetAggregateIdentifier val starId: UUID, val content: String, val user: User, val timestamp : LocalDateTime)
data class DeleteStarCommand(@TargetAggregateIdentifier val starId: UUID, val timestamp : LocalDateTime)


data class LikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID, val starId: UUID, val timestamp : LocalDateTime)
data class UnlikeStarCommand(@TargetAggregateIdentifier val likeId: UUID, val userId: UUID,val starId: UUID, val timestamp : LocalDateTime)