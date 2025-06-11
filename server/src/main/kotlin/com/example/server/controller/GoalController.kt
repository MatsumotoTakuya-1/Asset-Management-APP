package com.example.server.controller

import com.example.server.domain.goal.Goal
import com.example.server.domain.goal.GoalRepository
import com.example.server.domain.goal.GoalRequest
import com.example.server.domain.user.UserRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/goals")
class GoalController(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository
) {

    @PostMapping
    fun postGoal(@RequestBody request: GoalRequest) {
        val user =
            userRepository.findById(request.userId).orElseThrow { IllegalArgumentException("User is not found") }

        val record = Goal(
            user = user,
            firstValue = request.firstValue,
            targetAmount = request.targetAmount,
            targetYear = request.targetYear,
            targetRate = request.targetRate,
            createdAt = LocalDateTime.now(),
        )
        goalRepository.save(record)

    }
}