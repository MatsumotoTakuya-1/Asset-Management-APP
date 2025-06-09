package com.example.server.domain.asset



data class AssetRequest(
    val userId: Long,
    val name: String,
    val assetType: Long,
)