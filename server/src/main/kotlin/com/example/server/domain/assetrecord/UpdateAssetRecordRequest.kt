package com.example.server.domain.assetrecord

import java.math.BigDecimal

data class UpdateAssetRecordRequest (
    val amount: BigDecimal,
)