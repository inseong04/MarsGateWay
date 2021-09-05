package com.example.marsgateway.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marsgateway.util.MyApplication
import com.example.marsgateway.repository.MarsRoverLandingRepository

class MarsRoverLandingViewModelFactory(private val application: MarsRoverLandingRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MarsRoverLandingViewModel(MarsRoverLandingRepository(MyApplication.getApplication())) as T
    }

}