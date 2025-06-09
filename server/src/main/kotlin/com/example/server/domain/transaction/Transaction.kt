package com.example.server.domain.transaction

import com.example.server.domain.user.UserRepository
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserRepository,

    @Column(name = "year_month", nullable = false)
    val yearMonth: LocalDate,  // 例: 2025-06-01

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val type: String,  // "income" or "expense"

    @Column(name = "is_fixed")
    val isFixed: Boolean = false,

    @Column(nullable = false)
    val amount: BigDecimal,

    val memo: String? = null,

    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null
)