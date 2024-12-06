package com.example.Stars.queries.query

import com.example.Stars.queries.read_model.FollowSummary
import com.example.Stars.queries.read_model.LikeSummary
import com.example.Stars.queries.read_model.StarSummary
import com.example.Stars.queries.read_model.UserSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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
)
class GetUserForYouStarsQuery(
    val userId: UUID? = null,
)

class GetStarsQuery
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

interface UserSummaryRepository : JpaRepository <UserSummary, UUID>{
    fun findByUsername(username: String): Optional<UserSummary>
    fun findByUsernameAndPassword(username: String, password: String): Optional<UserSummary>
    fun findByUsernameOrEmail(username: String, email: String): Optional<UserSummary>
}
interface StarSummaryRepository : JpaRepository <StarSummary, UUID>{
    fun findByStarId(starId: UUID): Optional<StarSummary>
    @Query(
        "SELECT s FROM StarSummary s JOIN s.user u where u in (SELECT f.followee from FollowSummary f where f.follower.userId=:userId) OR s.user.userId = :userId order by s.timestamp DESC "
    )
    fun findAllStarsForUsersForYou(userId: UUID): Optional<List<StarSummary>>
    fun findAllByUserOrderByTimestampDesc(user: UserSummary): Optional<List<StarSummary>>
}
interface FollowSummaryRepository : JpaRepository <FollowSummary, UUID>
{
    fun findByFollowerAndFollowee(follower: UserSummary, followee: UserSummary): Optional<FollowSummary>
}
interface LikeRepository : JpaRepository <LikeSummary, UUID>{
    fun findByUserAndStar(user: UserSummary, star: StarSummary): Optional<LikeSummary>
    fun findAllByStar(star: StarSummary): Optional<List<LikeSummary>>
}