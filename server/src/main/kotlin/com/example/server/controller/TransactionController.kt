package com.example.server.controller

import com.example.server.domain.transaction.MonthlySummaryResponse
import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
import com.example.server.domain.transaction.TransactionRequest
import com.example.server.domain.transaction.TransactionResponse
import com.example.server.domain.user.User
import com.example.server.domain.user.UserRepository
import com.example.server.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val transactionService: TransactionService
) {

    // 一覧取得 使ってない。
    @GetMapping("/{yearMonth}")
    fun getTotalAsset(@PathVariable yearMonth: String): ResponseEntity<List<TransactionResponse>?> {
        val setYearMonth = yearMonth.take(7) //2025-06-30 -> 2025-06
        val parsedYearMonth = LocalDate.parse("$setYearMonth-01")
        val transactions = transactionRepository.findByYearMonth(parsedYearMonth)
        println(transactions)
        val response = transactions.map {
            TransactionResponse(
                id = it.id!!,
                amount = it.amount,
                memo = it.memo,
                category = it.category,
                type = it.type,
                isFixed = it.isFixed,
                yearMonth = it.yearMonth,
                userId = it.user.id!! //userのid定義が初期値0なら!!不要
            )
        }
        println(response)
        return ResponseEntity.ok(response)
    }

    // 月のデータ登録
    @PostMapping("/{yearMonth}")
    fun postTransaction(
        @RequestBody request: List<TransactionRequest>,
        @PathVariable yearMonth: String
    ): ResponseEntity<String> {
        val setYearMonth = yearMonth.take(7) //2025-06-30 -> 2025-06
        val parsedYearMonth = LocalDate.parse("$setYearMonth-01")//何が来ても１日に固定
        val transactions = request.map {
            val user = userRepository.findById(it.userId)
                .orElseThrow { IllegalArgumentException("User is not found") }

            Transaction(
                user = user,
                amount = it.amount,
                memo = it.memo,
                category = it.category,
                type = it.type,
                isFixed = it.isFixed,
                yearMonth = parsedYearMonth,
                createdAt = LocalDateTime.now()
            )
        }

        transactionRepository.saveAll(transactions)
        return ResponseEntity.ok("Saved ${transactions.size} records")
    }

    // 合計取得（カテゴリ別）
    @GetMapping("/{yearMonth}/summary/{tab}")
    fun getMonthlySummary(
        @PathVariable yearMonth: String,
        @PathVariable tab: String
    ): ResponseEntity<Map<String, BigDecimal>> {
        val setYearMonth = yearMonth.take(7)
        val parsedYearMonth = LocalDate.parse("$setYearMonth-01")
        val transactions = transactionRepository.findByYearMonth(parsedYearMonth)
            .filter { it.type == tab }

        val grouped = transactions.groupBy { it.category }
            .mapValues { entry ->
                entry.value.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            }

        return ResponseEntity.ok(grouped)
    }

    @GetMapping("/monthly-summary/{userId}")
    fun getMonthlySummary(@PathVariable userId: Long): ResponseEntity<List<MonthlySummaryResponse>> {
        val summary = transactionService.getMonthlySummaryByUser(userId)
        return ResponseEntity.ok(summary)
    }


}