package com.example.server.domain.transaction

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : CrudRepository<Transaction, Long> {

    fun findAllByUserId(userId: Long): List<Transaction>

    fun findAllByUserIdAndYearMonth(userId: Long, yearMonth: java.time.LocalDate): List<Transaction>

    fun countByUserId(userId: Long): Long
}