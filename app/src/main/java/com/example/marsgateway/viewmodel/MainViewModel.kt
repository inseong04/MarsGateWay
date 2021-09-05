package com.example.marsgateway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marsgateway.model.MarsWeather.Weather

class MainViewModel : ViewModel() {

    val sol : MutableLiveData<String> = MutableLiveData()
    val earthDate : MutableLiveData<String> = MutableLiveData()
    val todayMarsWeather : MutableLiveData<String> = MutableLiveData()
    val weatherList : MutableLiveData<ArrayList<Weather>> = MutableLiveData()
}