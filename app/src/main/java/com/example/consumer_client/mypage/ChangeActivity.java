package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.fragment.MyPage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface SNS_Service{
    @GET("/check_id")
    Call<ResponseBody> check_id(@Query("user_id") String user_id);
}

public class ChangeActivity extends AppCompatActivity {
    SNS_Service service;
    JsonParser jsonParser;
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    AccountSettingDialog accountSettingDialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_change);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SNS_Service.class);
        jsonParser = new JsonParser();
        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        //로그인 정보
        LinearLayout MyPage_MyLoginSetting = (LinearLayout) findViewById(R.id.MyPage_MyLoginSetting);
        // 소셜 확인 서버 통신

        Call<ResponseBody> call = service.check_id(user_id);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             try {
                                 JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                 String message = res.get("message").getAsString();
                                 if (message.equals("sns")){
                                     MyPage_MyLoginSetting.setVisibility(View.INVISIBLE);
                                 } else if (message.equals("not_sns")){
                                     MyPage_MyLoginSetting.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(ChangeActivity.this, LoginSettingActivity.class);
                                             intent.putExtra("user_id", user_id);
                                             startActivity(intent);
                                         }
                                     });
                                 } else {
                                     Toast.makeText(ChangeActivity.this, "아이디 오류 발생", Toast.LENGTH_SHORT).show();
                                 }

                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {

                         }
                     }
        );

        //회원 정보
        LinearLayout MyPage_MyAccountSetting = (LinearLayout) findViewById(R.id.MyPage_MyAccountSetting);
        MyPage_MyAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeActivity.this, AccountSettingActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
    }
}
