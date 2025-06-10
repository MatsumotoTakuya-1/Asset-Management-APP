package com.example.server.service

import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import com.example.server.domain.transaction.MonthlySummaryResponse
import com.example.server.domain.transaction.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate


@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {


    fun getMonthlySummaryByUser(userId: Long): List<MonthlySummaryResponse> {
        val transactions = transactionRepository.findAllByUserId(userId)

        return transactions
            .groupBy {
                it.yearMonth.toString().substring(0, 7)
            } // "2025-06-01" → "2025-06". 2025-06: [Transaction(,,), Transaction(..)]
            .map { (month, txs) ->
                val income = txs.filter { it.type == "income" }
                    .sumOf { it.amount.toInt() }

                val expense = txs.filter { it.type == "expense" }
                    .sumOf { it.amount.toInt() }

                MonthlySummaryResponse(
                    month = month,
                    income = income,
                    expense = expense
                )
            }
            .sortedBy { it.month }
    }
}

