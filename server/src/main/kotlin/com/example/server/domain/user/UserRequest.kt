package com.example.server.domain.user


data class UserRequest(
    val name: String,
    val email: String,
    val salt: String,
    val password: String,
)