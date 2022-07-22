package com.example.consumer_client;

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

import com.example.consumer_client.Adapter.hamburger.FarmDetailAdapter;
import com.example.consumer_client.Adapter.hamburger.FarmDetailInfo;
import com.example.consumer_client.Adapter.hamburger.JointPurchaseActivity;
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

interface MdService {
    @GET("mdView_main")
    Call<ResponseBody> getMdMainData();
}

public class MdListMainActivity extends AppCompatActivity {

    JsonParser jsonParser;
    MdService service;

    private RecyclerView mMdListRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mMdListMainAdapter;
    Context mContext;

    ArrayList<String> md_id_list = new ArrayList<String>();

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_total_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MdService.class);
        jsonParser = new JsonParser();

        mContext = this;

        firstInit();

        JsonObject body = new JsonObject();
        Call<ResponseBody> call = service.getMdMainData();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("91행", response.toString());
                try{
                    JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                    JsonArray jsonArray = res.get("md_result").getAsJsonArray();
                    JsonArray pay_schedule = res.get("pay_schedule").getAsJsonArray();
                    JsonArray pu_start = res.get("pu_start").getAsJsonArray();
                    JsonArray pu_end = res.get("pu_end").getAsJsonArray();

                    Toast.makeText(MdListMainActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                            //어뎁터 적용
                            mMdListMainAdapter = new FarmDetailAdapter(mList);
                            mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                            //세로로 세팅
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mMdListRecyclerView.setLayoutManager(linearLayoutManager);

                            for(int i=0;i<jsonArray.size();i++){
                                md_id_list.add(jsonArray.get(i).getAsJsonObject().get("md_id").getAsString());

                                addMdList("product Img",
                                        jsonArray.get(i).getAsJsonObject().get("farm_name").getAsString(),
                                        jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                        jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                        pay_schedule.get(i).getAsString(),
                                        pu_start.get(i).getAsString() + " ~ " + pu_end.get(i).getAsString()
                                );
                            }


                            mMdListMainAdapter.setOnItemClickListener(
                                    new FarmDetailAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int pos) {
                                            Log.d("162행", String.valueOf(pos));
                                            Log.d("163행", md_id_list.toString());

                                            Intent intent = new Intent(MdListMainActivity.this, JointPurchaseActivity.class);
                                            intent.putExtra("md_id", md_id_list.get(pos));

                                            startActivity(intent);
                                        }
                                    }
                            );
                        } }, 1000 ); // 1000 = 1초
                }
                catch(Exception e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MdListMainActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void firstInit(){
        mMdListRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addMdList(String mdProdImg, String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo mdDetail = new FarmDetailInfo();
        mdDetail.setProdImg(mdProdImg);
        mdDetail.setFarmName(farmName);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setPaySchedule(paySchedule);
        mdDetail.setPuTerm(puTerm);

        mList.add(mdDetail);
    }
}
