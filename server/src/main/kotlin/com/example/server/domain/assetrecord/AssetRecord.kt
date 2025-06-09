package com.example.server.domain.assetrecord

import com.example.server.domain.asset.Asset
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
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "asset_records")
data class AssetRecord(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    var asset: Asset,//実際はAssetエンティティを格納。　asset_record.asset.id
    @Column(name = "year_month", nullable = false)
    var yearMonth: LocalDate,
    @Column(nullable = false)
    val amount: BigDecimal,

    val memo: String? = null,

    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
)