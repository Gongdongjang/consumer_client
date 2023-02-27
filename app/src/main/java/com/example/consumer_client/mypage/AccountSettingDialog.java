package com.example.consumer_client.mypage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.fragment.MyPage;

public class AccountSettingDialog extends Dialog {
    AccountSettingDialog accountSettingDialog;

    public AccountSettingDialog(@NonNull Context context, String user_id) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mypage_dialog_userinfo);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        accountSettingDialog = this;

        Button mpd_cancel = findViewById(R.id.MPD_Cancel);
        mpd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountSettingDialog.dismiss();
            }
        });

        MyPage myPage = new MyPage();
        Button mpd_exit = findViewById(R.id.MPD_Exit);
        mpd_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("user_id", user_id);
                myPage.setArguments(bundle);
                
            }
        });
    }
}
