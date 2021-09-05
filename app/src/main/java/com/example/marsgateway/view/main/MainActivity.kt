package com.example.marsgateway.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.marsgateway.R
import com.example.marsgateway.adapter.FragmentAdapter
import com.example.marsgateway.databinding.ActivityMainBinding
import com.example.marsgateway.model.MarsWeather.Weather
import com.example.marsgateway.viewmodel.MainViewModel
import com.example.marsgateway.view.todaypicture.TodayPictureActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.jsoup.Jsoup
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var vM : MainViewModel
    private val CHECK_USER_DATE = "FIRST_MEET"
    private val TAG = "TodayPictureActivity"
    private val CHECK_EVNET_POPUP = "CHECK_EVNET_POPUP"
    private val CHECK_EVNET_POPUP_NO = "CHECK_EVNET_POPUP_NO"
    private val CHECK_EVNET_POPUP_YES = "CHECK_EVNET_POPUP_YES"
    var source : String? = null
    var sol : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vM = ViewModelProvider(this)[MainViewModel::class.java]
        val tabLayoutArray = arrayOf("Mars Weather", "Mars Rover Landing")

        checkShowOrNotshow()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = FragmentAdapter(supportFragmentManager, lifecycle)



        TabLayoutMediator(binding.tapLayout, binding.viewPager){tap, position->
            tap.text =tabLayoutArray[position]
        }.attach()

        binding.webView.getSettings().javaScriptEnabled = true
        // 자바스크립트인터페이스 연결
        // 이걸 통해 자바스크립트 내에서 자바함수에 접근할 수 있음.
        binding.webView.addJavascriptInterface(MyJavascriptInterface(this), "Android")
        // 페이지가 모두 로드되었을 때, 작업 정의
        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // 자바스크립트 인터페이스로 연결되어 있는 getHTML를 실행
                // 자바스크립트 기본 메소드로 html 소스를 통째로 지정해서 인자로 넘김
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
            }
        })
        //지정한 URL을 웹 뷰로 접근하기
        binding.webView.loadUrl("https://mars.nasa.gov/layout/embed/image/m20weather/")
    }

    class MyJavascriptInterface(val holder: MainActivity){
        @JavascriptInterface
        fun getHtml(html: String) {

            holder.ManufactureData(html)
//            Log.d(TAG, "getHtml: sourceff : ${holder.source}")
//            Log.d(TAG, "getHtml: 잊;ㅜㄴ상 천재")
//            val result = Jsoup.parse(holder.source)
//            val date = result.select("span.earthDate")
//            holder.vM.earthDate.value = date.text()
//            holder.vM.earthDate.value = result.select("span.marsDate").text()
//            holder.funciotn1()

        }
    }

    fun ManufactureData(html: String) {

        val result = Jsoup.parse(html)
        val sol = result.select("span.marsDate").text()
        val earthDate = result.select("span.earthDate").text()
        vM.earthDate.postValue(earthDate)
        vM.sol.postValue(sol)

        val resultlist = result.select("span.fahrenheit").text().split(" ")

        var Marstemper : String? = ""
        for(i in 0..resultlist.count()-1){
            if(i == 0){
                Marstemper = Marstemper!!.plus("High : ").plus(resultlist[i]).plus(" F").plus("\n")
            }
            else{
                Marstemper = Marstemper!!.plus("Low : ").plus(resultlist[i]).plus(" F")
            }
        }
        val weatherResult = result.select("div.item")

        val weatherList : ArrayList<Weather> = ArrayList()
        for (i in weatherResult){
            val html = Jsoup.parse(i.toString()).body()
            Log.d(TAG, "ManufactureData: htmlWeather ; $html")
            weatherList.add(Weather(
                html.select("span.dateSol").text(),
                html.select("span.dateUTC").text(),
                "High:${html.select("div.high").text().split("High:")[2]}" +
                        "\nLow:${html.select("div.low").text().split("Low")[2]}"
            ))
        }

        Log.d(TAG, "ManufactureData: weatherList : $weatherList")
        Log.d(TAG, "ManufactureData: item : $weatherResult")
        Log.d(TAG, "ManufactureData: Marstemper $Marstemper")
        vM.todayMarsWeather.postValue(Marstemper)
        vM.weatherList.postValue(weatherList)
        vM.earthDate.postValue(result.select("span.earthDate").text())
        vM.sol.postValue(result.select("span.marsDate").text())

        Log.d(TAG, "ManufactureData: vM.sol : vM.sol.value ${vM.sol.value}")
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