package com.example.server.controller

import com.example.server.domain.asset.Asset
import com.example.server.domain.asset.AssetInputRequest
import com.example.server.domain.asset.AssetRepository
import com.example.server.domain.asset.AssetResponse
import com.example.server.domain.asset.MonthlyAssetSummaryResponse
import com.example.server.domain.assetrecord.AssetRecord
import com.example.server.domain.assetrecord.AssetRecordRepository
import com.example.server.domain.assetrecord.AssetRecordRequest
import com.example.server.domain.assetrecord.AssetRecordResponse
import com.example.server.domain.assetrecord.UpdateAssetRecordRequest
import com.example.server.domain.transaction.MonthlySummaryResponse
import com.example.server.domain.user.UserRepository
import com.example.server.service.AssetService
import org.hibernate.sql.Update
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/assets")
class AssetController(
    private val assetService: AssetService,
    private val assetRepository: AssetRepository,
    private val userRepository: UserRepository,
    private val assetRecordRepository: AssetRecordRepository
) {

    @GetMapping("/{yearMonth}")
    fun getTotalAsset(@PathVariable yearMonth: String): ResponseEntity<Map<String, BigDecimal>> {
        val setYearMonth = yearMonth.take(7) //2025-06-30 -> 2025-06
        val parsedYearMonth = LocalDate.parse("$setYearMonth-01")

        val totalAmount = assetService.getTotalFromMemory(parsedYearMonth)
        return ResponseEntity.ok(mapOf("totalAmount" to totalAmount))//{totalAmount:指定付きの総資産}
    }

    //  先月の合計取得（カテゴリ別）
    @GetMapping("/{yearMonth}/summary")
    fun getLastMonthlySummary(
        @PathVariable yearMonth: String,
    ): ResponseEntity<Map<String, BigDecimal>> {
        // 日付をLocalDate型にする処理
        val setYearMonth = yearMonth.take(7)
        val parsedYearMonth = LocalDate.parse("$setYearMonth-01")
//        println(parsedYearMonth)

        //ユーザーが登録したasset情報取ってくる（複数なのでList<Asset>)
        val assets = assetRepository.findAllByUserId(1L)//ユーザid１固定
        println(assets)
        //上記のasset のassetIdを元にassetRecord取ってくる
        val assetRecord = assetService.getFromMemory(assets, parsedYearMonth)

        println(assetRecord)
        val grouped = assetRecord.groupBy { it.asset.name }
            .mapValues { entry -> //List<AssetRecord>を各要素で処理
                // fold(初期値、処理（今回は合計）同じassetIDで同じ年月の場合、更新処理するので実際は合計しなくてもOK
                entry.value.fold(BigDecimal.ZERO) { acc, t -> acc + t.amount }
            }

        return ResponseEntity.ok(grouped)//{銀行:2020,仮想通貨:100,...}
    }

    @PostMapping("/{yearMonth}")
    fun saveAsset(
        @PathVariable yearMonth: String,
        @RequestBody assetInputs: List<AssetInputRequest>
    ): ResponseEntity<String> {
        val parsedYearMonth = LocalDate.parse(yearMonth.take(7) + "-01")

        assetInputs.forEach { assetInput ->
            //userId -> User取得
            val user =
                userRepository.findById(assetInput.userId).orElseThrow { IllegalArgumentException("User is not found") }

            var asset = assetRepository.findByNameAndUser(assetInput.name, user)

            //なければ新規作成
            if (asset == null) {
                asset = assetRepository.save(
                    Asset(
                        name = assetInput.name,
                        assetType = "default",
                        user = user,
                        createdAt = LocalDateTime.now(),
                    )
                )
            }

            val existingRecord = assetRecordRepository.findByAssetAndYearMonth(asset, parsedYearMonth)

            if (existingRecord != null) {
                //上書き保存
                existingRecord.amount = assetInput.amount
                existingRecord.memo = assetInput.memo
                assetRecordRepository.save(existingRecord)
            } else {
                //新規作成
                val record = AssetRecord(
                    asset = asset,
                    yearMonth = parsedYearMonth,
                    amount = assetInput.amount,
                    memo = assetInput.memo ?: "",
                    createdAt = LocalDateTime.now(),
                )
                assetRecordRepository.save(record)
            }


        }
        return ResponseEntity.ok("保存完了")

    }

    //    monthly-summary
    @GetMapping("/monthly-summary")
    fun getMonthlyAssetSummary(): ResponseEntity<List<MonthlyAssetSummaryResponse>> {
        val summary = assetService.getMonthlySummaryByUser(1L) //ユーザ固定
        return ResponseEntity.ok(summary)
    }

    //使ってない。テストも書いてない
    @PostMapping
    fun postAssetRecord(@RequestBody request: AssetRecordRequest) {
        val parsedYearMonth = LocalDate.parse(request.yearMonth)

        //assetIdからAssetを取得
        val asset =
            assetRepository.findById(request.assetId).orElseThrow { IllegalArgumentException("Asset is not found") }

        val record = AssetRecord(
            asset = asset,
            yearMonth = parsedYearMonth,
            amount = request.amount,
            memo = request.memo,
            createdAt = LocalDateTime.now(),
        )
        assetService.save(record)
    }


    //下記から下はテスト書いてない！！
    @GetMapping("/list")
    fun getAssets(): ResponseEntity<List<AssetResponse>> {
        //userId(1L固定）
        val assets = assetRepository.findAllByUserId(1L)
//        println(assets)
        // AssetにUserエンティティを含むとLAZYロードでJSON変換できない。循環参照になるのでDTO指定して返すのがよき
        //下記のようにする。assetは省略してit.idのような書き方もできる
        val response = assets.map { asset -> AssetResponse(
            id = asset.id!!,
            name = asset.name,
            assetType = asset.assetType,
        )}
        return ResponseEntity.ok(response)
    }

    @GetMapping("/history/{assetId}")
    fun getAssetHistory(@PathVariable assetId: Long): ResponseEntity<List<AssetRecordResponse>> {
        val asset = assetRepository.findById(assetId).orElseThrow { IllegalArgumentException("Asset is not found") }
//        val assetRecords = assetRecordRepository.findByAsset(asset)
        val assetRecords = assetRecordRepository.findByAssetOrderByYearMonth(asset)

//
        val response = assetRecords.map { record ->
            AssetRecordResponse(
            id = record.id!!,
            amount = record.amount,
            yearMonth = record.yearMonth,
        )}
        return ResponseEntity.ok(response)
    }

    @PutMapping("/history/{assetRecordId}")
    fun putAssetHistory(
        @PathVariable assetRecordId: Long,
        @RequestBody request: UpdateAssetRecordRequest
    ):ResponseEntity<Void> {
        //findByIdの戻り値はOptional型で中に値あるか不明？入ってなければ例外処理のthrowなければ.amountにアクセスできない
        val assetRecord = assetRecordRepository.findById(assetRecordId).orElseThrow { IllegalArgumentException("Asset is not found") }

        assetRecord.amount = request.amount
        assetRecordRepository.save(assetRecord)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/history/{assetRecordId}")
    fun deleteAssetHistory(@PathVariable assetRecordId: Long): ResponseEntity<Void> {
        assetRecordRepository.deleteById(assetRecordId)
        return ResponseEntity.ok().build()
    }
}