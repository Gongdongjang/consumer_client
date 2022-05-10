package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

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

        // 이제 햄버거 전체입점 농가보기 -> 공동장에서 만날수 있는 모든 농가 리사이클러뷰에서 농장 클릭-> 하면 보이는
        // 세부페이지 정보 보이게 하기!!

        //intent로 값 넘길때
        Intent intent;
        String farm_name, farm_info, farm_loc;

        intent=getIntent(); //intent 값 받기

        farm_name=intent.getStringExtra("farmName");
        farm_info=intent.getStringExtra("farmInfo");
        farm_loc=intent.getStringExtra("farmLoc");

        TextView FarmName = (TextView) findViewById(R.id.FarmName);
        TextView FarmExplain = (TextView) findViewById(R.id.FarmExplain);
        TextView FarmLocation = (TextView) findViewById(R.id.FarmLocation);

        FarmName.setText(farm_name);
        FarmExplain.setText(farm_info);
        FarmLocation.setText(farm_loc);

        //지도
        MapView mapView = new MapView(mContext);
        // 중심점 변경 (농가위치 받아오면 위도, 경도 띄우게 하기) // 일단 나주농장 되는지 확인
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.01426900000000, 126.7169940), true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.farm_map_view);
        mapViewContainer.addView(mapView);


        //----------세부페이지에 있는 진행중인 공동구매 리사이클러뷰 띄우게하기
        //추후에 제품 이름 가져올 예정
        firstInit();
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
