package com.example.server

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import com.example.server.domain.assetrecord.AssetRecordRequest
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
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
class AssetRecordTest  (
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
    @Autowired val assetRecordRepository: AssetRecordRepository
){


    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var assetRepository: AssetRepository

    @BeforeEach
    fun setup() {
        // 各テストは項目が空の状態で始める。
        assetRecordRepository.deleteAll()
    }


    @Test
    fun contextLoads() {
    }

    @Test
    fun `GETリクエストはOKステータスを返す`() {
        // localhost/api/assets/total に GETリクエストを発行する。
        val response = restTemplate.getForEntity("http://localhost:$port/api/assets", String::class.java)
        // レスポンスのステータスコードは OK である。
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }


    @Test
    fun `特定の年月の総資産額を取得できる`() {
        // Asset を作成して保存（asset_id = 1, 2 の代わり）
        val asset1 = assetRepository.save(
            Asset(
                user = userRepository.save(
                    User(
                        name = "User A",
                        email = "a@example.com",
                        salt = "salt1",
                        password = "pass1",
                        createdAt = LocalDateTime.now()
                    )
                ),
                name = "証券口座A",
                assetType = "stock",
                createdAt = LocalDateTime.now()
            )
        )

        val asset2 = assetRepository.save(
            Asset(
                user = asset1.user, // 同じユーザーにしてもOK
                name = "証券口座B",
                assetType = "cash",
                createdAt = LocalDateTime.now()
            )
        )
        // テストデータ追加
        assetRecordRepository.save(
            AssetRecord(
                asset = asset1,
                yearMonth = LocalDate.parse("2025-06-01"),
                amount = BigDecimal("1000"),
                memo = "test",
                createdAt = LocalDateTime.now()
            )
        )
        assetRecordRepository.save(AssetRecord(
            asset = asset2,
            yearMonth = LocalDate.parse("2025-06-01"),
            amount = BigDecimal("500"),
            memo = "test2",
            createdAt = LocalDateTime.now()
        ))

        // APIコール
        val response = restTemplate.getForEntity("http://localhost:$port/api/assets/2025-06", Map::class.java)

        // 検証
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.get("totalAmount").toString(), equalTo("1500.0"))
    }

}