package com.example.consumer_client.farm;

import android.content.Context;
import android.content.Intent;
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
    JsonArray farmArray, mdArray, pay_schedule, pu_start, pu_end;
    String farm_id, farm_name, farm_info, farm_loc, farm_hours;
    Double farm_lat, farm_long;

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
                        farm_lat = farmArray.get(0).getAsJsonObject().get("farm_lat").getAsDouble();
                        farm_long = farmArray.get(0).getAsJsonObject().get("farm_long").getAsDouble();
                        farm_hours = farmArray.get(0).getAsJsonObject().get("farm_hours").getAsString();

                        //md 정보
                        mdArray = res.get("md_data").getAsJsonArray();
                        pay_schedule = res.get("pay_schedule").getAsJsonArray();
                        pu_start = res.get("pu_start").getAsJsonArray();
                        pu_end = res.get("pu_end").getAsJsonArray();

                        FarmName.setText(farm_name);
                        FarmExplain.setText(farm_info);
                        FarmLocation.setText(farm_loc);
                        FarmHourTime.setText(farm_hours);
                        FarmJointPurchaseCount.setText(String.valueOf(mdArray.size()));

                        //지도
                        MapView mapView = new MapView(mContext);
                        // 중심점 변경
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
                            addFarmJointPurchase(farm_name, mdArray.get(i).getAsJsonObject().get("md_name").getAsString(), mdArray.get(i).getAsJsonObject().get("store_name").getAsString(), pay_schedule.get(i).getAsString(), pu_start.get(i).getAsString()+" ~ "+pu_end.get(i).getAsString());
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

    public void addFarmJointPurchase(String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmName(farmName);
        farmDetail.setProdName(prodName);
        farmDetail.setStoreName(storeName);
        farmDetail.setPaySchedule(paySchedule);
        farmDetail.setPuTerm(puTerm);

        mList.add(farmDetail);
    }
}
