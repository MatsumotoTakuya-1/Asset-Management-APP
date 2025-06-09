package com.example.server.service

import com.example.server.domain.asset.AssetRecord
import com.example.server.domain.asset.AssetRecordRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class AssetService(
    private val repository: AssetRecordRepository
) {

    fun getTotalFromMemory(yearMonth: LocalDate): BigDecimal {
        return repository.findAll()
            .filter { it.yearMonth == yearMonth }
            .sumOf { it.amount }
    }

    fun save(record: AssetRecord): AssetRecord {
        return repository.save(record)
    }
}