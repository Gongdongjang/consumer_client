package com.example.consumer_client.my_town;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.consumer_client.MainActivity;
import com.example.consumer_client.ProgressDialog;
import com.example.consumer_client.R;
import com.example.consumer_client.store.StoreDetailActivity;
import com.example.consumer_client.store.StoreTotalAdapter;

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
import retrofit2.http.GET;

interface StoreMapService {
    @GET("/storeView")
    Call<ResponseBody> getStoreData();
}

class StoreData{
    String storeId;
    String storeName;
    Double storeLat;
    Double storeLong;

    public String getStoreId() {return storeId;}

    public void setStoreId(String storeId) {this.storeId = storeId;}

    public String getStoreName() {return storeName;}

    public void setStoreName(String storeName) {this.storeName = storeName;}

    public Double getStoreLat() {return storeLat;}

    public void setStoreLat(Double storeLat) {this.storeLat = storeLat;}

    public Double getStoreLong() {return storeLong;}

    public void setStoreLong(Double storeLong) {this.storeLong = storeLong;}
}

public class StoreMap extends AppCompatActivity implements MapView.POIItemEventListener {
    ProgressDialog customProgressDialog;
    StoreMapService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray storeArray;

    String user_id, standard_address;
    double myTownLat;
    double myTownLong;
    ArrayList<StoreData> dataArr= new ArrayList<StoreData>();

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_map);

        mContext = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StoreMapService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        standard_address=intent.getStringExtra("standard_address");

        final Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(standard_address,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = address.get(0);
        myTownLat = location.getLatitude();
        myTownLong=location.getLongitude();

        MapView mapView = new MapView(mContext);
        mapView.setPOIItemEventListener(this);
        //StoreData
        StoreData data = new StoreData();
        ArrayList<MapPOIItem> storeLoc_marker= new ArrayList<>();

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(mContext);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //로딩창 보여주기
        customProgressDialog.show();

        Call<ResponseBody> call = service.getStoreData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    storeArray = res.get("store_result").getAsJsonArray();  //json배열

                    //지도
                    //MapView mapView = new MapView(mContext);

                    // 중심점 변경 (사용자가 설정한 위치로 지도중심점 띄우기)
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(myTownLat, myTownLong), true);

                    // 줌 레벨 변경
                    mapView.setZoomLevel(1, true);
                    // 줌 인
                    mapView.zoomIn(true);
                    // 줌 아웃
                    mapView.zoomOut(true);

                    ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
                    mapViewContainer.addView(mapView);

                    //지도 적용
                    final Geocoder geocoder = new Geocoder(getApplicationContext());

                    //listCount=storeArray.size();
                    for(int i=0;i<storeArray.size() ;i++) {
                        //스토어위치->위도, 경도 구하기
                        String store_id = storeArray.get(i).getAsJsonObject().get("store_id").getAsString();
                        String store_name = storeArray.get(i).getAsJsonObject().get("store_name").getAsString();
                        String store_loc = storeArray.get(i).getAsJsonObject().get("store_loc").getAsString();
                        List<Address> address = geocoder.getFromLocationName(store_loc, 10);
                        Address location = address.get(0);
                        double store_lat = location.getLatitude();
                        double store_long = location.getLongitude();

                        //StoreData에 값 추가
                        data.setStoreId(store_id);
                        data.setStoreName(store_name);
                        data.setStoreLat(store_lat);
                        data.setStoreLong(store_long);
                        dataArr.add(data);
                        //Log.d("storeMap0", dataArr.get(i).getStoreName()+dataArr.get(i).getStoreLat());

                        MapPOIItem marker = new MapPOIItem();
                        //마커이미지 변경
                        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        marker.setCustomImageResourceId(R.drawable.ic_shop);
                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(dataArr.get(i).getStoreLat(), dataArr.get(i).getStoreLong()));
                        marker.setItemName(dataArr.get(i).getStoreName()); //클릭했을때 가게이름 나오기
                        marker.setTag(Integer.parseInt(dataArr.get(i).getStoreId()));   //sotre_id값 넘기기!!!!!!! 드디어
                        storeLoc_marker.add(marker);
                    }
                    mapView.addPOIItems(storeLoc_marker.toArray(new MapPOIItem[storeLoc_marker.size()]));
                    customProgressDialog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StoreMap.this, "지도로 스토어위치보기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
            }
        });

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Intent intent = new Intent(StoreMap.this, StoreDetailActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("storeid", String.valueOf(mapPOIItem.getTag())); //store_id 넘기기 드디어!!
        startActivity(intent);
    }

    // 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}

