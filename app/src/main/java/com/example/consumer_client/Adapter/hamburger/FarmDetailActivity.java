package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;

public class FarmDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mFarmDetailAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);

        mContext = this;

        firstInit();

        //추후에 제품 이름 가져올 예정
        for(int i=0;i<10;i++){
            addFarmJointPurchase("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
        }

        //어뎁터 적용
        mFarmDetailAdapter = new FarmDetailAdapter(mList);
        mRecyclerView.setAdapter(mFarmDetailAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
}

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmJointPurchaseView);
        mList = new ArrayList<>();
    }

    public void addFarmJointPurchase(String farmProdImg, String farmName, String farmProdName, String farmFeature, String farmSituation){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmDetailProdImgView(farmProdImg);
        farmDetail.setFarmDetailName(farmName);
        farmDetail.setFarmDetailProdName(farmProdName);

        mList.add(farmDetail);
    }
}
