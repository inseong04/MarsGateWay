package com.example.marsgateway.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.marsgateway.R
import com.example.marsgateway.view.todaypicture.TodayPictureActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity() {
    private val CHECK_USER_DATE = "FIRST_MEET"
    private val TAG = "TodayPictureActivity"
    private val CHECK_EVNET_POPUP = "CHECK_EVNET_POPUP"
    private val CHECK_EVNET_POPUP_NO = "CHECK_EVNET_POPUP_NO"
    private val CHECK_EVNET_POPUP_YES = "CHECK_EVNET_POPUP_YES"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 1500)
    }

}