package com.example.consumer_client.Adapter.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmData;
import com.example.consumer_client.FarmGet;
import com.example.consumer_client.FarmResponse;
import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.user.LoginActivity;
import com.example.consumer_client.user.data.LoginResponse;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import net.daum.mf.map.api.MapView;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FarmActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;
    String[] farmNameL = new String[30];
    String[] farmInfo = new String[30];
    String[] farmMainItem = new String[30];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);

        mContext = this;

        firstInit();

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<FarmGet> call = service.getFarmData();
        call.enqueue(new Callback<FarmGet>() {
            @Override
            public void onResponse(Call<FarmGet> call, Response<FarmGet> response) {

                if (response.isSuccessful()) {
                    FarmGet result = response.body();
                    List farm = result.getFarm();
                    for (int i = 0; i < Integer.parseInt(result.getCount()); i++) {
                        String str = farm.get(i).toString();
                        String[] list = str.split(", ");
                        farmNameL[i] = list[1].substring(10);
                        farmInfo[i] = list[2].substring(10);
                        farmMainItem[i] = list[3].substring(14);
                        Log.d("83행", farmInfo[i]);
                        Log.d("84행", farmMainItem[i]);
                    }
                    Toast.makeText(FarmActivity.this, "로딩중", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<FarmGet> call, Throwable t) {
                Toast.makeText(FarmActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                //어뎁터 적용
                mFarmTotalAdapter = new FarmTotalAdapter(mList);
                mRecyclerView.setAdapter(mFarmTotalAdapter);

                //세로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                
                for(int i=0;i<2;i++){
                    Log.d("farmName", farmNameL[i]);
                    String s = farmNameL[i];
                    String info = farmInfo[i];
                    String main = farmMainItem[i];
                    addFarm("product Img", s, main, info, "" + i);
                }
            } }, 500 ); // 1000 = 1초
    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addFarm(String farmProdImg, String farmName, String farmProdName, String farmFeature, String farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmProdName(farmProdName);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
