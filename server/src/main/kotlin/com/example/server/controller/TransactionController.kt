package com.example.server.controller

import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
import com.example.server.domain.transaction.TransactionRequest
import com.example.server.domain.transaction.TransactionResponse
import com.example.server.domain.user.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transaction")
class TransactionController(private val transactionRepository: TransactionRepository,
                            private val userRepository: UserRepository
) {

    @GetMapping("/{yearMonth}")
    fun getTotalAsset(@PathVariable yearMonth: String?): ResponseEntity<List<TransactionResponse>?> {
        val tempYearMonth = yearMonth ?: LocalDate.now().toString()
        val parsedYearMonth = LocalDate.parse(tempYearMonth)


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
                userId = it.user.id!!
            )
        }
        println(response)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/{yearMonth}")
    fun postTransaction(
        @RequestBody request: List<TransactionRequest>,
        @PathVariable yearMonth: String
    ): ResponseEntity<String> {
        val parsedYearMonth = LocalDate.parse(yearMonth)
        val transactions = request.map {
            val user = userRepository.findById(it.userId)
                .orElseThrow{ IllegalArgumentException("User is not found") }

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
        return ResponseEntity.ok("Saved ${transactions.size} records" )


    }


}