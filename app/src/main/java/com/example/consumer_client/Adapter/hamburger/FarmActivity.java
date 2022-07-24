package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmGet;
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
import retrofit2.http.Body;
import retrofit2.http.GET;

interface FarmService{
    @GET("/farmView")
    Call<ResponseBody> getFarmData();
}

public class FarmActivity extends AppCompatActivity {

    FarmService service;
    JsonParser jsonParser;

    private RecyclerView mFarmRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;

    JsonObject res;
    JsonArray farmArray, mdArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

        mContext = this;

        firstInit();

        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

//        JsonObject body = new JsonObject();
        Call<ResponseBody> call = service.getFarmData();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    res =  (JsonObject) jsonParser.parse(response.body().string());

                    farmArray = res.get("result").getAsJsonArray();
                    mdArray = res.get("md_result").getAsJsonArray();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FarmActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                //어뎁터 적용
                mFarmTotalAdapter = new FarmTotalAdapter(mList);
                mFarmRecyclerView.setAdapter(mFarmTotalAdapter);

                //세로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mFarmRecyclerView.setLayoutManager(linearLayoutManager);

                for(int i=0;i<farmArray.size();i++){
                    int count = 0;
                    for( int j = 0; j < mdArray.size(); j++){
                        if(mdArray.get(j).getAsJsonObject().get("farm_id").getAsInt() == farmArray.get(i).getAsJsonObject().get("farm_id").getAsInt()){
                            count++;
                        }
                    }
                    addFarm("product Img", farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);
                }

                mFarmTotalAdapter.setOnItemClickListener(
                        new FarmTotalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent intent = new Intent(FarmActivity.this, FarmDetailActivity.class);

                                intent.putExtra("farm_name", farmArray.get(pos).getAsJsonObject().get("farm_name").getAsString());
                                intent.putExtra("farm_info",farmArray.get(pos).getAsJsonObject().get("farm_info").getAsString());
                                intent.putExtra("farm_loc",farmArray.get(pos).getAsJsonObject().get("farm_loc").getAsString());
                                intent.putExtra("farm_lat",farmArray.get(pos).getAsJsonObject().get("farm_lat").getAsString());
                                intent.putExtra("farm_long",farmArray.get(pos).getAsJsonObject().get("farm_long").getAsString());
                                intent.putExtra("farm_hours",farmArray.get(pos).getAsJsonObject().get("farm_hours").getAsString());
                                intent.putExtra("farm_id",farmArray.get(pos).getAsJsonObject().get("farm_id").getAsString());
                                startActivity(intent);
                            }
                        });
            }}, 1000 ); // 1000 = 1초
    }

    public void firstInit(){
        mFarmRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addFarm(String farmProdImg, String farmName, String farmProdName, String farmFeature,int farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmProdName(farmProdName);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
