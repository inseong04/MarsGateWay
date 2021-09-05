package com.example.marsgateway.view.marsweather

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MarsWeatherFragment : Fragment() {

    private lateinit var binding : FragmentMarsWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_mars_weather, container, false)

//        GlobalScope.launch(Dispatchers.IO) {
//            val html = Jsoup.connect("https://mars.nasa.gov/layout/embed/image/m20weather/")
//                .header("access-control-allow-headers", "x-requested-with, Content-Type, origin, authorization, accept, client-security-token" )
//                .header("access-control-allow-methods", "POST, GET, OPTIONS, DELETE, PUT")
//                .header("access-control-allow-origin", "*")
//                .header("content-encoding", "gzip")
//                .header("content-type", "text/html;charset=UTF-8")
//                .header("via", "1.1 9eb3951df99086653d796bb1f065106f.cloudfront.net (CloudFront)")
//                .header("x-amz-cf-id", "82mJJH_CaIebd4426buz6crhL_FeV6eb1jQrZzPYl-FMiJFc2EZVdQ==")
//                .header("x-amz-cf-pop", "LAX3-C4")
//                .get()

//            withContext(Dispatchers.Main){
//                Log.d(TAG, "onCreateView: result ${html.body()}")
//            }
//        }
        return binding.root
    }
}