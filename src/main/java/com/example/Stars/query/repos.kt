package com.example.Stars.query

import com.example.Stars.read_model.StarSummary
import com.example.Stars.read_model.UserSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

class GetUsersQuery
class GetStarsQuery

interface UserSummaryRepository : JpaRepository <UserSummary, UUID>
interface StarSummaryRepository : JpaRepository <StarSummary, UUID>