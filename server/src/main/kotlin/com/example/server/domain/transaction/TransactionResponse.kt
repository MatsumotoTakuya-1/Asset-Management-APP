package com.example.server.domain.transaction

import java.math.BigDecimal
import java.time.LocalDate

data class TransactionResponse(
    val id: Long,
    val userId: Long,
    val yearMonth: LocalDate,     // "2025-06-01"
    val category: String,
    val type: String,          // "income" または "expense"
    val isFixed: Boolean,
    val amount: BigDecimal,
    val memo: String? = null
)