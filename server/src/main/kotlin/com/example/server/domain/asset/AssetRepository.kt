package com.example.server.domain.asset

import com.example.server.domain.user.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AssetRepository : CrudRepository<Asset, Long> {
    fun findByNameAndUserId(name: String, userId: Long): Asset?
    fun findAllByUserId(userId: Long): List<Asset>
    fun findByNameAndUser(name: String, user: User): Asset
}
