package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.fragment.MyPage;

public class ChangeActivity extends AppCompatActivity {
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    AccountSettingDialog accountSettingDialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_change);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        //로그인 정보
        LinearLayout MyPage_MyLoginSetting = (LinearLayout) findViewById(R.id.MyPage_MyLoginSetting);
        MyPage_MyLoginSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeActivity.this, LoginSettingActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

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
