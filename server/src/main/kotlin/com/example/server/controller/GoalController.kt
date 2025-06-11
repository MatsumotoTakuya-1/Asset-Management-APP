package com.example.server.controller

import com.example.server.domain.goal.Goal
import com.example.server.domain.goal.GoalRepository
import com.example.server.domain.goal.GoalRequest
import com.example.server.domain.goal.GoalResponse
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
        val existingGoal = goalRepository.findByUserId(user.id!!)

        val goal = if (existingGoal != null) {
            //すでに同じUserで目標あれば更新
            existingGoal.apply {
                firstValue = request.firstValue
                targetAmount = request.targetAmount
                targetYear = request.targetYear
                targetRate = request.targetRate
            }
        } else {
            //新規なら登録
            Goal(
                user = user,
                firstValue = request.firstValue,
                targetAmount = request.targetAmount,
                targetYear = request.targetYear,
                targetRate = request.targetRate,
                createdAt = LocalDateTime.now(),
            )
        }
        goalRepository.save(goal)
    }

    @GetMapping
    fun getGoal(): ResponseEntity<GoalResponse> {
        val myGoal = goalRepository.findByUserId(1L)

        val res = GoalResponse(
            firstValue = myGoal!!.firstValue,
            targetAmount = myGoal.targetAmount,
            targetRate = myGoal.targetRate,
            targetYear = myGoal.targetYear
        )
        return ResponseEntity.ok(res)
    }
}