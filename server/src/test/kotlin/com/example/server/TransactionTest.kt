package com.example.server


import com.example.server.domain.asset.Asset
import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
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
class TransactionTest  (
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
    @Autowired val transactionRepository: TransactionRepository,
){


    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        // 各テストは項目が空の状態で始める。
        transactionRepository.deleteAll()
    }


    @Test
    fun contextLoads() {
    }

    @Test
    fun `POSTした情報がtransactionエンティティに入っているか`() {
        // Asset を作成して保存（asset_id = 1, 2 の代わり）
        val user = userRepository.save(
                    User(
                        name = "User A",
                        email = "a@example.com",
                        salt = "salt1",
                        password = "pass1",
                        createdAt = LocalDateTime.now()
                    )
                )

        // テストデータ追加
        transactionRepository.save(
            Transaction(
                user = user,
                yearMonth = LocalDate.parse("2025-06-01"),
                category = "給料",
                type = "income",
                isFixed =  true,
                amount = BigDecimal("1000"),
                memo = "test",
                createdAt = LocalDateTime.now()
            )
        )


        // APIコール
        val response = restTemplate.getForEntity("http://localhost:$port/api/transaction?yearMonth=2025-06", Map::class.java)

        // 検証
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.get("totalAmount").toString(), equalTo("1500.0"))
    }




}