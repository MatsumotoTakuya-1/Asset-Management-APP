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
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionTest(
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
    @Autowired val transactionRepository: TransactionRepository,
    @Autowired val userRepository: UserRepository,
) {

    private lateinit var user: User

    @BeforeEach
    fun setup() {
        transactionRepository.deleteAll()
//        userRepository.deleteAll()

        user = userRepository.save(
            User(
                name = "テストユーザー",
                email = "test@example.com",
                salt = "salt",
                password = "hashed_password",
                createdAt = LocalDateTime.now()
            )
        )
        // データ登録
        transactionRepository.saveAll(
            listOf(
                Transaction(
                    user = user,
                    yearMonth = LocalDate.parse("2025-06-01"),
                    category = "給与",
                    type = "income",
                    isFixed = true,
                    amount = BigDecimal("250000"),
                    memo = "給料",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = LocalDate.parse("2025-06-01"),
                    category = "副業",
                    type = "income",
                    isFixed = false,
                    amount = BigDecimal("50000"),
                    memo = "副業収入",
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
                    yearMonth = LocalDate.parse("2025-04-01"),
                    category = "給料",
                    type = "income",
                    isFixed = true,
                    amount = BigDecimal("300000"),
                    memo = "4月給料",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = LocalDate.parse("2025-04-01"),
                    category = "家賃",
                    type = "expense",
                    isFixed = true,
                    amount = BigDecimal("70000"),
                    memo = "4月家賃",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = LocalDate.parse("2025-05-01"),
                    category = "副業",
                    type = "income",
                    isFixed = false,
                    amount = BigDecimal("50000"),
                    memo = "副収入",
                    createdAt = LocalDateTime.now()
                ),
                Transaction(
                    user = user,
                    yearMonth = LocalDate.parse("2025-05-01"),
                    category = "食費",
                    type = "expense",
                    isFixed = false,
                    amount = BigDecimal("15000"),
                    memo = "外食など",
                    createdAt = LocalDateTime.now()
                )
            )
        )
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun `POSTで複数トランザクションが保存される`() {
        val payload = listOf(
            mapOf(
                "userId" to user.id,
                "amount" to "20000",
                "memo" to "test1",
                "category" to "案件A",
                "type" to "income",
                "isFixed" to true,
                "yearMonth" to "2025-06-01",
            ),
            mapOf(
                "userId" to user.id,
                "amount" to "40000",
                "memo" to "test2",
                "category" to "案件B",
                "type" to "expense",
                "isFixed" to false,
                "yearMonth" to "2025-06-01",
            )
        )

        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/transactions/2025-06-01",
            payload,
            String::class.java
        )
        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val saved = transactionRepository.findByYearMonth(LocalDate.parse("2025-06-01"))
        assertThat(saved.size, equalTo(5))
    }

    @Test
    fun `GETでtransactionエンティティから情報取得`() {
        // テストデータ追加
        transactionRepository.save(
            Transaction(
                user = user,
                yearMonth = LocalDate.parse("2025-06-01"),
                category = "給料",
                type = "income",
                isFixed = true,
                amount = BigDecimal("1000"),
                memo = "test",
                createdAt = LocalDateTime.now()
            )
        )

        val response = restTemplate.exchange(
            "http://localhost:$port/api/transactions/2025-06-01",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<TransactionResponse>>() {}
        )

        // 検証
        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        assertThat(response.body?.last()?.amount, equalTo(BigDecimal("1000.00")))
    }

    @Test
    fun `収入カテゴリ別の合計が正しく返される`() {
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/transactions/2025-06-01/summary/income",
            Map::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val body = response.body as Map<*, *>
        assertThat(body["給与"], equalTo(250000.0))
        assertThat(body["副業"], equalTo(50000.0))
        //家賃keyないこと確認
        assertThat(body.containsKey("家賃"), equalTo(false)) // expenseは含まれない
    }

    @Test
    fun `支出カテゴリ別の合計が正しく返される`() {
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/transactions/2025-06-01/summary/expense",
            Map::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))

        val body = response.body as Map<*, *>
        assertThat(body["家賃"], equalTo(70000.0))
        assertThat(body.containsKey("給与"), equalTo(false))
    }

    @Test
    fun `月ごとの収支サマリーが返る`() {
        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/transactions/monthly-summary${user.id}",
            List::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val body = response.body!!
        assertThat(body.size, equalTo(3))

        val april = body.find { it["month"] == "2025-04" }!!
        assertThat(april["income"], equalTo(300000))
        assertThat(april["expense"], equalTo(70000))

        val may = body.find { it["month"] == "2025-05" }!!
        assertThat(may["income"], equalTo(50000))
        assertThat(may["expense"], equalTo(15000))
    }
}