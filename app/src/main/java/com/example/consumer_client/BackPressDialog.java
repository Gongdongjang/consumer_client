package com.example.consumer_client;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

public class BackPressDialog extends Dialog {
    BackPressDialog backPressDialog;

    public BackPressDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_backpress);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        backPressDialog = this;

        Button bp_cancel = findViewById(R.id.BP_Cancel);
        bp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressDialog.dismiss();
            }
        });

        Button bp = findViewById(R.id.BP);
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).moveTaskToBack(true);
                ((MainActivity)context).finish();
            }
        });
    }
}