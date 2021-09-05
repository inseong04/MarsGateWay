package com.example.marsgateway.data.api

import com.example.marsgateway.model.PictureData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {
    //윤수
    @GET("planetary/apod")
    fun getPicture(@Query("api_key") api_key: String): Call<PictureData>
}