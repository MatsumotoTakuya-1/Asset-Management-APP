package com.example.server.domain.assetrecord

import com.example.server.domain.asset.Asset
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.YearMonth

@Repository
interface AssetRecordRepository : CrudRepository<AssetRecord, Long> {
    @Query("SELECT ar FROM AssetRecord  ar WHERE ar.asset IN :assets")
    fun findAllByAsset(@Param("assets") assets: List<Asset>): List<AssetRecord>

    fun findByAssetAndYearMonth(asset: Asset, yearMonth: LocalDate): AssetRecord?

}

