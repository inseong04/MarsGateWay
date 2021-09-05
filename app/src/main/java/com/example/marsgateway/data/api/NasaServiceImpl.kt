package com.example.marsgateway.data.api

import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object NasaServiceImpl {

    // use : NasaServiceImpl.getNasaService().(FuctionToUse)

    private val baseUrl = "https://api.nasa.gov/"
    private val baseUrl_web = "https://mars.nasa.gov/"

    private var instance: Retrofit? = null
    private var instanceWeb: Retrofit? = null

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

    fun getMarsWeatherService() : NasaService = getInstanceWeb().create(NasaService::class.java)

    private fun getInstanceWeb(): Retrofit {
        if (instanceWeb == null) {
            instanceWeb = Retrofit.Builder()
                .baseUrl(baseUrl_web)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instanceWeb!!
    }
}