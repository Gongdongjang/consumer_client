package com.example.consumer_client;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        Button reviewBtn = (Button) findViewById(R.id.OrderReviewBtn);
        reviewBtn.setEnabled(false);

        //픽업완료 상태면 리뷰 버튼 활성화
        String state = "";
        if (state.equals("픽업완료")){
            reviewBtn = (Button) findViewById(R.id.OrderReviewBtn);
            reviewBtn.setEnabled(true);
        }
    }
}
