package com.example.marsgateway

import android.app.Application

class MyApplication : Application() {
    init{
        instance = this
    }

    companion object {
        lateinit var instance : MyApplication

        fun getApplication() : Application = instance
    }
}