package no.kristiania.pgr208_exam.data.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image (
    val data: List<Data>
)

@JsonClass(generateAdapter = true)
data class Data (
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String,
    val explorer: String
)

/*
@JsonClass(generateAdapter = true)
data class test(
        @field:Json(name = "regular") val regular: String
        */
/*val full : String,
        val regular : String,
        val small : String,
        val thumb : String*//*

)

@JsonClass(generateAdapter = true)
data class ImageLocation(
        val title: String?,
        val name: String?,
        val city: String?,
        val country: String?,
        val position: ImageLocationPosition?
)

@JsonClass(generateAdapter = true)
data class ImageLocationPosition(
        val latitude: Double?,
        val longitude: Double?
)
*/


