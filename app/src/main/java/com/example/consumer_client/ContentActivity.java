package com.example.consumer_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.consumer_client.review.ReviewListAdapter;
import com.example.consumer_client.review.ReviewListInfo;
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
import retrofit2.http.Body;
import retrofit2.http.GET;

interface ContentService {
    @GET("content")
    Call<ResponseBody> get_content();

    @GET("content/banner")
    Call<ResponseBody> getBanner();
}

public class ContentActivity extends AppCompatActivity {
    String TAG = ContentActivity.class.getSimpleName();
    ContentListAdapter contentListAdapter;
    JsonParser jsonParser = new JsonParser();
    private ArrayList<ContentItem> mList = new ArrayList<>();
    private RecyclerView mContentRecyclerView;
    String user_id;
    ViewPager2 bannerList;
    BannerListAdapter bannerListAdapter;
    ArrayList<String> bannerThumbnails = new ArrayList<>();
    ArrayList<String> bannerIds = new ArrayList<>();
    ArrayList<String> bannerTitles = new ArrayList<>();
    ArrayList<String> bannerMainPhotos = new ArrayList<>();
    ArrayList<String> bannerPhotos = new ArrayList<>();
    ArrayList<String> bannerContexts = new ArrayList<>();
    ArrayList<String> bannerDates = new ArrayList<>();
    ArrayList<String> bannerLinks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContentService contentService = retrofit.create(ContentService.class);

        mContentRecyclerView = findViewById(R.id.content_listview);
        mList = new ArrayList<>();

        bannerList = findViewById(R.id.content_banner_view);
        bannerListAdapter = new BannerListAdapter(this, bannerThumbnails,
                bannerIds, bannerTitles, bannerMainPhotos, bannerPhotos,
                bannerContexts, bannerLinks, bannerDates);
        bannerList.setAdapter(bannerListAdapter);

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);

        Call<ResponseBody> callCon = contentService.get_content();
        callCon.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonArray res = (JsonArray) jsonParser.parse(response.body().string());

                        for (int i = 0; i < res.size(); i++) {
                            JsonObject jsonRes = (JsonObject) res.get(i);
                            addContent(
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_thumbnail").toString().replaceAll("\"", ""),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_photo").toString().replaceAll("\"", ""),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_main").toString().replaceAll("\"", ""),
                                    jsonRes.get("content_id").getAsInt(),
                                    jsonRes.get("content_title").getAsString(),
                                    jsonRes.get("content_context").getAsString(),
                                    jsonRes.get("content_date").getAsString(),
                                    jsonRes.get("content_link").getAsString()
                            );
                        }

                        //어뎁터 적용
                        contentListAdapter = new ContentListAdapter(mList);
                        mContentRecyclerView.setAdapter(contentListAdapter);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ContentActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mContentRecyclerView.setLayoutManager(linearLayoutManager);

                        //메인콘텐츠리스트 리사이클러뷰 누르면 나오는
                        contentListAdapter.setOnItemClickListener(
                                new ContentListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(ContentActivity.this, ContentDetailActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        startActivity(intent);
                                    }
                                }
                        );

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

        Call<ResponseBody> callBan = contentService.getBanner();
        callBan.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        JsonArray data = res.getAsJsonArray("data");
                        for (int i = 0; i < 3; i++) {
                            JsonObject jsonObject = (JsonObject) data.get(i);
                            String thumbnailUrl = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_thumbnail").getAsString();
                            bannerThumbnails.add(thumbnailUrl);
                            bannerIds.add(jsonObject.get("content_id").getAsString());
                            bannerTitles.add(jsonObject.get("content_title").getAsString());
                            bannerContexts.add(jsonObject.get("content_context").getAsString());
                            bannerPhotos.add("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_photo").getAsString());
                            bannerMainPhotos.add("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_main").getAsString());
                            bannerLinks.add(jsonObject.get("content_link").getAsString());
                            bannerDates.add(jsonObject.get("upload_date").getAsString());
                        }
                        System.out.println("banner " + bannerThumbnails);
                        bannerListAdapter.notifyDataSetChanged();
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
    public void addContent(String thumbnailUrl, String photo_url, String mainPhotoUrl, int content_id, String content_title, String content_context, String content_date, String content_link) {
        ContentItem item = new ContentItem();

        item.setContent_thumbnail(thumbnailUrl);
        item.setContent_photo(photo_url);
        item.setContentMainPhotos(mainPhotoUrl);
        item.setContent_id(content_id);
        item.setContent_title(content_title);
        item.setContent_context(content_context);
        item.setContent_date(content_date);
        item.setContent_link(content_link);

//        mContentList.add(item);
    }
}