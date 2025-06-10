package com.example.server.domain.transaction

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.YearMonth

@Repository
interface TransactionRepository : CrudRepository<Transaction, Long> {

    fun findByYearMonth(yearMonth: LocalDate) : List<Transaction>

    fun findAllByUserId(userId: Long): List<Transaction>

    fun findAllByUserIdAndYearMonth(userId: Long, yearMonth: java.time.LocalDate): List<Transaction>

    fun countByUserId(userId: Long): Long
}