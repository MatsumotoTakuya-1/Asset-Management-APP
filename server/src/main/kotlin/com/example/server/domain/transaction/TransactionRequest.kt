package com.example.server.domain.transaction

import java.math.BigDecimal

data class TransactionRequest(
    val userId: Long,
    val yearMonth: String,     // "2025-06"
    val category: String,
    val type: String,          // "income" または "expense"
    val isFixed: Boolean = false,
    val amount: BigDecimal,
    val memo: String? = null
)
