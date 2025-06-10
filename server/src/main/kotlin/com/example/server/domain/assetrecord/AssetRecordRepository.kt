package com.example.server.domain.assetrecord

import com.example.server.domain.asset.Asset
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRecordRepository : CrudRepository<AssetRecord, Long> {
    fun findAllByAsset(asset: Asset): AssetRecord
}

