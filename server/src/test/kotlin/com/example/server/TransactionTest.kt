package com.example.server


import com.example.server.domain.asset.Asset
import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
import com.example.server.domain.transaction.TransactionResponse
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
import net.bytebuddy.matcher.ElementMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
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
    fun `POSTで複数トランザクションが保存される`(){
        val user = userRepository.save(
            User(
                name = "テストマン",
                email = "a@example.com",
                salt = "salt1",
                password = "pass1",
                createdAt = LocalDateTime.now()
            )
        )

        val payload = listOf(
            mapOf(
                "user_id" to user.id,
                "amount" to "20000",
                "memo" to "test1",
                "category" to "案件A",
                "type" to "income",
                "isFixed" to true,
                "yearMonth" to LocalDate.parse("2025-06-01"),
            ),
            mapOf(
                "user_id" to user.id,
                "amount" to "40000",
                "memo" to "test2",
                "category" to "案件B",
                "type" to "expense",
                "isFixed" to false,
                "yearMonth" to LocalDate.parse("2025-06-01"),
            )
        )

        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/transaction/2025-06-01",
            payload,
            String::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val saved = transactionRepository.findByYearMonth(LocalDate.parse("2025-06-01"))

    }

    @Test
    fun `POSTした情報がtransactionエンティティに入っているか`() {
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
//        val response = restTemplate.getForEntity(/* url = */ "http://localhost:$port/api/transaction/2025-06-01", /* responseType = */ List::class.java)

        val response = restTemplate.exchange(
            "http://localhost:$port/api/transaction/2025-06-01",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<TransactionResponse>>() {}
        )

//        println(response.body?.get(0)?.amount)
        // 検証
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.get(0)?.amount, equalTo(BigDecimal("1000.00")))
    }




}