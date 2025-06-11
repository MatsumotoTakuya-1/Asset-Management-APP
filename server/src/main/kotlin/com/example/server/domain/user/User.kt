package com.example.server.domain.user


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
//    val id: Long = 0,
    val name: String,
    val email: String,
    val salt: String,
    @Column(name = "password_hash")
    val password: String,
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
)