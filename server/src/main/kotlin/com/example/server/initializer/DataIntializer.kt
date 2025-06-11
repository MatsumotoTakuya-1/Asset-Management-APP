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
import kotlin.random.Random

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

        // ユーザーを作成 or 取得
        val user = userRepo.findByEmail("test@example.com") ?: userRepo.save(
            User(
                name = "テストユーザー",
                email = "test@example.com",
                salt = "random_salt",
                password = "hashed_password",
                createdAt = LocalDateTime.now()
            )
        )

        // 資産の作成
        val asset1 = assetRepo.findByNameAndUserId("銀行", user.id!!) ?: assetRepo.save(
            Asset(user = user, name = "銀行", assetType = "bank", createdAt = LocalDateTime.now())
        )

        val asset2 = assetRepo.findByNameAndUserId("証券", user.id!!) ?: assetRepo.save(
            Asset(user = user, name = "証券", assetType = "stock", createdAt = LocalDateTime.now())
        )

        val asset3 = assetRepo.findByNameAndUserId("不動産", user.id!!) ?: assetRepo.save(
            Asset(user = user, name = "不動産", assetType = "rent", createdAt = LocalDateTime.now())
        )

        val asset4 = assetRepo.findByNameAndUserId("仮想通貨", user.id!!) ?: assetRepo.save(
            Asset(user = user, name = "仮想通貨", assetType = "crypto", createdAt = LocalDateTime.now())
        )

        // 過去12ヶ月分のデータ作成
        val today = LocalDate.now()
        val months = (0..11).map { today.minusMonths(it.toLong()).withDayOfMonth(1) }.sorted()

        // 資産データを登録
        val assetRecords = months.flatMap { date ->
            listOf(
                AssetRecord(
                    asset = asset1,
                    yearMonth = date,
                    amount = BigDecimal(Random.nextInt(50000, 200000)),
                    memo = "${date.monthValue}月の銀行資産",
                    createdAt = LocalDateTime.now()
                ),
                AssetRecord(
                    asset = asset2,
                    yearMonth = date,
                    amount = BigDecimal(Random.nextInt(100000, 500000)),
                    memo = "${date.monthValue}月の証券資産",
                    createdAt = LocalDateTime.now()
                ),
                AssetRecord(
                    asset = asset3,
                    yearMonth = date,
                    amount = BigDecimal(Random.nextInt(300000, 700000)),
                    memo = "${date.monthValue}月の不動産資産",
                    createdAt = LocalDateTime.now()
                ),
                AssetRecord(
                    asset = asset4,
                    yearMonth = date,
                    amount = BigDecimal(Random.nextInt(10000, 200000)),
                    memo = "${date.monthValue}月の仮想通貨資産",
                    createdAt = LocalDateTime.now()
                )
            )
        }
        assetRecoRepo.saveAll(assetRecords)

        // トランザクションを登録
        val transactions = months.flatMap { date ->
            listOf(
                Transaction(
                    user = user,
                    yearMonth = date,
                    category = "給与",
                    type = "income",
                    isFixed = true,
                    amount = BigDecimal("250000"),
                    memo = "${date.monthValue}月の月給",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = date,
                    category = "家賃",
                    type = "expense",
                    isFixed = true,
                    amount = BigDecimal("70000"),
                    memo = "${date.monthValue}月の家賃",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = date,
                    category = "外食",
                    type = "expense",
                    isFixed = false,
                    amount = BigDecimal(Random.nextInt(3000, 15000)),
                    memo = "${date.monthValue}月の外食費",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = date,
                    category = "日用品",
                    type = "expense",
                    isFixed = false,
                    amount = BigDecimal(Random.nextInt(2000, 10000)),
                    memo = "${date.monthValue}月の日用品購入",
                    createdAt = LocalDateTime.now()
                )
            )
        }
        transactionRepo.saveAll(transactions)
    }
}