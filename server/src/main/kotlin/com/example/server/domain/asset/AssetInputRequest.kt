package com.example.server.domain.asset

import java.math.BigDecimal

data class AssetInputRequest(
    val name: String,
    val userId: Long,
    val amount: BigDecimal,
    val memo: String?
)
