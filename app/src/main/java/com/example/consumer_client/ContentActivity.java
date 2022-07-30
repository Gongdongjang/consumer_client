package com.example.consumer_client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    ListView content_list;
    ContentListAdapter contentListAdapter;
    ArrayList<String> photo_urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        content_list = findViewById(R.id.content_listview);
        contentListAdapter = new ContentListAdapter(this, photo_urls);
        content_list.setAdapter(contentListAdapter);

        get_content_list();
    }

    void get_content_list() {
        // sample code for test
        for (int i = 0; i < 5; i++) {
            photo_urls.add("https://static.remove.bg/remove-bg-web/bf3af3e882eb04971b4492a1015ef7e77df29362/assets/start_remove-c851bdf8d3127a24e2d137a55b1b427378cd17385b01aec6e59d5d4b5f39d2ec.png");
        }
        contentListAdapter.notifyDataSetChanged();
    }
}