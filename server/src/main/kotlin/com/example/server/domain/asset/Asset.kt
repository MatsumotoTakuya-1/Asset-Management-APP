package com.example.server.domain.asset

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
import java.time.LocalDateTime

@Entity
@Table(name = "assets")
data class Asset(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val name: String,
    @Column(name = "asset_type", nullable = false)
    var assetType: String,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,


    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
)


