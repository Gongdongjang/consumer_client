package com.example.consumer_client.Adapter.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmDetailData;
import com.example.consumer_client.FarmDetailResponse;
import com.example.consumer_client.MdListMainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;
import com.google.android.material.slider.RangeSlider;
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
    JsonArray mdArray, pay_schedule, pu_start, pu_end;
    String farm_id, farm_name, farm_info, farm_loc, farm_hours;
    Double farm_lat, farm_long;

    private RecyclerView mRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mFarmDetailAdapter;

    Context mContext;

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

        //intent로 값 넘길때
        Intent intent;
        intent=getIntent(); //intent 값 받기

        farm_name=intent.getStringExtra("farm_name");
        farm_info=intent.getStringExtra("farm_info");
        farm_loc=intent.getStringExtra("farm_loc");
        farm_hours=intent.getStringExtra("farm_hours");
        farm_id = intent.getStringExtra("farm_id");
        farm_lat = Double.parseDouble(intent.getStringExtra("farm_lat"));
        farm_long = Double.parseDouble(intent.getStringExtra("farm_long"));

        JsonObject body = new JsonObject();
        body.addProperty("farm_id", farm_id);

        Call<ResponseBody> call = service.farmDetail(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());

                        //md 정보
                        mdArray = res.get("md_data").getAsJsonArray();
                        pay_schedule = res.get("pay_schedule").getAsJsonArray();
                        pu_start = res.get("pu_start").getAsJsonArray();
                        pu_end = res.get("pu_end").getAsJsonArray();
//                        Log.d("md_data", mdArray.toString());
//                        Log.d("pay", pay_schedule.toString());
//                        Log.d("pu_start", pu_start.toString());
//                        Log.d("pu_end", pu_end.toString());

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

        FarmName.setText(farm_name);
        FarmExplain.setText(farm_info);
        FarmLocation.setText(farm_loc);
        FarmHourTime.setText(farm_hours);

        Log.d("farmnam,e", farm_name);
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

//                    mFarmDetailAdapter.setOnItemClickListener(
//                            new FarmDetailAdapter.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(View v, int pos) {
//                                    Log.d("120행", mdL.get(pos).toString()); //클릭한 item 정보 보이기
//                                    Intent intent = new Intent(FarmDetailActivity.this, JointPurchaseActivity.class);
//
//                                    //배열로 보내고 싶은데... 각각 보내는게 맞나...? 일단 putExtra로 값 보내기
//                                    intent.putExtra("farmName", mdL.get(pos).get(0));
//                                    intent.putExtra("mdName",mdL.get(pos).get(1));
//                                    intent.putExtra("storeName",mdL.get(pos).get(2));
//                                    intent.putExtra("paySchedule",mdL.get(pos).get(3));
//                                    intent.putExtra("puStart",mdL.get(pos).get(4));
//                                    intent.putExtra("puEnd",mdL.get(pos).get(5));
////                                intent.putExtra("farmId",farmL.get(pos).get(7));
////                                intent.putExtra("mdCount", mdCL.get(pos).get(0).toString());
////                                Log.d("179행", mdCL.get(pos).get(0).toString());
////                                intent.putExtra("mdName", md_nameL);
////                                intent.putExtra("storeName", store_nameL);
////                                intent.putExtra("puStart", pu_startL);
////                                intent.putExtra("puEnd", pu_endL);
////                                intent.putStringArrayListExtra("mdName", mdNameL);
//                                    startActivity(intent);
//                                }
//                            }
//                    );
//
//                }
//                else{
//                    //같은 화면 다시 띄우기
//                }
//            }
}
