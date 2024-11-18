package com.example.Stars.api

import com.example.Stars.write_model.User
import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserRegisteredEvent(val userId: UUID, val username: String, val email: String, val password: String,val active: Boolean)
data class UserLoggedInEvent(val userId: UUID, val email: String, val password: String,val active: Boolean)
data class UserLogOutEvent(val userId: UUID,val active: Boolean)

data class UserFollowedEvent(val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime)
data class UserUnfollowedEvent(val followId: UUID, val followerId: UUID, val followeeId: UUID, val timestamp : LocalDateTime)

data class StarPostedEvent(val starId: UUID, val content: String, val userId: UUID, val timestamp : LocalDateTime)
data class StarUpdatedEvent(val starId: UUID, val content: String, val user: User, val timestamp : LocalDateTime)
data class StarDeletedEvent(val starId: UUID, val timestamp : LocalDateTime)

data class StarLikedEvent(val likeId: UUID, val userId: UUID, val starId: UUID, val timestamp : LocalDateTime)
data class StarUnlikedEvent(val likeId: UUID, val userId: UUID,val starId: UUID, val timestamp : LocalDateTime)