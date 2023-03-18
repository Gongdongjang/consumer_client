package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface NoticeService{
    @GET("/notice")
    Call<ResponseBody> getNotice();
}

public class AboutGDJActivity extends AppCompatActivity {
    Context mContext;
    String user_id;
    NoticeService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray notice_list;
    String notice_title, notice_context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_gdjang);
        mContext = this;
        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NoticeService.class);
        jsonParser = new JsonParser();

        RecyclerView recyclerView = findViewById(R.id.Notice_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<ExpandableAdapter.Item> data = new ArrayList<>();

        Call<ResponseBody> call = service.getNotice();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res = (JsonObject) jsonParser.parse(response.body().string());
                        notice_list = res.get("notice_list").getAsJsonArray();
                        for (int i = 0; i <notice_list.size(); i++){
                            notice_title = notice_list.get(i).getAsJsonObject().get("notice_title").getAsString();
                            notice_context = notice_list.get(i).getAsJsonObject().get("notice_context").getAsString();
                            ExpandableAdapter.Item places = new ExpandableAdapter.Item(ExpandableAdapter.HEADER, notice_title);
                            places.invisibleChildren = new ArrayList<>();
                            places.invisibleChildren.add(new ExpandableAdapter.Item(ExpandableAdapter.CHILD, notice_context));
                            data.add(places);
                            recyclerView.setAdapter(new ExpandableAdapter(data));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Notice", "onFailure: e " + t.getMessage());
            }
        });

        Button company_btn = findViewById(R.id.Company_Btn);
        company_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutGDJActivity.this, AboutCompanyActivity.class);
                startActivity(intent);
            }
        });


        Button OpenKakao_Btn = (Button) findViewById(R.id.OpenKakao_Btn);
        OpenKakao_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://open.kakao.com/o/suhyQ78e"));
                startActivity(intent);
            }
        });
    }
}
