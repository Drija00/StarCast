package com.example.Stars.queries.query

import com.example.Stars.queries.read_model.LikeSummary
import com.example.Stars.queries.read_model.Notification
import com.example.Stars.queries.read_model.StarSummary
import com.example.Stars.queries.read_model.UserSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class GetUsersQuery
class GetLoginUserQuery(
    val username: String? = null,
    val password: String? = null
)
class GetUserByIdQuery(
    val userId: UUID? = null
)

class GetFilteredUsers(
    val filter: String? = null,
    val pageNumber: Int = 0,
    val pageSize: Int = 12,
)

class GetUserForRegistrationQuery(
    val username: String? = null,
    val email: String? = null
)
class GetUserByUsernameQuery(
    val username: String? = null,
)

class GetStarQuery(
    val star_id: UUID? = null,
)
class GetUserStarsQuery(
    val userId: UUID? = null,
    val pageNumber: Int = 0,
    val pageSize: Int = 12,
)
class GetUserForYouStarsQuery(
    val userId: UUID? = null,
    val pageNumber: Int = 0,
    val pageSize: Int = 12,
)

class GetStarsQuery(
    val pageNumber: Int = 0,
    val pageSize: Int = 12,
)

class GetFollowsQuery
class GetFollowQuery(
    val follower_id: UUID? = null,
    val followee_id: UUID? = null
)
class GetLikesQuery
class GetLikeQuery(
    val user_id: UUID? = null,
    val star_id: UUID? = null,
)

class GetStarLikesQuery(
    val star_id: UUID? = null
)

class GetNotiicationsForUser(
    val user_id: UUID,
    val pageNumber: Int = 0,
    val pageSize: Int = 12,
)

interface UserSummaryRepository : JpaRepository <UserSummary, UUID>{
    fun findByUsername(username: String): Optional<UserSummary>
    fun findByUsernameAndPassword(username: String, password: String): Optional<UserSummary>
    fun findByUsernameOrEmail(username: String, email: String): Optional<UserSummary>
    @Query("SELECT u FROM UserSummary u where u.username like %:filter% or u.firstName like %:filter% or u.lastName like %:filter%")
    fun findAllUsersByFilter(filter:String, pageable: Pageable): Page<UserSummary>
    @Query("SELECT COUNT(u) FROM UserSummary u where u.username like %:filter% or u.firstName like %:filter% or u.lastName like %:filter%")
    fun countAllUsersByFilter(filter:String): Long
}
interface StarSummaryRepository : JpaRepository <StarSummary, UUID>{
    @Query("SELECT s FROM StarSummary s LEFT JOIN FETCH s.likes LEFT JOIN FETCH s.images")
    override fun findAll(pageable: Pageable): Page<StarSummary>
    @Query("SELECT s FROM StarSummary s LEFT JOIN FETCH s.images WHERE s.starId=:starId" )
    fun findByStarId(starId: UUID): Optional<StarSummary>
    @Query(
        "SELECT s FROM StarSummary s WHERE s.user.userId IN ( SELECT f.userId FROM UserSummary u JOIN u.following f WHERE u.userId = :userId) OR s.user.userId = :userId"
    )
    fun findAllStarsForUsersForYou(userId: UUID, pageable: Pageable): Page<StarSummary>
    @Query(
        " SELECT COUNT(s) FROM StarSummary s WHERE s.user.userId IN (SELECT f.userId FROM UserSummary u JOIN u.following f WHERE u.userId = :userId) OR s.user.userId = :userId"
    )
    fun countStarsForUsersForYou(userId: UUID): Long

    @Query("SELECT COUNT(s) FROM StarSummary s WHERE s.user.userId = :userId")
    fun countStarsForUsers(userId: UUID): Long

    fun findAllByUser(user: UserSummary, pageable: Pageable): Page<StarSummary>

}
    interface NotificationRepository : JpaRepository <Notification, UUID>{
        @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.timestamp >= :oneWeekAgo")
        fun findRecentNotifications(
            @Param("user") user: UserSummary,
            @Param("oneWeekAgo") oneWeekAgo: LocalDateTime,
            pageable: Pageable
        ): Page<Notification>
    }

//interface FollowSummaryRepository : JpaRepository <FollowSummary, UUID>
//{
//    fun findByFollowerAndFollowee(follower: UserSummary, followee: UserSummary): Optional<FollowSummary>
//}
interface LikeRepository : JpaRepository <LikeSummary, UUID>{
    fun findByUserAndStar(user: UserSummary, star: StarSummary): Optional<LikeSummary>
    fun findAllByStar(star: StarSummary): Optional<List<LikeSummary>>
}