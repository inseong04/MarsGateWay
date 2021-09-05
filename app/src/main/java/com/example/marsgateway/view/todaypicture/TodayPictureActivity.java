package com.example.marsgateway.view.todaypicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.marsgateway.R;
import com.example.marsgateway.SecretKeyClass;
import com.example.marsgateway.data.api.NasaService;
import com.example.marsgateway.data.api.NasaServiceImpl;
import com.example.marsgateway.model.PictureData;
import com.example.marsgateway.view.MainActivity;

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
    Context context;
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

//        quitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(TodayPictureActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });
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

}