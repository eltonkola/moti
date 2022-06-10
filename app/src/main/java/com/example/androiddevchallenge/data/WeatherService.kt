
package com.example.androiddevchallenge.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

class WeatherClient<T> {

    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply { setLevel(HttpLoggingInterceptor.Level.HEADERS) }
        )
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.metaweather.com/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }
}

interface DemoRemoteService {
    @GET("location/search/")
    suspend fun getLocation(@Query("lattlong") lattlong: String): List<GeographicLocation>

    @GET("location/{id}/")
    suspend fun getWeather(@Path("id") id: String): WeatherData

    @GET("https://api.teleport.org/api/locations/{lattlong}")
    suspend fun getLocationImageName(@Path("lattlong") lattlong: String): UrbanSearch

    @GET
    suspend fun getLocationImage(@Url url: String): UrbanImage
}
