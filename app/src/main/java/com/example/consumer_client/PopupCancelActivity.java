package com.example.consumer_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.fragment.Home;

public class PopupCancelActivity extends AppCompatActivity {
    TextView txtText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 제거
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review_cancel_popup);
    }
    public void mOnCancel(View v){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    public void mOnRegister(View v){
        //등록하는 함수 있어야함.

        finish();
    }

    public boolean onTouchEvent(MotionEvent event){
        //바깥레이어 클릭 시 닫히지 않게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    public void onBackPressed(){
        return;
    }
}
