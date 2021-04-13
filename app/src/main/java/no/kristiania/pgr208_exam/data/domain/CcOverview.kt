package no.kristiania.pgr208_exam.data.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CcOverview (
    val data: List<SpecificCcData>
)

@JsonClass(generateAdapter = true)
data class SpecificCcData (
    val id: String?,
    val rank: String?,
    val symbol: String?,
    val name: String?,
    val supply: String?,
    val maxSupply: String?,
    val marketCapUsd: String?,
    val volumeUsd24Hr: String?,
    val priceUsd: String?,
    val changePercent24Hr: String?,
    val vwap24Hr: String?,
    val explorer: String?
)


