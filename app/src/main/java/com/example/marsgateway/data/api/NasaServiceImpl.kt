package com.example.marsgateway.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NasaServiceImpl {
    private val baseUrl = "https://api.nasa.gov/"
    private val builder = Retrofit
    .Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
        .build()

    val nasaApi = builder.create(NasaService::class.java)
}