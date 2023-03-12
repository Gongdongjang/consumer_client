package com.example.consumer_client.review;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

import androidx.annotation.Nullable;


public class ReviewDetailActivity extends AppCompatActivity {
    String user_id, order_id, md_img, store_name, md_name, md_qty, md_price, rvw_content, rvw_rating, rvw_img1, rvw_img2, rvw_img3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        order_id = intent.getStringExtra("order_id");
        md_img = intent.getStringExtra("md_img");
        store_name = intent.getStringExtra("store_name");
        md_name = intent.getStringExtra("md_name");
        md_qty = intent.getStringExtra("md_qty");
        md_price = intent.getStringExtra("md_price");
        rvw_content = intent.getStringExtra("rvw_content");
        rvw_rating = intent.getStringExtra("rvw_rating");
        rvw_img1 = intent.getStringExtra("rvw_img1");
        rvw_img2 = intent.getStringExtra("rvw_img2");
        rvw_img3 = intent.getStringExtra("rvw_img3");

        TextView reviewDetailName = findViewById(R.id.reviewDetailName);
        ImageView ReviewDetailProdImg = findViewById(R.id.ReviewDetailProdImg);
        TextView ReviewDetailStoreName = findViewById(R.id.ReviewDetailStoreName);
        TextView ReviewDetailProdName = findViewById(R.id.ReviewDetailProdName);
        TextView ReviewDetailOrderCount = findViewById(R.id.ReviewDetailOrderCount);
        TextView ReviewDetailOrderPrice = findViewById(R.id.ReviewDetailOrderPrice);
        ImageView Star_1 = findViewById(R.id.Star_1);
        ImageView Star_2 = findViewById(R.id.Star_2);
        ImageView Star_3 = findViewById(R.id.Star_3);
        ImageView Star_4 = findViewById(R.id.Star_4);
        ImageView Star_5 = findViewById(R.id.Star_5);
    }
}
