package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;

public class FarmActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);

        mContext = this;

        firstInit();

        //추후에 제품 이름 가져올 예정
        for(int i=0;i<10;i++){
            addFarm("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
        }

        //어뎁터 적용
        mFarmTotalAdapter = new FarmTotalAdapter(mList);
        mRecyclerView.setAdapter(mFarmTotalAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
