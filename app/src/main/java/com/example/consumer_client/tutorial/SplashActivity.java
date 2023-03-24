package com.example.consumer_client.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.user.IntegratedLoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent= new Intent(SplashActivity.this, IntegratedLoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);    //스플래시화면 2초 후 로그인화면으로.
    }
}
