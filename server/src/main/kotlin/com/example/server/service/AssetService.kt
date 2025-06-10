package com.example.server.service

import com.example.server.domain.asset.Asset
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class AssetService(
    private val repository: AssetRecordRepository
) {

    fun getFromMemory(asset: Asset, yearMonth: LocalDate): List<AssetRecord?> {
        return repository.findAll()
            .filter { it.asset == asset }
            .filter { it.yearMonth == yearMonth }
    }

    fun getTotalFromMemory(yearMonth: LocalDate): BigDecimal {
        return repository.findAll()
            .filter { it.yearMonth == yearMonth }
            .sumOf { it.amount }
    }

    fun save(record: AssetRecord): AssetRecord {
        return repository.save(record)
    }
}