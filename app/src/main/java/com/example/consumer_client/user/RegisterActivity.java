package com.example.consumer_client.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

public class RegisterActivity extends AppCompatActivity {
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //회원가입 버튼
        signup = findViewById(R.id.signupbutton);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        });
    }

}
