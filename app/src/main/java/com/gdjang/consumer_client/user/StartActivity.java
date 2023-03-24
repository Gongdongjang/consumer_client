package com.gdjang.consumer_client.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.tutorial.TutorialActivity;

import java.util.Objects;

public class StartActivity extends AppCompatActivity {
    Button startGdjang; //회원가입 끝나고 홈화면 바로가기? or 로그인하러가기
    String sns_login, name;
    TextView reg_nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        Intent intent = getIntent(); //intent 값 받기
        sns_login=intent.getStringExtra("sns_login");
        name=intent.getStringExtra("name");

        reg_nickname=findViewById(R.id.reg_nickname);
        reg_nickname.setText(name);

        startGdjang=findViewById(R.id.startGdjangBtn);
        startGdjang.setOnClickListener(v -> {
            if(Objects.equals(sns_login, "yes")){
                //sns로그인
                Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                i.putExtra("user_id", intent.getStringExtra("user_id"));
                startActivity(i);
            }else{
                //기본회원가입
                Intent i=new Intent(this, IntegratedLoginActivity.class); //자동로그인 되어 메인화면으로 이동
                startActivity(i);
            }

        });


    }
}
