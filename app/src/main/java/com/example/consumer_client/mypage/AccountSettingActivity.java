package com.example.consumer_client.mypage;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.fragment.MyPage;

public class AccountSettingActivity extends AppCompatActivity {
    Context mContext;
    String user_id;
    AccountSettingDialog accountSettingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit_userinfo);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");

        Button cancelBtn = findViewById(R.id.CancelBtn_MP);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountSettingDialog = new AccountSettingDialog(mContext, user_id);
                accountSettingDialog.show();
            }
        });

        Button changeBtn = findViewById(R.id.ConfirmBtn_MP);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountSettingActivity.this, MyPage.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
    }
}
