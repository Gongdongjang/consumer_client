package com.gdjang.consumer_client.review;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;

import com.gdjang.consumer_client.R;

public class ReviewRegisterDialog extends Dialog {
    private Button cancelClick;

    public ReviewRegisterDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.dialog_review_register);

        cancelClick = findViewById(R.id.cancelBtn);
        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
