package com.example.consumer_client;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.order.OrderDetailActivity;
import com.example.consumer_client.review.ReviewActivity;

public class ReviewDialog extends Dialog {
    private Button cancelClick, stopBtn;

    public ReviewDialog(@NonNull Context context){ //, String user_id, String store_loc, String store_name, String md_name, String order_id, String mdimg_thumbnail) {
        super(context);
        setContentView(R.layout.activity_review_cancel_popup);

        cancelClick = findViewById(R.id.cancelBtn);
        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        stopBtn = findViewById(R.id.stopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, OrderDetailActivity.class);
//                intent.putExtra("user_id", user_id);
//                intent.putExtra("order_id", order_id);
//                intent.putExtra("md_name", md_name);
//                intent.putExtra("store_loc", store_loc);
//                intent.putExtra("store_name", store_name);
//                intent.putExtra("mdimg_thumbnail", mdimg_thumbnail);
//                onStart();
            }
        });
    }
}
