package com.example.marsgateway.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.marsgateway.R
import com.example.marsgateway.data.api.NasaServiceImpl
import com.example.marsgateway.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabLayoutArray = arrayOf("Mars Weather", "Mars Rover Landing")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager, lifecycle)


        TabLayoutMediator(binding.tapLayout, binding.viewPager){tap, position->
            tap.text =tabLayoutArray[position]
        }.attach()

    }
}