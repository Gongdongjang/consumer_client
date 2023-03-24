package com.gdjang.consumer_client.notification;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.MainActivity;
import com.gdjang.consumer_client.R;

import com.gdjang.consumer_client.cart.CartListActivity;
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
import retrofit2.http.Body;
import retrofit2.http.POST;

interface NotiService {
    @POST("notification")
    Call<ResponseBody> getNotification(@Body JsonObject body);  //post user_id
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
        user_id = intent.getStringExtra("user_id");

        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);

        //noti정보 불러오기
        Call<ResponseBody> call = service.getNotification(body);
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
                        addNoti(notiArray.get(i).getAsJsonObject().get("notification_title").getAsString(),
                                notiArray.get(i).getAsJsonObject().get("notification_content").getAsString(),
                                notiArray.get(i).getAsJsonObject().get("notification_target").getAsString());
                        // #F7F7F7
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

        //상단바 뒤로가기
        ImageView gotoBack = findViewById(R.id.gotoBack);
        gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NotificationList.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NotificationList.this, CartListActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

    }

    private void firstInit() {
        mNotificationRecycler = findViewById(R.id.NotificationRecycler);
        mList = new ArrayList<>();
    }

    private void addNoti(String notiTitle, String notiContent, String target) {
        NotificationItem noti = new NotificationItem();
        noti.setNotiTitle(notiTitle);
        noti.setNotiContent(notiContent);
        noti.setTarget(target);
        mList.add(noti);
    }

    //뒤로가기 버튼 시 화면전환 (토큰 update 화면 안보이게)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationList.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }

}
