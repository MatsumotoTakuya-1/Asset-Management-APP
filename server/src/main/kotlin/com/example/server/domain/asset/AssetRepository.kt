package com.example.server.domain.asset

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRepository : CrudRepository<Asset, Long>{
    fun findByNameAndUserId(name: String, userId: Long): Asset?
}
