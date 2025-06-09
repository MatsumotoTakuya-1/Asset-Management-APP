package com.example.server

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
@Profile("dev")
class DataInitializer {

    @Bean
    fun initDatabase(repository: AssetRecordRepository) = CommandLineRunner {
        if (repository.count() == 0L) {
            repository.saveAll(
                listOf(
                    AssetRecord(
                        assetId = 1,
                        yearMonth = LocalDate.parse("2025-06-01"),
                        amount = BigDecimal("10000"),
                        memo = "預金",
                        createdAt = LocalDateTime.now()
                    ),
                    AssetRecord(
                        assetId = 2,
                        yearMonth = LocalDate.parse("2025-06-01"),
                        amount = BigDecimal("30000"),
                        memo = "証券",
                        createdAt = LocalDateTime.now()
                    ),
                    AssetRecord(
                        assetId = 3,
                        yearMonth = LocalDate.parse("2025-05-01"),
                        amount = BigDecimal("20000"),
                        memo = "先月の資産",
                        createdAt = LocalDateTime.now()
                    )
                )
            )
        }
    }
}