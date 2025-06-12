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
    //findAllByAssetの引数にList<>をそのまま入れれない。多分仕様。List<Asset>に対しIN検索するため
    // ar.assetがINのassetsのいずれかであるassetRecordを取得
    @Query("SELECT ar FROM AssetRecord  ar WHERE ar.asset IN :assets")
    fun findAllByAsset(@Param("assets") assets: List<Asset>): List<AssetRecord>

    fun findByAsset(asset: Asset): List<AssetRecord>

    fun findByAssetAndYearMonth(asset: Asset, yearMonth: LocalDate): AssetRecord?

    fun findByAssetOrderByYearMonth(asset: Asset): List<AssetRecord>


}

