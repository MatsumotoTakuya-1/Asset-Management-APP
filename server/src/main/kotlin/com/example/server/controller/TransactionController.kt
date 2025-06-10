package com.example.server.controller

import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
import com.example.server.domain.transaction.TransactionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/transaction")
class TransactionController(private val transactionRepository: TransactionRepository) {

    @GetMapping("/{yearMonth}")
    fun getTotalAsset(@PathVariable yearMonth: String?): ResponseEntity<List<TransactionResponse>?> {
        val tempYearMonth = yearMonth ?: LocalDate.now().toString()
        val parsedYearMonth = LocalDate.parse(tempYearMonth)


        val transactions = transactionRepository.findByYearMonth(parsedYearMonth)

        println(transactions)
        val response = transactions.map { TransactionResponse(
            id = it.id!!,
            amount = it.amount,
            memo = it.memo,
            category = it.category,
            type = it.type,
            isFixed = it.isFixed,
            yearMonth = it.yearMonth,
            userId = it.user.id!!
        ) }
        println(response)

        return ResponseEntity.ok(response)
    }


}