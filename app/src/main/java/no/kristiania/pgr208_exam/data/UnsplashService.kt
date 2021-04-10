package no.kristiania.pgr208_exam.data

import no.kristiania.pgr208_exam.data.domain.Image
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {

    @GET("photos")
    suspend fun getPaginationPage(@Query("page") page: String, @Query("client_id") client_id:  String): List<Image>

    @GET("photos/{id}?client_id=C-ZjCJxgg8vlnSPrYoav8yb-YRZmiaY43RBxVCjd_VU")
    suspend fun getImage(@Path("id") id: String): Image

}