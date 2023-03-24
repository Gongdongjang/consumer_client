package com.gdjang.consumer_client.shopping_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.review.ReviewListAdapter;
import com.gdjang.consumer_client.review.ReviewListInfo;
import com.gdjang.consumer_client.review.ReviewMyDetailActivity;
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

interface ReviewListService {
    @POST("/reviewList")
    Call<ResponseBody> reviewList(@Body JsonObject body);
}

public class ReviewList extends AppCompatActivity {
    JsonParser jsonParser;
    ReviewListService service;
    JsonObject res;
    String user_id;
    JsonArray my_review_list;
    private RecyclerView mReviewListRecyclerView;
    private ReviewListAdapter mReviewListAdapter;
    private ArrayList<ReviewListInfo> mList;
    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_review);

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ReviewListService.class);
        jsonParser = new JsonParser();

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        Call<ResponseBody> call = service.reviewList(body);

        firstInit();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());

                    my_review_list = res.get("my_review_list").getAsJsonArray();

                    //어뎁터 적용
                    mReviewListAdapter = new ReviewListAdapter(mList);
                    mReviewListRecyclerView.setAdapter(mReviewListAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mReviewListRecyclerView.setLayoutManager(linearLayoutManager);

                    for (int i = 0; i < my_review_list.size(); i++) {

                        addReviewList(
                                user_id,
                                my_review_list.get(i).getAsJsonObject().get("order_id").getAsString(),
                                "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + my_review_list.get(i).getAsJsonObject().get("rvw_img1").getAsString(),
                                "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + my_review_list.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("store_name").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("md_name").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("rvw_content").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("rvw_rating").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("order_select_qty").getAsString(),
                                my_review_list.get(i).getAsJsonObject().get("pay_price").getAsString()
                        );
                    }

                    mReviewListAdapter.setOnItemClickListener(
                            new ReviewListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(ReviewList.this, ReviewMyDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("review_user_id", user_id);
                                    intent.putExtra("order_id", my_review_list.get(pos).getAsJsonObject().get("order_id").getAsString());
                                    intent.putExtra("mdimg_thumbnail", "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + my_review_list.get(pos).getAsJsonObject().get("mdimg_thumbnail").getAsString());
                                    intent.putExtra("store_name", my_review_list.get(pos).getAsJsonObject().get("store_name").getAsString());
                                    intent.putExtra("md_name",my_review_list.get(pos).getAsJsonObject().get("md_name").getAsString());
                                    intent.putExtra("md_qty", my_review_list.get(pos).getAsJsonObject().get("order_select_qty").getAsString());
                                    intent.putExtra("md_fin_price", my_review_list.get(pos).getAsJsonObject().get("pay_price").getAsString());
                                    intent.putExtra("rvw_content", my_review_list.get(pos).getAsJsonObject().get("rvw_content").getAsString());
                                    intent.putExtra("rvw_rating", my_review_list.get(pos).getAsJsonObject().get("rvw_rating").getAsString());
                                    intent.putExtra("rvw_img1", my_review_list.get(pos).getAsJsonObject().get("rvw_img1").getAsString());
                                    intent.putExtra("rvw_img2", my_review_list.get(pos).getAsJsonObject().get("rvw_img2").getAsString());
                                    intent.putExtra("rvw_img3", my_review_list.get(pos).getAsJsonObject().get("rvw_img3").getAsString());
                                    startActivity(intent);
                                }
                            }
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "전체스토어 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
            }
        });
    }

    public void firstInit() {
        mReviewListRecyclerView = findViewById(R.id.totalReviewListView);
        mList = new ArrayList<>();
    }

    public void addReviewList(String userId, String orderId, String reviewImg, String mdImgView, String storeName, String mdName,
                              String reviewContent, String rvw_rating,String mdQty, String mdPrice) {
        ReviewListInfo reviewList = new ReviewListInfo();
        reviewList.setUserId(userId);
        reviewList.setOrderId(orderId);
        reviewList.setReviewImg1(reviewImg);
        reviewList.setStoreProdImgView(mdImgView);
        reviewList.setStoreName(storeName);
        reviewList.setMdName(mdName);
        reviewList.setReviewContent(reviewContent);
        reviewList.setRvw_rating(rvw_rating);
        reviewList.setMdQty(mdQty);
        reviewList.setMdPrice(mdPrice);
        mList.add(reviewList);
    }
}

