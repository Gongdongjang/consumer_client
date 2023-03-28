package com.gdjang.consumer_client.md;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

//import com.example.consumer_client.cart.CartDialog;
import com.bumptech.glide.Glide;
import com.gdjang.consumer_client.ItemDetailPagerAdapter;
import com.gdjang.consumer_client.cart.CartListActivity;
import com.gdjang.consumer_client.order.OrderDialog;
import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.review.ReviewListAdapter;
import com.gdjang.consumer_client.review.ReviewListInfo;
import com.gdjang.consumer_client.review.ReviewMyDetailActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface JointPurchaseService {
    @POST("jointPurchase")
    Call<ResponseBody> postMdId(@Body JsonObject body);

    @POST("isKeep")
    Call<ResponseBody> postisKeep(@Body JsonObject body);

    @POST("keep")
    Call<ResponseBody> postKeep(@Body JsonObject body);
}

public class JointPurchaseActivity extends AppCompatActivity {
    String TAG = JointPurchaseActivity.class.getSimpleName();

    private String md_id;
    Context mContext;

    JointPurchaseService service;
    JsonParser jsonParser;

    JsonObject res, body;
    JsonArray md_detail, reviewArray, keep_date;
    String pu_start, pu_end, pickup_start, pickup_end, user_id, store_id, md_end, dDay, standard_address, mdimg_thumbnail;
    JsonArray keep_data;
    String message, store_loc;
    String[] imgUrl = new String[5];

    private RecyclerView mReviewListRecyclerView;
    private ReviewListAdapter mReviewListAdapter;
    private ArrayList<ReviewListInfo> mReviewList;

