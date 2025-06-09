package com.example.server

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssetRecordRepository : CrudRepository<AssetRecord, Long>
