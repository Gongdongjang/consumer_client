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

    String user_id;
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

                    //StoreData
                    StoreData data = new StoreData();

                    //지도
                    MapView mapView = new MapView(mContext);

                    // 중심점 변경 (사용자가 설정한 위치로 지도중심점 띄우기)
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.5912999, 127.0221068), true);
                    //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(store_lat, store_long), true);

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

                    ArrayList<MapPOIItem> storeLoc_marker= new ArrayList<>();
                    MapPOIItem marker = new MapPOIItem();

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

                        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(dataArr.get(i).getStoreLat(), dataArr.get(i).getStoreLong()));
                        marker.setItemName(dataArr.get(i).getStoreName()); //클릭했을때 가게이름 나오기
                        storeLoc_marker.add(marker);
                        mapView.addPOIItems(storeLoc_marker.toArray(new MapPOIItem[0]));

                        //클릭했을때 이제 id값 넘기기!!
                    }

                    customProgressDialog.dismiss();

//                    for (int i=0; i< storeArray.size(); i++) {
//                        //MapPOIItem marker = new MapPOIItem();
//                        //Log.d("storeMapName", String.valueOf(dataArr.get(i)));
//                        //Log.d("storeMapName", dataArr.get(i).getStoreName());
//                        //marker.setMapPoint(MapPoint.mapPointWithGeoCoord(dataArr.get(i).getStoreLat(), dataArr.get(i).getStoreLong()));
//                        //marker.setItemName(dataArr.get(i).getStoreName()); //클릭했을때 가게이름 나오기
//                        //storeLoc_marker.add(marker);
//                    }
//                    //mapView.addPOIItems(storeLoc_marker.toArray(new MapPOIItem[0]));

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
        //Toast.makeText(this, "Clicked " + mapPOIItem.getItemName() + " Callout Balloon", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(StoreMap.this, StoreDetailActivity.class);
        intent.putExtra("user_id", user_id);
        //intent.putExtra("storeid", mapPOIItem.);
        //스토어이름 index구해서 똑같이 storeid 넘겨야 하는걸까?
        startActivity(intent);

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