    //Dialog 선언
    OrderDialog orderDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_purchase);

        mContext = this;

        //상단바
        TextView up_FarmerName = (TextView) findViewById(R.id.up_FarmerName);
        TextView up_ProdName = (TextView) findViewById(R.id.up_ProdName);
        //뒤로가기
        ImageView up_mdArrow = findViewById(R.id.up_mdArrow);

        //000 농부님의 000상품 단락
        TextView FarmName = (TextView) findViewById(R.id.JP_FarmName_Main);
        TextView MdName = (TextView) findViewById(R.id.ProdName);
        TextView Dday = (TextView) findViewById(R.id.Dday);
        TextView MdPrice = (TextView) findViewById(R.id.setPerCost);
        TextView PurchaseDate = (TextView) findViewById(R.id.purchaseDate);
        TextView StkRemain = (TextView) findViewById(R.id.JP_Remain_Count);
        TextView StkGoal = (TextView) findViewById(R.id.JP_Goal_Count);
        TextView StkTotal = (TextView) findViewById(R.id.JP_TotalCount);
        TextView PuStart = (TextView) findViewById(R.id.JP_PU_Start);
        TextView PuEnd = (TextView) findViewById(R.id.JP_PU_End);

        //고정 하단바 (찜하기, 장바구니, 주문하기)
        ImageView Keep = (ImageView) findViewById(R.id.JP_KeepBtn);
        Button Order = (Button) findViewById(R.id.JP_OrderBtn);

        //공유하기
        ImageView KakaoShare = (ImageView) findViewById(R.id.KakaoShare);

        //농가와 픽업 스토어 소개 단락
        ImageView FarmFileName = (ImageView) findViewById(R.id.JP_FarmIMG);
        TextView FarmName2 = (TextView) findViewById(R.id.JP_FarmName);
        TextView FarmInfo = (TextView) findViewById(R.id.JP_FarmDesc);
        ImageView StoreFileName = (ImageView) findViewById(R.id.JP_StoreIMG);
        TextView StoreName = (TextView) findViewById(R.id.JP_StoreName);
        TextView StoreInfo = (TextView) findViewById(R.id.JP_StoreDesc);
        ImageView JP_MD_Datail_Img = findViewById(R.id.JP_MD_Datail_Img);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(JointPurchaseService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        standard_address = intent.getStringExtra("standard_address");
        md_id = intent.getStringExtra("md_id");

        //뒤로가기
        up_mdArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(JointPurchaseActivity.this, MdListMainActivity.class);
                intent1.putExtra("user_id", user_id);
                intent1.putExtra("standard_address", standard_address);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView toolbar_cart = findViewById(R.id.toolbar_cart);
        toolbar_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JointPurchaseActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);

        //상세페이지 데이터 등록
        Call<ResponseBody> call2 = service.postMdId(body);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res = (JsonObject) jsonParser.parse(response.body().string());
                        md_detail = res.get("md_detail_result").getAsJsonArray();
                        pu_start = res.get("pu_start").getAsString();
                        pu_end = res.get("pu_end").getAsString();
                        pickup_start= md_detail.get(0).getAsJsonObject().get("pu_timeStart").getAsString();
                        pickup_end= md_detail.get(0).getAsJsonObject().get("pu_timeEnd").getAsString();
                        md_end = res.get("md_end").getAsString();
                        dDay = res.get("dDay").getAsString();
                        reviewArray = res.get("review_data").getAsJsonArray();

                        store_id = md_detail.get(0).getAsJsonObject().get("store_id").getAsString();

                        imgUrl[0] = md_detail.get(0).getAsJsonObject().get("mdImg_slide01").isJsonNull() ? "null" : "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_slide01").getAsString();
                        imgUrl[1] = md_detail.get(0).getAsJsonObject().get("mdImg_slide02").isJsonNull() ? "null" : "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_slide02").getAsString();
                        imgUrl[2] = md_detail.get(0).getAsJsonObject().get("mdImg_slide03").isJsonNull() ? "null" : "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_slide03").getAsString();
                        imgUrl[3] = md_detail.get(0).getAsJsonObject().get("mdImg_slide04").isJsonNull() ? "null" : "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_slide04").getAsString();
                        imgUrl[4] = md_detail.get(0).getAsJsonObject().get("mdImg_slide05").isJsonNull() ? "null" : "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_slide05").getAsString();

                        int count = 0;
                        for (int i = 0; i < 5; i++) {
                            if (!imgUrl[i].equals("null")) {
                                count++;
                            }
                        }
                        //가로 슬라이더
                        setInit(count, imgUrl[0], imgUrl[1], imgUrl[2], imgUrl[3], imgUrl[4]);

                        //스토어 위치(주문하기에서)
                        store_loc = md_detail.get(0).getAsJsonObject().get("store_loc").getAsString();

                        //상단바 setText
                        up_FarmerName.setText(md_detail.get(0).getAsJsonObject().get("farm_farmer").getAsString());
                        up_ProdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());

                        //000 농부님의 000상품 setText
                        FarmName.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                        MdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());
                        MdPrice.setText(md_detail.get(0).getAsJsonObject().get("pay_price").getAsString());

                        String realIf0;
                        if (dDay.equals("0")) realIf0 = "D - day";
                        else if (Integer.parseInt(dDay) < 0)
                            realIf0 = "D + " + Math.abs(Integer.parseInt(dDay));
                        else realIf0 = "D - " + dDay;

                        Dday.setText(realIf0);
                        PurchaseDate.setText(md_end);
                        StkRemain.setText(md_detail.get(0).getAsJsonObject().get("stk_remain").getAsString());
                        StkGoal.setText(md_detail.get(0).getAsJsonObject().get("stk_goal").getAsString());
                        StkTotal.setText(md_detail.get(0).getAsJsonObject().get("stk_total").getAsString());
                        PuStart.setText(pu_start);
                        PuEnd.setText(pu_end);

                        Glide.with(JointPurchaseActivity.this)
                                .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdImg_detail").getAsString())
                                .into(JP_MD_Datail_Img);

                        //프로필 이미지
                        Glide.with(JointPurchaseActivity.this)
                                .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("farm_thumbnail").getAsString())
                                .into(FarmFileName);
                        Glide.with(JointPurchaseActivity.this)
                                .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("store_thumbnail").getAsString())
                                .into(StoreFileName);

                        mdimg_thumbnail = "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdimg_thumbnail").getAsString();

                        //공유하기
                        KakaoShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    FeedTemplate params = FeedTemplate
                                            .newBuilder(ContentObject.newBuilder(md_detail.get(0).getAsJsonObject().get("farm_farmer").getAsString() + " 농부의 " + md_detail.get(0).getAsJsonObject().get("md_name").getAsString() + "구매하기!",
                                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + md_detail.get(0).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                                            .setMobileWebUrl("https://developers.kakao.com").build())
                                                    .setDescrption("근처에서 직접 픽업하고 소포장을 줄여 환경도 지키자!")
                                                    .build())
                                            .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                                    .setWebUrl("https://developers.kakao.com")
                                                    .setMobileWebUrl("https://developers.kakao.com")
                                                    .setAndroidExecutionParams("key1=value1")
                                                    .setIosExecutionParams("key1=value1")
                                                    .build()))
                                            .build();

                                    Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                                    serverCallbackArgs.put("user_id", "${current_user_id}");
                                    serverCallbackArgs.put("product_id", "${shared_product_id}");


                                    KakaoLinkService.getInstance().sendDefault(mContext, params, new ResponseCallback<KakaoLinkResponse>() {
                                        @Override
                                        public void onFailure(ErrorResult errorResult) {
                                        }

                                        @Override
                                        public void onSuccess(KakaoLinkResponse result) {
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        //농가와 픽업스토어 setText
                        FarmName2.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                        FarmInfo.setText(md_detail.get(0).getAsJsonObject().get("farm_info").getAsString());
                        StoreName.setText(md_detail.get(0).getAsJsonObject().get("store_name").getAsString());
                        StoreInfo.setText(md_detail.get(0).getAsJsonObject().get("store_info").getAsString());

                        PuStart.setText(pu_start);
                        PuEnd.setText(pu_end);

                        //리뷰 리사이클러뷰 띄우기
                        firstInit();

                        //리뷰 어뎁터 적용
                        mReviewListAdapter = new ReviewListAdapter(mReviewList);
                        mReviewListRecyclerView.setAdapter(mReviewListAdapter);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mReviewListRecyclerView.setLayoutManager(linearLayoutManager);

                        for (int i = 0; i < reviewArray.size(); i++) {
                            addReviewList(
                                    user_id,
                                    reviewArray.get(i).getAsJsonObject().get("order_id").getAsString(),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + reviewArray.get(i).getAsJsonObject().get("rvw_img1").getAsString(),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + reviewArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("rvw_content").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("rvw_rating").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("order_select_qty").getAsString(),
                                    reviewArray.get(i).getAsJsonObject().get("pay_price").getAsString()
                            );
                        }
                        mReviewListAdapter.setOnItemClickListener(
                                new ReviewListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(JointPurchaseActivity.this, ReviewMyDetailActivity.class);
                                        intent.putExtra("user_id", user_id); //현재 로그인 되어있는 user_id
                                        intent.putExtra("review_user_id", reviewArray.get(pos).getAsJsonObject().get("user_id").getAsString()); //리뷰 쓴 user_id
                                        intent.putExtra("user_name", reviewArray.get(pos).getAsJsonObject().get("user_name").getAsString()); //리뷰 쓴 user_name
                                        intent.putExtra("order_id", reviewArray.get(pos).getAsJsonObject().get("order_id").getAsString());
                                        intent.putExtra("mdimg_thumbnail", "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + reviewArray.get(pos).getAsJsonObject().get("mdimg_thumbnail").getAsString());
                                        intent.putExtra("store_name", reviewArray.get(pos).getAsJsonObject().get("store_name").getAsString());
                                        intent.putExtra("md_name", reviewArray.get(pos).getAsJsonObject().get("md_name").getAsString());
                                        intent.putExtra("md_qty", reviewArray.get(pos).getAsJsonObject().get("order_select_qty").getAsString());
                                        intent.putExtra("md_fin_price", reviewArray.get(pos).getAsJsonObject().get("pay_price").getAsString());
                                        intent.putExtra("rvw_content", reviewArray.get(pos).getAsJsonObject().get("rvw_content").getAsString());
                                        intent.putExtra("rvw_rating", reviewArray.get(pos).getAsJsonObject().get("rvw_rating").getAsString());
                                        intent.putExtra("rvw_img1", reviewArray.get(pos).getAsJsonObject().get("rvw_img1").getAsString());
                                        intent.putExtra("rvw_img2", reviewArray.get(pos).getAsJsonObject().get("rvw_img2").getAsString());
                                        intent.putExtra("rvw_img3", reviewArray.get(pos).getAsJsonObject().get("rvw_img3").getAsString());
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
            public void onFailure(Call<ResponseBody> call2, Throwable t) {
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });

        //----고정하단바-----
        //찜 한 정보 불러오기 (해당 사용자가 해당 상품에 찜했으면, 하트)
        Call<ResponseBody> call = service.postisKeep(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                    Log.d("116행", res.toString());
                    message = res.get("message").getAsString();
                    if (message.equals("exist")) {
                        Keep.setImageResource(R.drawable.ic_mypage_wish);
                        Keep.setTag("liked");
                    } else if (message.equals("notexist")) {
                        Keep.setImageResource(R.drawable.ic_order_wish);
                        Keep.setTag("like");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });

        //찜 클릭시 취소 or 등록 제어
        Keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = service.postKeep(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                            keep_data = res.get("keep_data").getAsJsonArray();
                            message = res.get("message").getAsString();
                            if (Keep.getTag().equals("like")) {
                                Toast.makeText(JointPurchaseActivity.this, "찜한 상품에 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                Keep.setImageResource(R.drawable.ic_mypage_wish);
                                Keep.setTag("liked");
                            } else {
                                Toast.makeText(JointPurchaseActivity.this, "찜한 상품에서 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                Keep.setImageResource(R.drawable.ic_order_wish);
                                Keep.setTag("like");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: e " + t.getMessage());
                    }
                });
            }
        });

        //주문 클릭
        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog = new OrderDialog(mContext, (String) MdName.getText(), (String) MdPrice.getText()
                        , (String) StkRemain.getText(), pu_start, pu_end, pickup_start, pickup_end, (String) StoreName.getText(),
                        store_id, store_loc, user_id, md_id, mdimg_thumbnail);
            }
        });
    }

    //뷰페이저2 실행 메서드
    private void setInit(int count, String pic1, String pic2, String pic3, String pic4, String pic5) {
        ViewPager2 viewPageSetUp = findViewById(R.id.JP_MD_Img);
        ItemDetailPagerAdapter itemDetailPagerAdapter = new ItemDetailPagerAdapter(count, this, pic1, pic2, pic3, pic4, pic5);
        viewPageSetUp.setAdapter(itemDetailPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(count); //페이지 한계 지정 개수
        viewPageSetUp.setCurrentItem(1000); //무한처럼 보이도록 하려고

        //페이지끼리 간격
        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        //페이지 보이는 정도
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset);
        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });
    }

    public void firstInit() {
        mReviewListRecyclerView = findViewById(R.id.jp_reviewList);
        mReviewList = new ArrayList<>();
    }

    public void addReviewList(String userId, String orderId, String reviewImg, String mdImgView, String storeName, String mdName,
                              String reviewContent, String rvw_rating, String mdQty, String mdPrice) {
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
        mReviewList.add(reviewList);
    }

}
