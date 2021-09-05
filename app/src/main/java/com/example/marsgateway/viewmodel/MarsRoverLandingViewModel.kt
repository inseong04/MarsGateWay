package com.example.marsgateway.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marsgateway.model.MarsRoverResponseModel
import com.example.marsgateway.repository.MarsRoverLandingRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsRoverLandingViewModel(repository: MarsRoverLandingRepository) : ViewModel() {
    private val repository : MarsRoverLandingRepository = repository
    var photosList : MutableLiveData<MarsRoverResponseModel> = MutableLiveData()


    suspend fun getPhotos(application: Application,earthDate : String, page : Int, apiKey : String) {
        if (photosList != MutableLiveData<MarsRoverResponseModel>()) {
            photosList = MutableLiveData()
        }

        repository.getPhotos(earthDate, page, apiKey)
            .enqueue(object : Callback<MarsRoverResponseModel> {

                override fun onResponse(
                    call: Call<MarsRoverResponseModel>,
                    response: Response<MarsRoverResponseModel>
                ) {
                    if (response.isSuccessful) {
                        photosList.setValue(response.body())
                        Log.e("success", "success")
                    }
                    else if (response.code() == 500) {
                        Toast.makeText(application, "Rover has not uploaded the photo yet.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(application, "Error Occurred\n ERROR CODE : ${response.code()}\n ERROR BODY : ${response.message()}"
                            , Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MarsRoverResponseModel>, t: Throwable) {
                    t.printStackTrace()
                }

            })
    }



}