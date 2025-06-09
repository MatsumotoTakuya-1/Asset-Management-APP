package com.example.server

import com.example.server.domain.asset.AssetRecord
import com.example.server.domain.asset.AssetRecordRepository
import com.example.server.domain.asset.AssetRecordRequest
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
    @Autowired val repository: AssetRecordRepository
){


    @BeforeEach
    fun setup() {
        // 各テストは項目が空の状態で始める。
        repository.deleteAll()
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
    fun `POSTリクエストはOKステータスを返す`() {
        // localhost/api/assets/total に POSTリクエストを送る。このときのボディは {"text": "hello"}
        val request = AssetRecordRequest(
            yearMonth = "2025-06",
            assetId = 10,
            amount = BigDecimal("1200000"),
            memo = "ボーナス月"
        )

        val response = restTemplate.postForEntity("http://localhost:$port/api/assets", request, String::class.java)
        // レスポンスのステータスコードは OK であること。
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
    }


    @Test
    fun `POSTリクエストはasset_recordオブジェクトを格納する`() {
        // localhost/api/assets/total に POSTリクエストを送る。
        val request = AssetRecordRequest(
            yearMonth = "2025-06",
            assetId = 10,
            amount = BigDecimal("1200000"),
            memo = "ボーナス月",
        )

        restTemplate.postForEntity("http://localhost:$port/api/assets", request, String::class.java)

        // localhost/api/assets/total に GETリクエストを送り、レスポンスを AssetRecord の配列として解釈する。
        val response = restTemplate.getForEntity("http://localhost:$port/api/assets", Map::class.java)
        val asset = response.body!!
        println("asset: $asset")

        //  asset の長さは 1。
        assertThat(asset.size, equalTo(1))
        // asset[0] には "1200000" をもつAssetRecord が含まれている。
        assertThat(asset["totalAmount"], equalTo(1200000.0))
    }

    @Test
    fun `特定の年月の総資産額を取得できる`() {
        // テストデータ追加
        repository.save(
            AssetRecord(
                assetId = 1,
                yearMonth = LocalDate.parse("2025-06-01"),
                amount = BigDecimal("1000"),
                memo = "test",
                createdAt = LocalDateTime.now()
            )
        )
        repository.save(AssetRecord(
            assetId = 2,
            yearMonth = LocalDate.parse("2025-06-01"),
            amount = BigDecimal("500"),
            memo = "test2",
            createdAt = LocalDateTime.now()
        ))

        // APIコール
        val response = restTemplate.getForEntity("http://localhost:$port/api/assets?yearMonth=2025-06", Map::class.java)

        // 検証
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.get("totalAmount").toString(), equalTo("1500.0"))
    }

}