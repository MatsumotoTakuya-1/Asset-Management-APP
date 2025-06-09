package com.example.server.initializer

import com.example.server.domain.asset.AssetRecord
import com.example.server.domain.asset.AssetRecordRepository
import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
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
    fun initDatabase(
        assetRepo: AssetRecordRepository,
        userRepo: UserRepository,
        transactionRepo: TransactionRepository
    ) = CommandLineRunner {

        // ✅ ユーザーが存在しない場合のみ追加
        if (userRepo.count() == 0L) {
            val user: User = userRepo.save(
                User(
                    name = "テストユーザー",
                    email = "test@example.com",
                    salt = "random_salt",
                    password = "hashed_password",
                    createdAt = LocalDateTime.now()
                )
            )

            // ✅ 資産データ登録
            if (assetRepo.count() == 0L) {
                assetRepo.saveAll(
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

            // ✅ トランザクションデータ登録
            if (transactionRepo.count() == 0L) {
                transactionRepo.saveAll(
                    listOf(
                        Transaction(
                            user = user,
                            yearMonth = LocalDate.parse("2025-06-01"),
                            category = "給与",
                            type = "income",
                            isFixed = true,
                            amount = BigDecimal("250000"),
                            memo = "月給",
                            createdAt = LocalDateTime.now()
                        ),
                        Transaction(
                            user = user,
                            yearMonth = LocalDate.parse("2025-06-01"),
                            category = "家賃",
                            type = "expense",
                            isFixed = true,
                            amount = BigDecimal("70000"),
                            memo = "固定費",
                            createdAt = LocalDateTime.now()
                        ),
                        Transaction(
                            user = user,
                            yearMonth = LocalDate.parse("2025-06-01"),
                            category = "外食",
                            type = "expense",
                            isFixed = false,
                            amount = BigDecimal("4000"),
                            memo = "友人と夕食",
                            createdAt = LocalDateTime.now()
                        )
                    )
                )
            }
        }
    }
}
