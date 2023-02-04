package com.example.consumer_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

interface ContentService {
    @GET("content")
    Call<ResponseBody> get_content();

    @GET("content/banner")
    Call<ResponseBody> getBanner();
}

public class ContentActivity extends AppCompatActivity {
    String TAG = ContentActivity.class.getSimpleName();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ContentService contentService = retrofit.create(ContentService.class);
    JsonParser jsonParser = new JsonParser();

    ListView content_list;
    ContentListAdapter contentListAdapter;

    ViewPager2 bannerList;
    BannerListAdapter bannerListAdapter;
    ArrayList<String> bannerThumbnails = new ArrayList<>();

    ArrayList<String> content_thumbnail = new ArrayList<>();
    ArrayList<Integer> content_id = new ArrayList<>();
    ArrayList<String> content_title = new ArrayList<>();
    ArrayList<String> content_date = new ArrayList<>();
    ArrayList<String> content_context = new ArrayList<>();
    ArrayList<String> content_photo = new ArrayList<>();
    ArrayList<String> content_link = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        content_list = findViewById(R.id.content_listview);
        contentListAdapter = new ContentListAdapter(this, content_thumbnail, content_id, content_title, content_date, content_context, content_photo, content_link);
        content_list.setAdapter(contentListAdapter);

        bannerList = findViewById(R.id.content_banner_view);
        bannerListAdapter = new BannerListAdapter(this, bannerThumbnails);
        bannerList.setAdapter(bannerListAdapter);

        get_content_list();
        getBannerList();
    }

    void get_content_list() {
        Call<ResponseBody> call = contentService.get_content();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonArray res = (JsonArray) jsonParser.parse(response.body().string());
                        for (int i = 0; i < res.size(); i++) {
                            JsonObject jsonRes = (JsonObject) res.get(i);
                            String thumbnail_url = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_thumbnail").toString().replaceAll("\"", "");
                            content_thumbnail.add(thumbnail_url);

                            String photo_url = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_photo").toString().replaceAll("\"", "");
                            content_photo.add(photo_url);

                            content_id.add(jsonRes.get("content_id").getAsInt());
                            content_title.add(jsonRes.get("content_title").getAsString());
                            content_context.add(jsonRes.get("content_context").getAsString());
                            content_date.add(jsonRes.get("content_date").getAsString());
                            content_link.add(jsonRes.get("content_link").getAsString());
                           }
                        contentListAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void getBannerList() {
        Call<ResponseBody> call = contentService.getBanner();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        JsonArray data = res.getAsJsonArray("data");
                        for (int i = 0; i < 3; i++) {
                            JsonObject jsonObject = (JsonObject) data.get(i);
                            String thumbnailUrl = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_thumbnail").toString().replaceAll("\"", "");
                            bannerThumbnails.add(thumbnailUrl);
                        }
                        System.out.println("banner " + bannerThumbnails);
                        bannerListAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Log.d(TAG, "Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}