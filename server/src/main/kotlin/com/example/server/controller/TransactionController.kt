package com.example.server.controller

import com.example.server.domain.transaction.Transaction
import com.example.server.domain.transaction.TransactionRepository
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
    fun getTotalAsset(@PathVariable yearMonth: String?): ResponseEntity<List<Transaction>?> {
        val tempYearMonth = yearMonth ?: LocalDate.now().toString()
        val parsedYearMonth = LocalDate.parse(tempYearMonth)


        val transaction = transactionRepository.findByYearMonth(parsedYearMonth)
        println(transaction)
        return ResponseEntity.ok(transaction)
    }


}