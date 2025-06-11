package com.example.server.domain.goal

import com.example.server.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "goals")
data class Goal(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "first_value", nullable = false)
    var firstValue: Int,

    @Column(name = "target_amount", nullable = false)
    var targetAmount: Int,

    @Column(name = "target_year", nullable = false)
    var targetYear: String,

    @Column(name = "target_rate", nullable = false)
    var targetRate: BigDecimal,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
)


