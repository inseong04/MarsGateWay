package com.example.marsgateway.data.api

import com.example.marsgateway.model.MarsRoverResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaService {

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPhotos(@Query("earth_date") earthDate : String, @Query("page") page : Int, @Query("api_key") apiKey : String)
    : Call<MarsRoverResponseModel>


}