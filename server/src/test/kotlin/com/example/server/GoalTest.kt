package com.example.server

import com.example.server.domain.goal.Goal
import com.example.server.domain.goal.GoalRepository
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoalControllerTest(
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int,
    @Autowired val goalRepository: GoalRepository,
    @Autowired val userRepository: UserRepository,
) {

    lateinit var user: User

    @BeforeEach
    fun setup() {
        goalRepository.deleteAll()
        userRepository.deleteAll()

        user = userRepository.save(
            User(
                name = "テストユーザー",
                email = "test@example.com",
                salt = "salt",
                password = "hashed_password",
                createdAt = LocalDateTime.now()
            )
        )
    }

    @Test
    fun `POSTでGoalが新規作成される`() {
        val request = mapOf(
            "userId" to user.id,
            "firstValue" to 100,
            "targetAmount" to 1000000,
            "targetYear" to "2030",
            "targetRate" to "5.0"
        )

        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/goals",
            request,
            String::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val goal = goalRepository.findByUserId(user.id!!)
        assertThat(goal?.targetAmount, equalTo(1000000))
        assertThat(goal?.targetYear, equalTo("2030"))
    }

    @Test
    fun `GETで保存済みGoalが取得できる`() {
        // 事前にGoalを保存
        goalRepository.save(
            Goal(
                user = user,
                firstValue = 200,
                targetAmount = 500000,
                targetYear = "2028",
                targetRate = BigDecimal("3.0"),
                createdAt = LocalDateTime.now()
            )
        )

        val response = restTemplate.getForEntity(
            "http://localhost:$port/api/goals",
            Map::class.java
        )

        assertThat(response.statusCode, equalTo(HttpStatus.OK))
        val body = response.body!!
        assertThat(body["targetAmount"], equalTo(500000))
        assertThat(body["targetYear"], equalTo("2028"))
        assertThat(body["firstValue"], equalTo(200))
        assertThat(body["targetRate"], equalTo(3.0))
    }
}