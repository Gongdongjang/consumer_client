package com.gdjang.consumer_client.content;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import retrofit2.http.GET;

interface ContentService {
    @GET("content")
    Call<ResponseBody> get_content();

    @GET("content/banner")
    Call<ResponseBody> getBanner();
}

public class ContentActivity extends AppCompatActivity {
    String TAG = ContentActivity.class.getSimpleName();
    JsonParser jsonParser = new JsonParser();

    //콘텐츠 어뎁터 선언
    private RecyclerView mContentRecyclerView;
    ContentListAdapter contentListAdapter;
    private ArrayList<ContentItem> mList = new ArrayList<>();

    String user_id, standard_address;
    ViewPager2 bannerList;
    BannerListAdapter bannerListAdapter;
    ArrayList<String> bannerThumbnails = new ArrayList<>();
    ArrayList<String> bannerIds = new ArrayList<>();
    ArrayList<String> bannerTitles = new ArrayList<>();
    ArrayList<String> bannerMainPhotos = new ArrayList<>();
    ArrayList<String> bannerPhotos = new ArrayList<>();
    ArrayList<String> bannerContexts = new ArrayList<>();
    ArrayList<String> bannerMdId1 = new ArrayList<>();
    ArrayList<String> bannerMdId2 = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ContentService contentService = retrofit.create(ContentService.class);

        Intent intent;
        intent = getIntent();

        user_id = intent.getStringExtra("user_id");
        standard_address = intent.getStringExtra("standard_address");

        mContentRecyclerView = findViewById(R.id.content_listview);
        mList = new ArrayList<>();

        bannerList = findViewById(R.id.content_banner_view);
        bannerListAdapter = new BannerListAdapter(this,
                user_id, standard_address, bannerThumbnails,
                bannerIds, bannerTitles, bannerMainPhotos, bannerPhotos,
                bannerContexts, bannerMdId1, bannerMdId2);
        bannerList.setAdapter(bannerListAdapter);



        //뒤로가기
        ImageView gotoBack = findViewById(R.id.gotoBack);
        gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContentActivity.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //콘텐츠 띄우기
        Call<ResponseBody> callCon = contentService.get_content();
        callCon.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonArray res = (JsonArray) jsonParser.parse(response.body().string());

                        //어뎁터 적용
                        contentListAdapter = new ContentListAdapter(mList);
                        mContentRecyclerView.setAdapter(contentListAdapter);

                        //세로로 세팅
                        linearLayoutManager = new LinearLayoutManager(ContentActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mContentRecyclerView.setLayoutManager(linearLayoutManager);

                        for (int i = 0; i < res.size(); i++) {
                            JsonObject jsonRes = (JsonObject) res.get(i);
                            addContent(
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_thumbnail").toString().replaceAll("\"", ""),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_photo").toString().replaceAll("\"", ""),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonRes.get("content_main").toString().replaceAll("\"", ""),
                                    jsonRes.get("content_id").getAsInt(),
                                    jsonRes.get("content_title").getAsString(),
                                    jsonRes.get("content_context").getAsString(),
                                    jsonRes.get("content_md_id1").isJsonNull() ? "null" : jsonRes.get("content_md_id1").getAsString(),
                                    jsonRes.get("content_md_id2").isJsonNull() ? "null" : jsonRes.get("content_md_id2").getAsString()
                            );
                        }

                        //콘텐츠리스트 리사이클러뷰 누르면 나오는
                        contentListAdapter.setOnItemClickListener(
                                new ContentListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(ContentActivity.this, ContentDetailActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("standard_address", standard_address);
                                        intent.putExtra("content_id", mList.get(pos).getContent_id());
                                        intent.putExtra("content_title", mList.get(pos).getContent_title());
                                        intent.putExtra("content_photo", mList.get(pos).getContent_photo());
                                        intent.putExtra("contentMainPhoto", mList.get(pos).getContentMainPhotos());
                                        intent.putExtra("content_context", mList.get(pos).getContent_context());
                                        intent.putExtra("content_md_id1", mList.get(pos).getContent_md_id1());
                                        intent.putExtra("content_md_id2", mList.get(pos).getContent_md_id2());
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

        //배너 띄우기
        Call<ResponseBody> callBan = contentService.getBanner();
        callBan.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        JsonArray data = res.getAsJsonArray("data");
                        for (int i = 0; i < data.size(); i++) {
                            Log.d("data.size()", String.valueOf(data.size()));
                            JsonObject jsonObject = (JsonObject) data.get(i);
                            String thumbnailUrl = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_thumbnail").getAsString();
                            bannerThumbnails.add(thumbnailUrl);
                            bannerIds.add(jsonObject.get("content_id").getAsString());
                            bannerTitles.add(jsonObject.get("content_title").getAsString());
                            bannerContexts.add(jsonObject.get("content_context").getAsString());
                            bannerPhotos.add("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_photo").getAsString());
                            bannerMainPhotos.add("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonObject.get("content_main").getAsString());
                            if(!jsonObject.get("content_md_id1").isJsonNull()){
                                bannerMdId1.add(jsonObject.get("content_md_id1").getAsString());
                            }
                            else{
                                bannerMdId1.add("null");
                            }
                            if(!jsonObject.get("content_md_id2").isJsonNull()){
                                bannerMdId2.add(jsonObject.get("content_md_id2").getAsString());
                            }else{
                                bannerMdId2.add("null");
                            }
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

    //콘텐츠 어뎁터 연결
    public void addContent(String thumbnailUrl, String photo_url, String mainPhotoUrl, int content_id, String content_title, String content_context, String content_md_id1, String content_md_id2) {
        ContentItem item = new ContentItem();

        item.setContent_thumbnail(thumbnailUrl);
        item.setContent_photo(photo_url);
        item.setContentMainPhotos(mainPhotoUrl);
        item.setContent_id(content_id);
        item.setContent_title(content_title);
        item.setContent_context(content_context);
        item.setContent_md_id1(content_md_id1);
        item.setContent_md_id2(content_md_id2);

        mList.add(item);
    }

}