package com.example.server.domain.assetrecord

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRecordRepository : CrudRepository<AssetRecord, Long>

