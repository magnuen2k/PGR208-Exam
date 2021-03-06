package no.kristiania.pgr208_exam.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//Creating an instance of moshi and adding a kotlin json adapter factory
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Making the web-request to the api from the given url
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api.coincap.io/v2/")
    .build()

//Lazy init mounting a service using retrofit
object API {
    val COIN_CAP_SERVICE: CoinCapService by lazy {
        retrofit.create(CoinCapService::class.java)
    }
}