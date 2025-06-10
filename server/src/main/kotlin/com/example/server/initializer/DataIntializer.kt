package com.example.server.initializer

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
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
        assetRepo: AssetRepository,
        assetRecoRepo: AssetRecordRepository,
        userRepo: UserRepository,
        transactionRepo: TransactionRepository
    ) = CommandLineRunner {

        // 必ずユーザーを取得
        val user = userRepo.findByEmail("test@example.com") ?: userRepo.save(
            User(
                name = "テストユーザー",
                email = "test@example.com",
                salt = "random_salt",
                password = "hashed_password",
                createdAt = LocalDateTime.now()
            )
        )

        val asset = assetRepo.findByNameAndUserId("SBI証券",user.id!!) ?: assetRepo.save(
            Asset(
                user=user,
                name = "SBI証券",
                assetType = "stock",
                createdAt = LocalDateTime.now()
            )
        )

        // 資産データがなければ登録
        if (assetRecoRepo.count() == 0L) {
            assetRecoRepo.saveAll(
                listOf(
                    AssetRecord(asset = asset, yearMonth = LocalDate.parse("2025-06-01"), amount = BigDecimal("10000"), memo = "預金", createdAt = LocalDateTime.now()),
                    AssetRecord(asset = asset, yearMonth = LocalDate.parse("2025-06-01"), amount = BigDecimal("30000"), memo = "証券", createdAt = LocalDateTime.now()),
                    AssetRecord(asset = asset, yearMonth = LocalDate.parse("2025-05-01"), amount = BigDecimal("20000"), memo = "先月の資産", createdAt = LocalDateTime.now())
                )
            )
        }

        // トランザクションがなければ登録
        if (transactionRepo.count() == 0L) {
            transactionRepo.saveAll(
                listOf(
                    Transaction(user = user, yearMonth = LocalDate.parse("2025-06-01"), category = "給与", type = "income", isFixed = true, amount = BigDecimal("250000"), memo = "月給", createdAt = LocalDateTime.now()),
                    Transaction(user = user, yearMonth = LocalDate.parse("2025-06-01"), category = "家賃", type = "expense", isFixed = true, amount = BigDecimal("70000"), memo = "固定費", createdAt = LocalDateTime.now()),
                    Transaction(user = user, yearMonth = LocalDate.parse("2025-06-01"), category = "外食", type = "expense", isFixed = false, amount = BigDecimal("4000"), memo = "友人と夕食", createdAt = LocalDateTime.now())
                )
            )
        }
    }
}
