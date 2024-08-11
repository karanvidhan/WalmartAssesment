package com.example.myapplication.data.remote

import com.example.myapplication.data.local.APODEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getAPOD(
        @Query("api_key") apiKey: String
    ): Response<APODEntity>
}
