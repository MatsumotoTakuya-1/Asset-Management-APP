package com.example.server.service

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.asset.MonthlyAssetSummaryResponse
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import com.example.server.domain.transaction.MonthlySummaryResponse
import com.example.server.domain.user.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.collections.component1
import kotlin.collections.component2

@Service
class AssetService(
    private val repository: AssetRecordRepository,
    private val userRepository: UserRepository,
    private val assetRepository: AssetRepository,
    private val assetRecordRepository: AssetRecordRepository
) {

    fun getFromMemory(assets: List<Asset>, yearMonth: LocalDate): List<AssetRecord> {
        return repository.findAll()
            .filter { it.asset in assets }
            .filter { it.yearMonth == yearMonth }
    }

    fun getTotalFromMemory(yearMonth: LocalDate): BigDecimal {
        return repository.findAll()
            .filter { it.yearMonth == yearMonth }
            .sumOf { it.amount }
    }

    fun getMonthlySummaryByUser(userId: Long): List<MonthlyAssetSummaryResponse> {
        val user =
            userRepository.findById(userId).orElseThrow { IllegalArgumentException("User is not found") }

        val assets = assetRepository.findAllByUser(user)
        val assetRecord = assetRecordRepository.findAllByAsset(assets)

        return assetRecord
            .groupBy {
                it.yearMonth.toString().substring(0, 7)
            } // "2025-06-01" → "2025-06". 2025-06: [AssetRecord(,,), AssetRecord(..)]
            .map { (month, txs) ->
                val myAsset = txs.sumOf { it.amount.toInt() }


                MonthlyAssetSummaryResponse(
                    month = month,
                    amount = myAsset,
                )
            }
            .sortedBy { it.month }
    }

    fun save(record: AssetRecord): AssetRecord {
        return repository.save(record)
    }
}