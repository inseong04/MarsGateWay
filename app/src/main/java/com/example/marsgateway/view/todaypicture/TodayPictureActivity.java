package com.example.marsgateway.view.todaypicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.marsgateway.R;
import com.example.marsgateway.SecretKeyClass;
import com.example.marsgateway.data.api.NasaService;
import com.example.marsgateway.data.api.NasaServiceImpl;
import com.example.marsgateway.model.PictureData;
import com.example.marsgateway.view.MainActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodayPictureActivity extends AppCompatActivity {

    TextView titleTv;
    TextView explanationTv;
    ImageView pictureImg;
    Button quitBtn;
    PictureData pictureData;
    Toolbar toolbar;
    private static final String CHECK_USER_DATE = "FIRST_MEET";
    private static final String TAG = "TodayPictureActivity";
    private static final String CHECK_EVNET_POPUP = "CHECK_EVNET_POPUP";
    private static final String CHECK_EVNET_POPUP_NO = "CHECK_EVNET_POPUP_NO";
    private static final String CHECK_EVNET_POPUP_YES = "CHECK_EVNET_POPUP_YES";
    private String event_popup_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_picture);

        titleTv = findViewById(R.id.titleTv);
        explanationTv = findViewById(R.id.explanationTv);
        pictureImg = findViewById(R.id.pictureImg);
        quitBtn = findViewById(R.id.quitBtn);
        explanationTv.setMovementMethod(new ScrollingMovementMethod());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(NasaService.class);
        NasaService service = retrofit.create(NasaService.class);

        String api_key = SecretKeyClass.api_key;
        Call<PictureData> call = service.getPicture(api_key);
        call.enqueue(new Callback<PictureData>() {
            @Override
            public void onResponse(Call<PictureData> call, Response<PictureData> response) {
                if (response.isSuccessful()) {
                    PictureData pictureData = response.body();
                    TodayPictureActivity.this.pictureData = pictureData;
                    titleTv.setText(pictureData.title);
                    Glide.with(TodayPictureActivity.this)
                            .load(pictureData.url)
                            .into(pictureImg);
                    explanationTv.setText(pictureData.explanation);
                } else {
                    Log.e("error", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<PictureData> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // EVENT POP
    public void checkShowOrNotshow(){
        String chk_today =null;
        String pref_user_date= null;

        SharedPreferences pref = getApplicationContext().getSharedPreferences("event_popup", MODE_PRIVATE);
        chk_today = pref.getString(CHECK_EVNET_POPUP,CHECK_EVNET_POPUP_NO);
        pref_user_date = pref.getString(CHECK_USER_DATE,"FIRST_MEET");

        if(!pref_user_date.equals("FIRST_MEET")){
            int my_result =compareUserdateWithToday(pref_user_date);
            //오늘 접속함
            if(my_result == 0){
                Log.i(TAG,"## 이벤트 팝업 : 오늘 접속함");

                //하루 안보기 클릭
                if(chk_today.equals(CHECK_EVNET_POPUP_YES)){
                    Log.i(TAG,"## 이벤트 팝업 : 하루 안보기 클릭");
                }
                //하루 안보기 클릭 안했음
                else if(chk_today.equals(CHECK_EVNET_POPUP_NO)){
                    Log.i(TAG,"## 이벤트 팝업 : 하루 안보기 클릭 안했음");
                }
            }
            //어제 접속함
            else if(my_result < 0){
                Log.i(TAG,"## 이벤트 팝업 : 어제 접속함");
                getApplication().getSharedPreferences("event_popup",MODE_PRIVATE).edit().putString(CHECK_EVNET_POPUP,CHECK_EVNET_POPUP_NO).apply();
                pref.edit().putString(CHECK_USER_DATE,getToday()).apply();
            }
            //미래에서 왔을일은 없으니 에러
            else{
                Log.i(TAG,"## 이벤트 팝업 : 미래에서 왔을일은 없으니 에러");
                getApplication().getSharedPreferences("event_popup",MODE_PRIVATE).edit().putString(CHECK_EVNET_POPUP,CHECK_EVNET_POPUP_NO).apply();
            }
        } else {
            Log.i(TAG,"## 이벤트 팝업 : 앱을 처음 깔음");
            pref.edit().putString(CHECK_USER_DATE,getToday()).apply();
        }

    }

    // 미래 : int_calDateDays > 0 양수
    // 현재 : int_calDateDays == 0 같을때
    // 과거 : int_calDateDays < 0 음수
    private int compareUserdateWithToday(String user_date) {
        Integer int_calDateDays =null;
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();

            Date FirstDate = format.parse(user_date);
            Date SecondDate = format.parse(getToday());// 오늘 날짜를 yyyy-MM-dd포맷 String 값

            long calDate = FirstDate.getTime() - SecondDate.getTime();
            Long calDateDays = calDate / ( 24*60*60*1000);
            int_calDateDays = (calDateDays != null) ? calDateDays.intValue() : -1;//long to int
        }
        catch(ParseException e) { e.printStackTrace(); }
        return int_calDateDays;
    }


    private static String getToday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        return format.format(cal.getTime());
    }

    // start NewHttpGetRequest.class
    private class NewHttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            HttpsURLConnection urlConnection= null;
            java.net.URL url = null;
            String response = "";
            try {
                event_popup_result=null;
                url  = new java.net.URL(params[0]);
                urlConnection=(HttpsURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inStream = null;
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp = "";
                while((temp = bReader.readLine()) != null){
                    //Parse data
                    response += temp;
                }
                bReader.close();
                inStream.close();
                urlConnection.disconnect();
                event_popup_result = response;
                Log.i("??","## response :" +response);
            } catch (FileNotFoundException no_file_exception){
                event_popup_result ="FileNotFoundException";
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("??","## Exception :" +e.toString());
                event_popup_result = "otherException";
            }
            return event_popup_result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //파일 없을 경우 처리
            if(event_popup_result.contains("FileNotFoundException")){
                Toast.makeText(TodayPictureActivity.this,event_popup_result, Toast.LENGTH_SHORT).show();
            }
            //다른 익셉션일 경우 처리
            else if(event_popup_result.contains("otherException")){
                Toast.makeText(TodayPictureActivity.this,event_popup_result, Toast.LENGTH_SHORT).show();
            }
            // 통신이 성공했을때
            else{
                checkShowOrNotshow();
            }

            Log.i("??","## onPostExecute" +s);
        }
    }// end NewHttpGetRequest.class

}