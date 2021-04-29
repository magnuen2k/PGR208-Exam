package no.kristiania.pgr208_exam.data

import no.kristiania.pgr208_exam.data.domain.CcHistory
import no.kristiania.pgr208_exam.data.domain.CcOverview
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinCapService {

    /*@GET("photos")
    suspend fun getPaginationPage(@Query("page") page: String, @Query("client_id") client_id:  String): List<CcOverview>

    @GET("photos/{id}?client_id=C-ZjCJxgg8vlnSPrYoav8yb-YRZmiaY43RBxVCjd_VU")
    suspend fun getImage(@Path("id") id: String): CcOverview*/

    @GET("assets")
    suspend fun getAssetOverview() : CcOverview


    @GET("assets/{id}/history?interval=h1")
    suspend fun getInterval(@Path("id") id: String) : CcHistory

}