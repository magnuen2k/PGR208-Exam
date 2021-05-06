package no.kristiania.pgr208_exam.data

import no.kristiania.pgr208_exam.data.domain.CcHistory
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.data.domain.CurrencyData
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinCapService {
    @GET("assets")
    suspend fun getAssetOverview() : CcOverview

    @GET("assets/{id}")
    suspend fun getCurrency(@Path("id") id: String) : CurrencyData

    @GET("assets/{id}/history?interval=h1")
    suspend fun getInterval(@Path("id") id: String) : CcHistory
}