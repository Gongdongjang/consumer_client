package com.example.consumer_client.content;

import static com.example.consumer_client.address.LocationDistance.distance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.farm.FarmDetailAdapter;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.md.MdDetailInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ContentDetailService {
    @POST("contentDetail")
    Call<ResponseBody> contentDetail(@Body JsonObject body);
}

public class ContentDetailActivity extends AppCompatActivity {
    String TAG = ContentDetailActivity.class.getSimpleName();

    ContentDetailService service;
    JsonParser jsonParser;
    Context mContext;
    JsonObject res;
    JsonArray mdArray, dDay;

    TextView content_title, content_context, contentDate;
    ImageView content_photo, contentMainPhoto;

    double myTownLat, myTownLong;
    String user_id, standard_address, content_md_id1, content_md_id2;

    //제품 어뎁터 선언
    private RecyclerView mMdRecyclerView;
    private ArrayList<MdDetailInfo> mMdList;
    FarmDetailAdapter farmDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_click);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ContentDetailService.class);
        jsonParser = new JsonParser();

        mContext = this;

        content_title = findViewById(R.id.content_title);
        content_context = findViewById(R.id.content_context);
        contentMainPhoto = findViewById(R.id.content_main_photo);
        content_photo = findViewById(R.id.content_photo);
        contentDate = findViewById(R.id.content_date);

        Intent intent = getIntent();
        content_title.setText(intent.getStringExtra("content_title"));
        content_context.setText(intent.getStringExtra("content_context"));
        contentDate.setText(Objects.requireNonNull(intent.getStringExtra("contentDate")).split("T")[0]);
        Picasso.get().load(intent.getStringExtra("content_photo")).into(content_photo);
        Picasso.get().load(intent.getStringExtra("contentMainPhoto")).into(contentMainPhoto);

        content_md_id1 = intent.getStringExtra("content_md_id1");
        content_md_id2 = intent.getStringExtra("content_md_id2");

        user_id = intent.getStringExtra("user_id");
        standard_address = intent.getStringExtra("standard_address");

        //뒤로가기
        ImageView gotoBack = findViewById(R.id.gotoBack);
        gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ContentDetailActivity.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContentDetailActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        if (!content_md_id1.equals("null") || !content_md_id2.equals("null")) {
            JsonObject body = new JsonObject();
            body.addProperty("content_md_id1", content_md_id1);
            body.addProperty("content_md_id2", content_md_id2);

            final Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> myAddr = null;
            try {
                myAddr = geocoder.getFromLocationName(standard_address, 8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address location = myAddr.get(0);
            myTownLat = location.getLatitude();
            myTownLong = location.getLongitude();

            Call<ResponseBody> call = service.contentDetail(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        res = (JsonObject) jsonParser.parse(response.body().string());

                        //md 정보
                        mdArray = res.get("contentDetail").getAsJsonArray();
                        dDay = res.get("dDay").getAsJsonArray();

                        //진행 중인 공동구매 리사이클러뷰 띄우게하기
                        firstInit();

                        //제품 상세 어뎁터 적용
                        farmDetailAdapter = new FarmDetailAdapter(mMdList);
                        mMdRecyclerView.setAdapter(farmDetailAdapter);

                        //가로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        mMdRecyclerView.setLayoutManager(linearLayoutManager);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ContentDetailActivity.this, 2, GridLayoutManager.VERTICAL, false);
                        mMdRecyclerView.setLayoutManager(gridLayoutManager);

                        for (int i = 0; i < mdArray.size(); i++) {
                            List<Address> address = null;
                            try {
                                address = geocoder.getFromLocationName(mdArray.get(i).getAsJsonObject().get("store_loc").getAsString(), 8);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Address location = address.get(0);
                            double store_lat = location.getLatitude();
                            double store_long = location.getLongitude();
                            //자신이 설정한 위치와 스토어 거리 distance 구하기
                            double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");

                            String realIf0;
                            if (dDay.get(i).getAsString().equals("0")) realIf0 = "D - day";
                            else if (dDay.get(i).getAsInt() < 0)
                                realIf0 = "D + " + Math.abs(dDay.get(i).getAsInt());
                            else realIf0 = "D - " + dDay.get(i).getAsString();

                            addFarmJointPurchase(
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + mdArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    mdArray.get(i).getAsJsonObject().get("md_id").getAsString(),
                                    mdArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    mdArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    String.format("%.2f", distanceKilo) + "km",
                                    mdArray.get(i).getAsJsonObject().get("pay_price").getAsString(),
                                    realIf0, "null");
                        }

                        //거리 가까운순으로 정렬
                        mMdList.sort(new Comparator<MdDetailInfo>() {
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

                        farmDetailAdapter.setOnItemClickListener(
                                new FarmDetailAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, int pos) {
                                        Intent intent = new Intent(ContentDetailActivity.this, JointPurchaseActivity.class);
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("md_id", mMdList.get(pos).getMdId());

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
                    Log.e(TAG, "onFailure: e " + t.getMessage());
                }
            });
        }

    }

    public void firstInit() {
        mMdRecyclerView = findViewById(R.id.jp_list);
        mMdList = new ArrayList<>();
    }

    //제품 리스트 연결
    public void addFarmJointPurchase(String prodImgName, String mdId, String prodName, String
            storeName, String distance, String mdPrice, String dDay, String puTime) {
        MdDetailInfo mdDetail = new MdDetailInfo();

        mdDetail.setProdImg(prodImgName);
        mdDetail.setMdId(mdId);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setDistance(distance);
        mdDetail.setMdPrice(mdPrice);
        mdDetail.setDday(dDay);
        mdDetail.setPuTime(puTime);

        mMdList.add(mdDetail);
    }
}