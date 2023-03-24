package com.gdjang.consumer_client.review;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.gdjang.consumer_client.R;

public class ReviewDeleteDialog extends Dialog {
    private Button cancelClick;

    public ReviewDeleteDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.dialog_review_delete);

        cancelClick = findViewById(R.id.cancelBtn);
        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
