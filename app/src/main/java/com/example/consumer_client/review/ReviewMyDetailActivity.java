package com.example.consumer_client.review;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.shopping_info.ReviewList;
import com.example.consumer_client.shopping_info.ShoppingInfoActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ReviewDeleteService {
    @POST("review/delete")
    Call<ResponseBody> reviewDelete(@Body JsonObject body);
}

public class ReviewMyDetailActivity extends AppCompatActivity {
    String user_id, user_name, review_user_id, order_id, md_name, md_qty, md_fin_price, store_name, mdimg_thumbnail, store_loc, rvw_content, rvw_img1, rvw_img2, rvw_img3, star_count;
    ReviewDeleteService service;
    JsonParser jsonParser;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_check);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ReviewDeleteService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        review_user_id = intent.getStringExtra("review_user_id");
        order_id = intent.getStringExtra("order_id");
        md_name = intent.getStringExtra("md_name");
        md_qty = intent.getStringExtra("md_qty");
        md_fin_price = intent.getStringExtra("md_fin_price");
        store_name = intent.getStringExtra("store_name");
        mdimg_thumbnail = intent.getStringExtra("mdimg_thumbnail");
        rvw_content = intent.getStringExtra("rvw_content");
        star_count = intent.getStringExtra("rvw_rating");
        rvw_img1 = intent.getStringExtra("rvw_img1");
        rvw_img2 = intent.getStringExtra("rvw_img2");
        rvw_img3 = intent.getStringExtra("rvw_img3");

        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewMyDetailActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        TextView reviewUser = findViewById(R.id.reviewUser); //나면 나, 다른 사람이면 user_name으로
        if (user_id.equals(review_user_id)) {
            reviewUser.setText("나");
        } else {
            user_name = user_name.substring(0, 1);
            reviewUser.setText(user_name + "**");
        }
        ImageView ReviewCheckProdImg = findViewById(R.id.ReviewCheckProdImg);
        TextView JP_StoreName = (TextView) findViewById(R.id.JP_StoreName);
        TextView ReviewCheckProdName = (TextView) findViewById(R.id.ReviewCheckProdName);
        TextView ReviewCheckOrderCount = (TextView) findViewById(R.id.ReviewCheckOrderCount);
        TextView ReviewCheckOrderPrice = (TextView) findViewById(R.id.ReviewCheckOrderPrice);

        ImageView Star_1 = findViewById(R.id.Star_1);
        ImageView Star_2 = findViewById(R.id.Star_2);
        ImageView Star_3 = findViewById(R.id.Star_3);
        ImageView Star_4 = findViewById(R.id.Star_4);
        ImageView Star_5 = findViewById(R.id.Star_5);
        ImageView reviewImg1 = (ImageView) findViewById(R.id.ReviewCheckImg1);
        ImageView reviewImg2 = (ImageView) findViewById(R.id.ReviewCheckImg2);
        ImageView reviewImg3 = (ImageView) findViewById(R.id.ReviewCheckImg3);
        TextView ReviewCheckContent = findViewById(R.id.ReviewCheckContent);

        //상품 정보
        Glide.with(ReviewMyDetailActivity.this)
                .load(mdimg_thumbnail)
                .into(ReviewCheckProdImg);
        JP_StoreName.setText(store_name);
        ReviewCheckProdName.setText(md_name);
        ReviewCheckOrderCount.setText(md_qty);
        ReviewCheckOrderPrice.setText(md_fin_price);

        //리뷰 정보
        //별점
        if (star_count.equals("1")) {
            Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
        } else if (star_count.equals("2")) {
            Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
        } else if (star_count.equals("3")) {
            Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
        } else if (star_count.equals("4")) {
            Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_4.setImageResource(R.drawable.ic_product_review_list_on_14px);
        } else if (star_count.equals("5")) {
            Star_1.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_2.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_3.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_4.setImageResource(R.drawable.ic_product_review_list_on_14px);
            Star_5.setImageResource(R.drawable.ic_product_review_list_on_14px);
        }

        reviewImg1.setVisibility(View.GONE);
        reviewImg2.setVisibility(View.GONE);
        reviewImg3.setVisibility(View.GONE);

        //리뷰 이미지
        if (!rvw_img1.equals("null")) {
            reviewImg1.setVisibility(View.VISIBLE);
            Glide.with(ReviewMyDetailActivity.this)
                    .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + rvw_img1)
                    .into(reviewImg1);
        }
        if (!rvw_img2.equals("null")) {
            reviewImg2.setVisibility(View.VISIBLE);
            Glide.with(ReviewMyDetailActivity.this)
                    .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + rvw_img2)
                    .into(reviewImg2);
        }
        if (!rvw_img3.equals("null")) {
            reviewImg3.setVisibility(View.VISIBLE);
            Glide.with(ReviewMyDetailActivity.this)
                    .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + rvw_img3)
                    .into(reviewImg3);
        }

        //리뷰 내용
        ReviewCheckContent.setText(rvw_content);

        Button goHome = findViewById(R.id.goHome);
        Button ReviewCheckEditBtn = findViewById(R.id.ReviewCheckEditBtn);
        Button ReviewCheckDeleteBtn = findViewById(R.id.ReviewCheckDeleteBtn);

        // 로그인한 아이디 != 리뷰 작성자
        if (!user_id.equals(review_user_id)) {
            ReviewCheckEditBtn.setVisibility(View.GONE);
            ReviewCheckDeleteBtn.setVisibility(View.GONE);
        }

        //홈으로 가기 버튼 클릭
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewMyDetailActivity.this, MainActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //리뷰 삭제 버튼 클릭
        ReviewCheckDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject body = new JsonObject();
                body.addProperty("user_id", user_id);
                body.addProperty("order_id", order_id);


                //삭제 버튼 클릭 시, 삭제됨을 고지
                ReviewDeleteDialog reviewDeleteDialog = new ReviewDeleteDialog(ReviewMyDetailActivity.this);
                reviewDeleteDialog.show();
                reviewDeleteDialog.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Call<ResponseBody> call = service.reviewDelete(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Log.d("review 삭제", response.toString());

                                if (response.isSuccessful()) {
                                    try {
                                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());

                                        Intent intent = new Intent(getApplicationContext(), ShoppingInfoActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        startActivity(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("실패", "failure.....");
                            }
                        });
                    }
                });
            }
        });
    }
}