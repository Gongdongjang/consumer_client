package com.example.consumer_client.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;

public class StartActivity extends AppCompatActivity {
    Button startGdjang; //회원가입 끝나고 홈화면 바로가기? or 로그인하러가기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startGdjang=findViewById(R.id.btn_startGdjang);
        startGdjang.setOnClickListener(v -> {
            Intent intent=new Intent(this, MainActivity.class); //로그인화면or 메인화면으로 이동
            startActivity(intent);
        });


    }
}
