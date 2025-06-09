package com.example.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@RestController
class AssetController (@Autowired private val repository: AssetRecordRepository
){

    @GetMapping("/api/assets/total")
    fun getAssetRecord():List<AssetRecord> {
        val asset = repository.findAll()
        return asset.toList();
    }

    @PostMapping("/api/assets/total")
    fun postAssetRecord(@RequestBody request: AssetRecordRequest) {
        val parsedYearMonth = LocalDate.parse("${request.yearMonth}-01")
        val record = AssetRecord(
            assetId = request.assetId,
            yearMonth =  parsedYearMonth,
            amount = request.amount,
            memo = request.memo,
            createdAt = LocalDateTime.now(),
        )
        repository.save(record)
    }


}