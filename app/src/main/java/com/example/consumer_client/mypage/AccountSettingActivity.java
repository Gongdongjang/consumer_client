package com.example.consumer_client.mypage;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.consumer_client.R;
import com.example.consumer_client.fragment.MyPage;

public class AccountSettingActivity extends AppCompatActivity {
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    AccountSettingDialog accountSettingDialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit_userinfo);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

//        linearLayout = (LinearLayout) findViewById(R.id.Activity_UserInfo);

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
                // 변경 프로세스

                // 뒤로가기
                finish();

                // 액티비티 -> 프래그먼트
//                FragmentManager manager = getSupportFragmentManager();
//                FragmentTransaction transaction = manager.beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("user_id", user_id);
//                MyPage myPage = new MyPage();
//                myPage.setArguments(bundle);
//                transaction.add(R.id.Fragment_MyPage, myPage).commit();
//                linearLayout.setVisibility(View.GONE);

//                Intent intent = new Intent(AccountSettingActivity.this, MyPage.class);
//                intent.putExtra("user_id", user_id);
//                Log.d("@@@@@@@@@@@@@@@@@user_id ",user_id);
//                startActivity(intent);
            }
        });
    }
}
