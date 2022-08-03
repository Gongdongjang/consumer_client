package com.example.consumer_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ContentClick extends AppCompatActivity {

    TextView content_title, content_context;
    ImageView content_photo;
    Button content_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_click);

        content_title = findViewById(R.id.content_title);
        content_context = findViewById(R.id.content_context);
        content_photo = findViewById(R.id.content_photo);
        content_link = findViewById(R.id.content_link_btn);

        Intent intent = getIntent();
        content_title.setText(intent.getStringExtra("content_title"));
        content_context.setText(intent.getStringExtra("content_context"));
        Picasso.get().load(intent.getStringExtra("content_photo")).into(content_photo);
        // link는 추후 연결
    }
}