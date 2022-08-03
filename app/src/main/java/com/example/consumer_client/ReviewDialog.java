package com.example.consumer_client;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.consumer_client.fragment.Home;

public class ReviewDialog extends Dialog {
    private TextView txt_contents;
    private Button cancelClick;
    private Button registerClick;

    public ReviewDialog(@NonNull Context context, String contents){
        super(context);
        setContentView(R.layout.activity_review_cancel_popup);

        txt_contents = findViewById(R.id.PopupReviewTxt);
        txt_contents.setText(contents);
        cancelClick = findViewById(R.id.cancelBtn);
        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        registerClick = findViewById(R.id.registerBtn);
        registerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
