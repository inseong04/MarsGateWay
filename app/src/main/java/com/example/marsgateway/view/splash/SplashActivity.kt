package com.example.marsgateway.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.marsgateway.R
import com.example.marsgateway.view.main.MainActivity

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