package com.example.server.domain.goal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : CrudRepository<Goal, Long>