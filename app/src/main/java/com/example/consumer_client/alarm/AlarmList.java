package com.example.consumer_client.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

public class AlarmList extends AppCompatActivity {

    TextView AlarmTitle, AlarmBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmlist);

        String title = "";
        String body = "";
        try{
            Intent psuhIntent = getIntent();
            Log.i("#########Alarm title: ",psuhIntent.getExtras().get("title").toString());
            title = psuhIntent.getExtras().get("title").toString();
            body = psuhIntent.getExtras().get("body").toString();
        }

        catch (Exception e){
            title = "";
            body = "";
            Log.i("#########Alarm catch: ", title);
        }

    }
}
