package com.example.Stars.query

import com.example.Stars.read_model.FollowSummary
import com.example.Stars.read_model.LikeSummary
import com.example.Stars.read_model.StarSummary
import com.example.Stars.read_model.UserSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

class GetUsersQuery
class GetLoginUserQuery(
    val username: String? = null,
    val password: String? = null
)

class GetUserForRegistrationQuery(
    val username: String? = null,
    val email: String? = null
)

class GetStarsQuery
class GetFollowsQuery
class GetLikesQuery

interface UserSummaryRepository : JpaRepository <UserSummary, UUID>{
    fun findByUsername(username: String): Optional<UserSummary>
    fun findByUsernameAndPassword(username: String, password: String): Optional<UserSummary>
    fun findByUsernameOrEmail(username: String, email: String): Optional<UserSummary>
}
interface StarSummaryRepository : JpaRepository <StarSummary, UUID>{
    fun findByStarId(starId: UUID): Optional<StarSummary>
}
interface FollowSummaryRepository : JpaRepository <FollowSummary, UUID>
interface LikeRepository : JpaRepository <LikeSummary, UUID>