package com.example.marsgateway.view.marsweather

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.marsgateway.R
import com.example.marsgateway.databinding.FragmentMarsWeatherBinding

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.marsgateway.viewmodel.MainViewModel
import com.example.marsgateway.adapter.WeatherListAdapter


class MarsWeatherFragment : Fragment() {

    private lateinit var binding: FragmentMarsWeatherBinding
    private lateinit var vM : MainViewModel

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mars_weather, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vM = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        
        vM.earthDate.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: earthDate ${vM.earthDate.value}")
            binding.dayText.text = vM.earthDate.value.toString()
        })
        vM.earthDate.observe(requireActivity(), Observer {
            binding.solText.text = vM.sol.value.toString()
        })
        vM.todayMarsWeather.observe(requireActivity(), Observer {
            binding.temperatureText.text = vM.todayMarsWeather.value.toString()
        })
        vM.weatherList.observe(requireActivity(), {
            binding.temperatureRecyclerView.adapter = vM.weatherList.value?.let { it1 ->
                WeatherListAdapter(
                    it1
                )
            }
        })
    }


}