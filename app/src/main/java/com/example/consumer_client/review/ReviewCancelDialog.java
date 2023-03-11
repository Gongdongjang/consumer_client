package com.example.consumer_client.review;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.consumer_client.R;

public class ReviewCancelDialog extends Dialog {
    private Button cancelClick;

    public ReviewCancelDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.dialog_review_cancel);

        cancelClick = findViewById(R.id.cancelBtn);
        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
