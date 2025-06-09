package com.example.server.domain.assetrecord

import java.math.BigDecimal

data class AssetRecordRequest(
    val yearMonth: String,
    val assetId: Long,
    val amount: BigDecimal,
    val memo: String? = null
)

//
//{
//    "yearMonth": "2025-06",
//    "assetId": 10,
//    "amount": 1200000,
//    "memo": "ボーナス月"
//}