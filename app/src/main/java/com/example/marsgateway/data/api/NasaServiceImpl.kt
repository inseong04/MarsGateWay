package com.example.marsgateway.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NasaServiceImpl {

    // use : NasaServiceImpl.getNasaService().(FuctionToUse)

    private val baseUrl = "https://api.nasa.gov/"

    private var instance: Retrofit? = null

    public fun getNasaService(): NasaService = getInstance().create(NasaService::class.java)

    private fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

}