package com.example.consumer_client.review;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

public class ReviewActivity extends AppCompatActivity {
    String md_name, md_qty, md_fin_price;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        Intent intent = getIntent(); //intent 값 받기
        md_name = intent.getStringExtra("md_name");
        md_qty = intent.getStringExtra("md_qty");
        md_fin_price = intent.getStringExtra("md_fin_price");

        TextView JP_ProdName = (TextView) findViewById(R.id.JP_ProdName);
        TextView ClientOrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView ClientOrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);

        JP_ProdName.setText(md_name);
        ClientOrderCount.setText(md_qty);
        ClientOrderPrice.setText(md_fin_price);
    }
}
