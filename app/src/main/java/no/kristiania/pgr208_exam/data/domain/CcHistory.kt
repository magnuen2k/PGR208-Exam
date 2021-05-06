package no.kristiania.pgr208_exam.data.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CcHistory (
    val data: List<SpecificCcHistory>
)

@JsonClass(generateAdapter = true)
data class SpecificCcHistory (
    val priceUsd: String?,
    val time: Long?,
    val date: String?
)
