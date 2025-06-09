package com.example.server

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
interface AssetRecordRepository : CrudRepository<AssetRecord, Long>{
    @Query("SELECT SUM(a.amount) FROM AssetRecord a WHERE a.yearMonth = :yearMonth")
    fun findTotalAmountByYearMonth(@Param("yearMonth") yearMonth: LocalDate): BigDecimal?
}
