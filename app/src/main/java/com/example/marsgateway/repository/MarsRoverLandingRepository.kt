package com.example.marsgateway.repository

import android.app.Application
import com.example.marsgateway.data.api.NasaServiceImpl

class MarsRoverLandingRepository(application: Application) {
    private val nasaService = NasaServiceImpl.getNasaService()

    suspend fun getPhotos(earthDate : String, page : Int, apiKey : String)
    = nasaService.getPhotos(earthDate, page, apiKey)

}