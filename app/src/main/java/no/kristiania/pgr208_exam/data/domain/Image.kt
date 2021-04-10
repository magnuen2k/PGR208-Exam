package no.kristiania.pgr208_exam.data.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image (
    val id: String,
    val created_at: String,
    val updated_at: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blur_hash: String,
    val description: String?,
    val alt_description: String?,
    val urls: Urls,
    val likes: Int,
     val location: ImageLocation?
    )

@JsonClass(generateAdapter = true)
data class Urls(
        @field:Json(name = "regular") val regular: String
        /*val full : String,
        val regular : String,
        val small : String,
        val thumb : String*/
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


