package com.example.server.domain.assetrecord

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class AssetRecordResponse (
    val id: Long,
    val amount: BigDecimal,
    val yearMonth: LocalDate,
)