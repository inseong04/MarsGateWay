package com.example.marsgateway.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.marsgateway.R
import com.example.marsgateway.data.api.NasaServiceImpl
import com.example.marsgateway.databinding.ActivityMainBinding
import com.example.marsgateway.view.todaypicture.TodayPictureActivity
import com.google.android.material.tabs.TabLayoutMediator
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val CHECK_USER_DATE = "FIRST_MEET"
    private val TAG = "TodayPictureActivity"
    private val CHECK_EVNET_POPUP = "CHECK_EVNET_POPUP"
    private val CHECK_EVNET_POPUP_NO = "CHECK_EVNET_POPUP_NO"
    private val CHECK_EVNET_POPUP_YES = "CHECK_EVNET_POPUP_YES"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabLayoutArray = arrayOf("Mars Weather", "Mars Rover Landing")

        checkShowOrNotshow()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager, lifecycle)



        TabLayoutMediator(binding.tapLayout, binding.viewPager){tap, position->
            tap.text =tabLayoutArray[position]
        }.attach()
    }


    fun checkShowOrNotshow() {
        var chk_today: String? = null
        var pref_user_date: String? = null
        val pref = applicationContext.getSharedPreferences("event_popup", MODE_PRIVATE)
        chk_today = pref.getString(
            CHECK_EVNET_POPUP,
            CHECK_EVNET_POPUP_NO
        )
        pref_user_date = pref.getString(CHECK_USER_DATE, "FIRST_MEET")
        if (pref_user_date != "FIRST_MEET") {
            val my_result = compareUserdateWithToday(pref_user_date)
            //오늘 접속함
            if (my_result == 0) {
                Log.i(TAG, "## 이벤트 팝업 : 오늘 접속함")

                //하루 안보기 클릭
                if (chk_today == CHECK_EVNET_POPUP_YES) {
                    Log.i(TAG, "## 이벤트 팝업 : 하루 안보기 클릭")
                } else if (chk_today == CHECK_EVNET_POPUP_NO) {
                    Log.i(TAG, "## 이벤트 팝업 : 하루 안보기 클릭 안했음")
                    intentPictureActivity()
                }
            } else if (my_result < 0) {
                Log.i(TAG, "## 이벤트 팝업 : 어제 접속함")
                application.getSharedPreferences("event_popup", MODE_PRIVATE).edit().putString(
                    CHECK_EVNET_POPUP,
                    CHECK_EVNET_POPUP_NO
                ).apply()
                pref.edit().putString(CHECK_USER_DATE, getToday()).apply()
                intentPictureActivity()
            } else {
                Log.i(TAG, "## 이벤트 팝업 : 미래에서 왔을일은 없으니 에러")
                application.getSharedPreferences("event_popup", MODE_PRIVATE).edit().putString(
                    CHECK_EVNET_POPUP,
                    CHECK_EVNET_POPUP_NO
                ).apply()
            }
        } else {
            Log.i(TAG, "## 이벤트 팝업 : 앱을 처음 깔음")
            pref.edit().putString(CHECK_USER_DATE, getToday()).apply()
            intentPictureActivity()
        }
    }

    private fun intentPictureActivity() {
        startActivity(Intent(this, TodayPictureActivity::class.java))
    }

    // 미래 : int_calDateDays > 0 양수
    // 현재 : int_calDateDays == 0 같을때
    // 과거 : int_calDateDays < 0 음수
    private fun compareUserdateWithToday(user_date: String?): Int {
        var int_calDateDays: Int? = null
        try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            val FirstDate = format.parse(user_date)
            val SecondDate = format.parse(getToday()) // 오늘 날짜를 yyyy-MM-dd포맷 String 값
            val calDate = FirstDate.time - SecondDate.time
            val calDateDays = calDate / (24 * 60 * 60 * 1000)
            int_calDateDays = calDateDays?.toInt() ?: -1 //long to int
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return int_calDateDays!!
    }


    private fun getToday(): String? {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        return format.format(cal.time)
    }
}