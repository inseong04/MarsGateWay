package com.example.marsgateway.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val sol : MutableLiveData<String> = MutableLiveData()
    val earthDate : MutableLiveData<String> = MutableLiveData()
}