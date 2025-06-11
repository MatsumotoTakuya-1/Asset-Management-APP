package com.example.server.domain.goal

import java.math.BigDecimal


data class GoalRequest(
    val userId: Long,
    val firstValue: Int,
    val targetAmount: Int,
    val targetYear: String,
    val targetRate: BigDecimal,
)