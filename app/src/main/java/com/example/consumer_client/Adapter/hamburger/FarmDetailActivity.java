package com.example.consumer_client.Adapter.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmDetailData;
import com.example.consumer_client.FarmDetailResponse;
import com.example.consumer_client.R;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mFarmDetailAdapter;
    private ServiceApi service;
    Context mContext;
    int md_count, farm_id;
    String farm_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);

        mContext = this;

        // 이제 햄버거 전체입점 농가보기 -> 공동장에서 만날수 있는 모든 농가 리사이클러뷰에서 농장 클릭-> 하면 보이는
        // 세부페이지 정보 보이게 하기!!

        //intent로 값 넘길때
        Intent intent;
        String farm_info, farm_loc, farm_hours;
        Double farm_lat,farm_long;
//        int md_count, farm_id;
//        Integer md_count;
        service = RetrofitClient.getClient().create(ServiceApi.class);

//        String[] md_name = new String[30];
//        String[] store_name = new String[30];
//        String[] pu_start = new String[30];
//        String[] pu_end = new String[30];

        intent=getIntent(); //intent 값 받기

        farm_name=intent.getStringExtra("farmName");
        farm_info=intent.getStringExtra("farmInfo");
        farm_loc=intent.getStringExtra("farmLoc");
        farm_hours=intent.getStringExtra("farmHours");
        farm_id = (int) Double.parseDouble(intent.getStringExtra("farmId"));
        md_count = (int) Double.parseDouble(intent.getStringExtra("mdCount"));
        Log.d("119행", String.valueOf(farm_id));
//        Log.d("62행", String.valueOf((int)Double.parseDouble(farm_id)));

//        store_name = intent.getStringArrayExtra("storeName");
//        pu_start = intent.getStringArrayExtra("puStart");
//        pu_end = intent.getStringArrayExtra("puEnd");

        farm_lat=Double.parseDouble(intent.getStringExtra("farmLat")); //위도-> double 형변환
        farm_long=Double.parseDouble(intent.getStringExtra("farmLong")); //경도

        TextView FarmName = (TextView) findViewById(R.id.FarmName);
        TextView FarmExplain = (TextView) findViewById(R.id.FarmExplain);
        TextView FarmLocation = (TextView) findViewById(R.id.FarmLocation);
        TextView FarmHourTime = (TextView) findViewById(R.id.FarmHourTime);
        TextView FarmJointPurchaseCount = (TextView) findViewById(R.id.FarmJointPurchaseCount);
//        TextView FarmId = (TextView) findViewById(R.id.FarmId);

        FarmName.setText(farm_name);
        FarmExplain.setText(farm_info);
        FarmLocation.setText(farm_loc);
        FarmHourTime.setText(farm_hours);
        FarmJointPurchaseCount.setText(String.valueOf(md_count));
//        FarmId.setText(String.valueOf(farm_id));

        //지도
        MapView mapView = new MapView(mContext);
        // 중심점 변경
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.01426900000000, 126.7169940), true);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(farm_lat, farm_long), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.farm_map_view);
        mapViewContainer.addView(mapView);

        //농가위치 마커 아이콘 띄우기
        MapPoint f_MarkPoint = MapPoint.mapPointWithGeoCoord(farm_lat, farm_long);  //마커찍기

        MapPOIItem farm_marker=new MapPOIItem();
        farm_marker.setItemName(farm_name); //클릭했을때 농가이름 나오기
        farm_marker.setTag(0);
        farm_marker.setMapPoint(f_MarkPoint);   //좌표입력받아 현위치로 출력

        //  (클릭 전)기본으로 제공하는 BluePin 마커 모양의 색.
        farm_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // (클릭 후) 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        farm_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(farm_marker);

        //나중에 농가위치 마커 커스텀 이미지로 바꾸기
        //farm_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        //farm_marker.setCustomImageResourceId(R.drawable.homeshape);

        firstInit();
        //----------세부페이지에 있는 진행중인 공동구매 리사이클러뷰 띄우게하기
        send_farm_id(new FarmDetailData(farm_id, md_count));
    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmJointPurchaseView);
        mList = new ArrayList<>();
    }

    public void addFarmJointPurchase(String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmName(farmName);
        farmDetail.setProdName(prodName);
        farmDetail.setStoreName(storeName);
        farmDetail.setPaySchedule(paySchedule);
        farmDetail.setPuTerm(puTerm);

        mList.add(farmDetail);
    }
    //farm_id 보내기
    private void send_farm_id(FarmDetailData data) {
        service.farmDetail(data).enqueue(new Callback<FarmDetailResponse>() {
            @Override
            public void onResponse(Call<FarmDetailResponse> call, Response<FarmDetailResponse> response) {
                FarmDetailResponse result = response.body();
                Toast.makeText(FarmDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    Log.d("171", result.getMd_name().toString());

                    //어뎁터 적용
                    mFarmDetailAdapter = new FarmDetailAdapter(mList);
                    mRecyclerView.setAdapter(mFarmDetailAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

//                추후에 제품 이름 가져올 예정
                    for(int i=0;i<md_count;i++){
                        addFarmJointPurchase(farm_name, result.getMd_name().get(i).toString(), result.getStore_name().get(i).toString() , result.getPay_schedule().get(i).toString(), result.getPu_start().get(i).toString() + "~" + result.getPu_end().get(i).toString());
                    }

                }
                else{
                    //같은 화면 다시 띄우기
                }
            }
            @Override
            public void onFailure(Call<FarmDetailResponse> call, Throwable t) {
                Toast.makeText(FarmDetailActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }

}
