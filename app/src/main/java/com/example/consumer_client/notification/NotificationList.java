package com.example.consumer_client.notification;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface NotiService {
    @GET("notification")
    Call<ResponseBody> getNotification();  //post user_id
}

public class NotificationList  extends AppCompatActivity {
    NotiService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray notiArray;

    private RecyclerView mNotificationRecycler;
    private ArrayList<NotificationItem> mList;
    private NotificationAdapter notificationAdapter;
    Context mContext;

    String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NotiService.class);
        jsonParser = new JsonParser();

        firstInit();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");

        //noti정보 불러오기
        Call<ResponseBody> call= service.getNotification();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    notiArray = res.get("noti_result").getAsJsonArray();  //json배열

                    //어뎁터 적용
                    notificationAdapter = new NotificationAdapter(mList);
                    mNotificationRecycler.setAdapter(notificationAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mNotificationRecycler.setLayoutManager(linearLayoutManager);

                    for (int i = 0; i < notiArray.size(); i++) {
                        addNoti(notiArray.get(i).getAsJsonObject().get("notification_title").getAsString(), notiArray.get(i).getAsJsonObject().get("notification_content").getAsString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(NotificationList.this, "알림정보 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
            }
        });
}

    private void firstInit() {
        mNotificationRecycler = findViewById(R.id.NotificationRecycler);
        mList = new ArrayList<>();
    }

    private void addNoti(String notiTitle, String notiContent) {
        NotificationItem noti = new NotificationItem();
        noti.setNotiTitle(notiTitle);
        noti.setNotiContent(notiContent);
        mList.add(noti);
    }
}