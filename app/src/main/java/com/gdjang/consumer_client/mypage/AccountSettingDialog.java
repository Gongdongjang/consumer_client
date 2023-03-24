package com.gdjang.consumer_client.mypage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.gdjang.consumer_client.R;

import org.jetbrains.annotations.NotNull;

public class AccountSettingDialog extends Dialog {
    AccountSettingDialog accountSettingDialog;

    public AccountSettingDialog(@NotNull Context context, String user_id) {
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

        Button mpd_exit = findViewById(R.id.MPD_Exit);
        mpd_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountSettingDialog.dismiss();
                ((AccountSettingActivity)context).finish();
            }
        });
    }
}
