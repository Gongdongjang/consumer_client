package com.example.consumer_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ContentDetailActivity extends AppCompatActivity {

    TextView content_title, content_context, contentDate;
    ImageView content_photo, contentMainPhoto;
    Button content_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_click);

        content_title = findViewById(R.id.content_title);
        content_context = findViewById(R.id.content_context);
        contentMainPhoto = findViewById(R.id.content_main_photo);
        content_photo = findViewById(R.id.content_photo);
        content_link = findViewById(R.id.content_link_btn);
        contentDate = findViewById(R.id.content_date);

        Intent intent = getIntent();
        content_title.setText(intent.getStringExtra("content_title"));
        content_context.setText(intent.getStringExtra("content_context"));
        contentDate.setText(Objects.requireNonNull(intent.getStringExtra("contentDate")).split("T")[0]);
        Picasso.get().load(intent.getStringExtra("content_photo")).into(content_photo);
        Picasso.get().load(intent.getStringExtra("contentMainPhoto")).into(contentMainPhoto);
        // TODO: 관련된 공동구매 연결
    }
}