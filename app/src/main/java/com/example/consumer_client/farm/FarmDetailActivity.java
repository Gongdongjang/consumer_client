package com.example.consumer_client.farm;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface FarmDetailService{
    @POST("farmDetail")
    Call<ResponseBody> farmDetail(@Body JsonObject body);
}

public class FarmDetailActivity extends AppCompatActivity {
    String TAG = FarmDetailActivity.class.getSimpleName();

    FarmDetailService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray farmArray, mdArray, pu_start, pu_end;
    String farm_id, farm_name, farm_info, farm_loc, farm_hours;

    private RecyclerView mRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mFarmDetailAdapter;

    Context mContext;

    String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(FarmDetailService.class);
        jsonParser = new JsonParser();

        mContext = this;

        TextView FarmName = (TextView) findViewById(R.id.FarmName);
        TextView FarmExplain = (TextView) findViewById(R.id.FarmExplain);
        TextView FarmLocation = (TextView) findViewById(R.id.FarmLocation);
        TextView FarmHourTime = (TextView) findViewById(R.id.FarmHourTime);
        TextView FarmJointPurchaseCount = (TextView) findViewById(R.id.FarmJointPurchaseCount);

        //intent로 값 넘길때
        Intent intent;
        intent=getIntent(); //intent 값 받기

        user_id=intent.getStringExtra("user_id");
        farm_id = intent.getStringExtra("farm_id");

        JsonObject body = new JsonObject();
        body.addProperty("farm_id", farm_id);

        Call<ResponseBody> call = service.farmDetail(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());

                        //farm 정보
                        farmArray = res.get("farm_data").getAsJsonArray();
                        farm_name = farmArray.get(0).getAsJsonObject().get("farm_name").getAsString();
                        farm_info = farmArray.get(0).getAsJsonObject().get("farm_info").getAsString();
                        farm_loc = farmArray.get(0).getAsJsonObject().get("farm_loc").getAsString();
                        //farm_hours = farmArray.get(0).getAsJsonObject().get("farm_hours").getAsString();

                        //md 정보
                        mdArray = res.get("md_data").getAsJsonArray();
                        pu_start = res.get("pu_start").getAsJsonArray();
                        pu_end = res.get("pu_end").getAsJsonArray();

                        FarmName.setText(farm_name);
                        FarmExplain.setText(farm_info);
                        FarmLocation.setText(farm_loc);
                        //FarmHourTime.setText(farm_hours);
                        FarmJointPurchaseCount.setText(String.valueOf(mdArray.size()));

                        //세부 페이지1 (진행 중인 공동구매) 리사이클러뷰 띄우게하기
                        firstInit();

                        //어뎁터 적용
                        mFarmDetailAdapter = new FarmDetailAdapter(mList);
                        mRecyclerView.setAdapter(mFarmDetailAdapter);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(linearLayoutManager);

                        for(int i=0;i<mdArray.size();i++){
                            addFarmJointPurchase(farm_name, "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + mdArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(), mdArray.get(i).getAsJsonObject().get("md_name").getAsString(), mdArray.get(i).getAsJsonObject().get("store_name").getAsString(),pu_start.get(i).getAsString()+" ~ "+pu_end.get(i).getAsString());
                        }
                        Log.d("FarmDetail", user_id);

                        mFarmDetailAdapter.setOnItemClickListener(
                            new FarmDetailAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(FarmDetailActivity.this, JointPurchaseActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("md_id", mdArray.get(pos).getAsJsonObject().get("md_id").getAsString());

                                    startActivity(intent);
                                }
                            }
                    );

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
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });
    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmJointPurchaseView);
        mList = new ArrayList<>();
    }

    public void addFarmJointPurchase(String farmName, String prodImgName, String prodName, String storeName, String puTerm){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmName(farmName);
        farmDetail.setProdImg(prodImgName);
        farmDetail.setProdName(prodName);
        farmDetail.setStoreName(storeName);
//        farmDetail.setPaySchedule(paySchedule);
        farmDetail.setPuTerm(puTerm);

        mList.add(farmDetail);
    }
}
