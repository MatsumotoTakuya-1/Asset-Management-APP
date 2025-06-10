package com.example.server.controller

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRequest
import com.example.server.service.AssetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/assets")
class AssetController (
    private val asserService: AssetService,
    private val assetRepository: AssetRepository
){

    @GetMapping
    fun getTotalAsset(@PathVariable yearMonth: String?): ResponseEntity<Map<String, Any>> {
        //year_monthがnullの場合今月に置き換え("2025-06")
        val tempYearMonth = yearMonth ?: LocalDate.now().toString().substring(0,7)
        val parsedYearMonth = LocalDate.parse("$tempYearMonth-01")

        val totalAmount = asserService.getTotalFromMemory(parsedYearMonth)
        return ResponseEntity.ok(mapOf("totalAmount" to totalAmount))
    }
//    fun getAssetRecord():List<AssetRecord> {
//        val asset = repository.findAll()
//        return asset.toList();
//    }

    @PostMapping
    fun postAssetRecord(@RequestBody request: AssetRecordRequest) {
        val parsedYearMonth = LocalDate.parse("${request.yearMonth}-01")

        //assetIdからAssetを取得
        val asset = assetRepository.findById(request.assetId).orElseThrow { IllegalArgumentException("Asset is not found") }

        val record = AssetRecord(
            asset = asset,
            yearMonth = parsedYearMonth,
            amount = request.amount,
            memo = request.memo,
            createdAt = LocalDateTime.now(),
        )
        asserService.save(record)
    }


}