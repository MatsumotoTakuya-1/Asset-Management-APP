package com.example.server.domain.transaction

data class MonthlySummaryResponse(
    val month: String,
    val income: Int,
    val expense: Int,
)
