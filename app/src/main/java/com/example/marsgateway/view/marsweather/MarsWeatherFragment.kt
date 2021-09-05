package com.example.marsgateway.view.marsweather

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.databinding.DataBindingUtil
import com.example.marsgateway.R
import com.example.marsgateway.databinding.FragmentMarsWeatherBinding
import android.webkit.WebView

import android.webkit.WebViewClient
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.marsgateway.SecretKeyClass
import com.example.marsgateway.data.api.NasaService
import com.example.marsgateway.data.api.NasaServiceImpl
import com.example.marsgateway.view.MainActivity
import com.example.marsgateway.view.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.w3c.dom.Document


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
    }


}