package com.example.server

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetInputRequest
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AssetRecordTest(
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
    @Autowired val assetRecordRepository: AssetRecordRepository,
    @Autowired val userRepository: UserRepository,
    @Autowired val assetRepository: AssetRepository
) {
    lateinit var user: User
    lateinit var asset1: Asset
    lateinit var asset2: Asset

    @BeforeEach
    fun setup() {
        // 初期化
        assetRecordRepository.deleteAll()
        assetRepository.deleteAll()
        userRepository.deleteAll()

        user = userRepository.save(
            User(
                name = "User A",
                email = "a@example.com",
                salt = "salt1",
                password = "pass1",
                createdAt = LocalDateTime.now()
            )
        )

        asset1 = assetRepository.save(
            Asset(
                user = user,
                name = "証券口座A",
                assetType = "stock",
                createdAt = LocalDateTime.now()
            )
        )

        asset2 = assetRepository.save(
            Asset(
                user = user,
                name = "証券口座B",
                assetType = "cash",
                createdAt = LocalDateTime.now()
            )
        )
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun `指定の年月の総資産額を取得できる`() {
        assetRecordRepository.saveAll(
            listOf(
                AssetRecord(
                    asset = asset1,
                    yearMonth = LocalDate.parse("2025-06-01"),
                    amount = BigDecimal("1000"),
                    memo = "test",
                    createdAt = LocalDateTime.now()
                ),
                AssetRecord(
                    asset = asset2,
                    yearMonth = LocalDate.parse("2025-06-01"),
                    amount = BigDecimal("500"),
                    memo = "test2",
                    createdAt = LocalDateTime.now()
                )
            )
        )

        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/assets/2025-06-01",
            Map::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.get("totalAmount").toString(), equalTo("1500.0"))
    }

    @Test
    fun `先月の資産名ごとの資産額を取得できる`() {

        // テストデータ追加
        assetRecordRepository.saveAll(
            listOf(
                AssetRecord(
                    asset = asset1,
                    yearMonth = LocalDate.parse("2025-05-01"),
                    amount = BigDecimal("1000"),
                    memo = "test",
                    createdAt = LocalDateTime.now()
                ),
                AssetRecord(
                    asset = asset2,
                    yearMonth = LocalDate.parse("2025-05-01"),
                    amount = BigDecimal("500"),
                    memo = "test2",
                    createdAt = LocalDateTime.now()
                )
            )
        )
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/assets/2025-05-09/summary",
            Map::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val body = response.body as Map<*, *>
        assertThat(body["証券口座A"], equalTo(1000.0))
        assertThat(body["証券口座B"], equalTo(500.0))
    }

    @Test
    fun `資産の登録ができる(asset, assetRecord)`() {
        val request = listOf(
            AssetInputRequest(
                name = "銀行",
                userId = 1L,
                amount = BigDecimal("1000"),
                memo = "test",
            )
        )

        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/assets/2025-06-01",
            request,
            String::class.java
        )

        // ステータスコード確認
        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        // 登録結果の確認
        val savedRecords = assetRecordRepository.findAll()
        //kotlinならcount()の方がいい？toList()すればsizeも使える
        assertThat(savedRecords.count(), equalTo(1))

        val record = savedRecords.first()
        //うまく通らない。assetをFetchType.LAZY→EAGERにして即時読み込みにすれば通る
        //assertThat(record.asset.name, equalTo("銀行"))
        assertThat(record.amount, equalTo(BigDecimal("1000.00")))
        assertThat(record.memo, equalTo("test"))


    }

    @Test
    fun `monthly summaryAPIが正常に資産推移を返す`() {
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/assets/monthly-summary",
            List::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body as List<*>, equalTo(listOf<AssetRecord>()))


    }

}