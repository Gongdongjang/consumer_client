package com.example.consumer_client.store;

import static com.example.consumer_client.address.LocationDistance.distance;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.farm.FarmActivity;
import com.example.consumer_client.farm.FarmDetailActivity;
import com.example.consumer_client.farm.FarmDetailAdapter;
import com.example.consumer_client.md.MdDetailInfo;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.R;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface StoreDetailService {
    @POST("storeDetail/")
    Call<ResponseBody> StoreDetail(@Body JsonObject body);
}

public class StoreDetailActivity extends AppCompatActivity {

    StoreDetailService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray storeArray, jpArray,rvwArray, pu_start, pu_end, storeDate, dDay;
    String store_id, store_name;
    Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerView reviewRecyclerView;
    private ArrayList<MdDetailInfo> mList;
    private ArrayList<StoreReviewInfo> mReviewList;
    private FarmDetailAdapter mStoreDetailAdapter;
    private StoreReviewAdapter mStoreReviewAdapter;

    String user_id, standard_address, day;
    double myTownLat;
    double myTownLong;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mContext = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StoreDetailService.class);
        jsonParser = new JsonParser();

        Intent intent;
        intent=getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        store_id=intent.getStringExtra("storeid");
        standard_address=intent.getStringExtra("standard_address");

        //뒤로가기
        ImageView toolbar_goBack = findViewById(R.id.toolbar_goBack);
        toolbar_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StoreDetailActivity.this, StoreActivity.class);
                intent1.putExtra("user_id", user_id);
                intent1.putExtra("standard_address", standard_address);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView toolbar_cart = findViewById(R.id.toolbar_cart);
        toolbar_cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StoreDetailActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        TextView change_address = findViewById(R.id.change_address);
        ImageView StoreMainImg = findViewById(R.id.StoreMainImg);
        ImageView StoreStoryImg = findViewById(R.id.StoreStoryImg);
        TextView StoreName = (TextView) findViewById(R.id.StoreName);
        TextView StoreExplain = (TextView) findViewById(R.id.StoreExplain);
        TextView StoreLocation = (TextView) findViewById(R.id.StoreLocation);
        TextView StoreStart = (TextView) findViewById(R.id.StoreStart);
        TextView StoreEnd = (TextView) findViewById(R.id.StoreEnd);
        TextView StoreWeek = (TextView) findViewById(R.id.StoreWeek);
        TextView StoreCall = (TextView) findViewById(R.id.StoreCall);
        TextView StoreJointPurchaseCount = (TextView) findViewById(R.id.StoreJointPurchaseCount);

        //공유하기
        ImageView KakaoShare = (ImageView) findViewById(R.id.KakaoShare);
        change_address.setText(standard_address);

        final Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> myAddr = null;
        try {
            myAddr = geocoder.getFromLocationName(standard_address, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = myAddr.get(0);
        myTownLat= location.getLatitude();
        myTownLong = location.getLongitude();


        JsonObject body = new JsonObject();
        body.addProperty("id", store_id);
        Call<ResponseBody> call = service.StoreDetail(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                         res= (JsonObject) jsonParser.parse(response.body().string());

                        //store정보
                        storeArray= res.get("store_result").getAsJsonArray();

                        //md정보
                        jpArray = res.get("jp_result").getAsJsonArray();
                        pu_start = res.get("pu_start").getAsJsonArray();
                        pu_end = res.get("pu_end").getAsJsonArray();
                        dDay = res.get("dDay").getAsJsonArray();

                        //리뷰정보
                        rvwArray = res.get("review_result").getAsJsonArray();
                        store_name = storeArray.get(0).getAsJsonObject().get("store_name").getAsString();

                        //영업 or 휴무일 정보
                        storeDate = res.get("store_date").getAsJsonArray();
                        day = res.get("day").getAsString();

                        //공유하기
                        KakaoShare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    FeedTemplate params = FeedTemplate
                                            .newBuilder(ContentObject.newBuilder(store_name+"에서 픽업하기!",
                                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + storeArray.get(0).getAsJsonObject().get("store_mainImg").getAsString(),
                                                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                                            .setMobileWebUrl("https://developers.kakao.com").build())
                                                    .setDescrption("근처에서 직접 픽업하고 소포장을 줄여 환경도 지키자!")
                                                    .build())
                                            .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
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
                                        public void onFailure(ErrorResult errorResult) {}

                                        @Override
                                        public void onSuccess(KakaoLinkResponse result) {
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();}
                            }
                        });

                        Glide.with(StoreDetailActivity.this)
                                .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + storeArray.get(0).getAsJsonObject().get("store_mainImg").getAsString())
                                .into(StoreMainImg);
                        Glide.with(StoreDetailActivity.this)
                                .load("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + storeArray.get(0).getAsJsonObject().get("store_detailImg").getAsString())
                                .into(StoreStoryImg);

                        StoreName.setText(store_name);
                        StoreExplain.setText(storeArray.get(0).getAsJsonObject().get("store_info").getAsString());
                        StoreLocation.setText(storeArray.get(0).getAsJsonObject().get("store_loc").getAsString());

                        List<Address> address = null;
                        try {
                            address = geocoder.getFromLocationName(storeArray.get(0).getAsJsonObject().get("store_loc").getAsString(), 8);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address location = address.get(0);
                        double store_lat = location.getLatitude();
                        double store_long = location.getLongitude();
                        //자신이 설정한 위치와 스토어 거리 distance 구하기
                        double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");

                        if(day.equals("일")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_sun1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_sun2").getAsString());
                        }
                        else if(day.equals("월")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_mon1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_mon2").getAsString());
                        }
                        else if(day.equals("화")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_tue1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_tue2").getAsString());
                        }
                        else if(day.equals("수")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_wed1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_wed2").getAsString());
                        }
                        else if(day.equals("목")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_thu1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_thu2").getAsString());
                        }
                        else if(day.equals("금")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_fri1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_fri2").getAsString());
                        }
                        else if(day.equals("토")){
                            StoreStart.setText(storeDate.get(0).getAsJsonObject().get("hours_sat1").getAsString());
                            StoreEnd.setText(storeDate.get(0).getAsJsonObject().get("hours_sat2").getAsString());
                        }

                        // 휴무일 다시 처리 -> 어떻게 출력되는지 확인
                        StoreWeek.setText(storeDate.get(0).getAsJsonObject().get("hours_week").getAsString());
                        StoreCall.setText(storeArray.get(0).getAsJsonObject().get("store_phone").getAsString());
                        StoreJointPurchaseCount.setText(String.valueOf(jpArray.size()));

                        firstInit();

                        //어뎁터 적용
                        mStoreDetailAdapter = new FarmDetailAdapter(mList);
                        mRecyclerView.setAdapter(mStoreDetailAdapter);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 2, GridLayoutManager.VERTICAL, false);
                        mRecyclerView.setLayoutManager(gridLayoutManager);

//                       //리뷰 어뎁터 적용
//                       mStoreReviewAdapter = new StoreReviewAdapter(mReviewList);
//                       reviewRecyclerView.setAdapter(mStoreReviewAdapter);

                        //두 칸으로 세팅
                        //GridLayoutManager gridLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 2, GridLayoutManager.VERTICAL, false);
                        //reviewRecyclerView.setLayoutManager(gridLayoutManager);

                        //진행중인 공동구매 md
                        for(int i=0;i<jpArray.size();i++){

                            String realIf0;
                            if (dDay.get(i).getAsString().equals("0")) realIf0 = "D - day";
                            else if(dDay.get(i).getAsInt() < 0) realIf0 = "D + "+ Math.abs(dDay.get(i).getAsInt());
                            else realIf0 = "D - " + dDay.get(i).getAsString();

                            addStoreJointPurchase(
                                     "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jpArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    jpArray.get(i).getAsJsonObject().get("md_id").getAsString(),
                                    jpArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    jpArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    String.format("%.2f", distanceKilo)+"km",
                                    jpArray.get(i).getAsJsonObject().get("pay_price").getAsString(),
                                    realIf0,  pu_start.get(i).getAsString());
                        }

//                        //리뷰
//                        for(int i=0;i<rvwArray.size();i++){
//                            addReview("product Img", "@id " + i, rvwArray.get(i).getAsJsonObject().get("md_name").getAsString(), rvwArray.get(i).getAsJsonObject().get("rvw_rating").getAsString(),rvwArray.get(i).getAsJsonObject().get("rvw_content").getAsString());
//                        }

                        //거리 가까운순으로 정렬
                        mList.sort(new Comparator<MdDetailInfo>() {
                            @Override
                            public int compare(MdDetailInfo o1, MdDetailInfo o2) {
                                int ret;
                                Double distance1 = Double.valueOf(o1.getDistance().substring(0, o1.getDistance().length() - 2));
                                Double distance2 = Double.valueOf(o2.getDistance().substring(0, o2.getDistance().length() - 2));
                                //거리비교
                                ret = distance1.compareTo(distance2);
                                return ret;
                            }
                        });

                        mStoreDetailAdapter.setOnItemClickListener(
                                new FarmDetailAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(StoreDetailActivity.this, JointPurchaseActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("md_id", mList.get(pos).getMdId());
                                        startActivity(intent);
                                    }
                                }
                        );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StoreDetailActivity.this, "스토어 세부 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("세부", t.getMessage());
            }

        });

    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmPurchaseView);
        reviewRecyclerView = findViewById(R.id.StoreReview);
        mList = new ArrayList<>();
        mReviewList = new ArrayList<>();
    }

    public void addStoreJointPurchase(String prodImgName, String mdId, String prodName, String storeName, String distance, String mdPrice, String dDay, String puTime){
        MdDetailInfo mdDetail = new MdDetailInfo();

        mdDetail.setProdImg(prodImgName);
        mdDetail.setMdId(mdId);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setDistance(distance);
        mdDetail.setMdPrice(mdPrice);
        mdDetail.setDday(dDay);
//        mdDetail.setPaySchedule(paySchedule);
        mdDetail.setPuTime(puTime);

        mList.add(mdDetail);
    }

    public void addReview(String userProfileImg, String userID, String prodName, String starCount, String reviewMessage){
        StoreReviewInfo review = new StoreReviewInfo();

        review.setUserProfileImg(userProfileImg);
        review.setUserID(userID);
        review.setProdName(prodName);
        review.setStarCount(starCount);
        review.setReview(reviewMessage);

        mReviewList.add(review);
    }
}