package com.example.server.domain.goal

import java.math.BigDecimal

data class GoalResponse(
    val firstValue: Int,
    val targetAmount: Int,
    val targetYear: String,
    val targetRate: BigDecimal,
)
