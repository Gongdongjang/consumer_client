package com.example.consumer_client.order;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.PayActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface OrderInsertService{
    @POST("orderInsert")
    Call<ResponseBody> postOrderData(@Body JsonObject body);
}

public class ToPayActivity extends AppCompatActivity {

    String user_id;
    String md_id, mdName, purchaseNum, prodPrice;
    String store_id, store_name, store_loc;
    String pickupDate,pickupTime;

    OrderInsertService service;
    JsonParser jsonParser;
    JsonObject res, body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OrderInsertService.class);
        jsonParser = new JsonParser();

        TextView ProdName=(TextView) findViewById(R.id.JP_ProdName);
        TextView OrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView OrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);
        TextView StoreName = (TextView) findViewById(R.id.Pay_Store_Name);
        TextView StoreAddr = (TextView) findViewById(R.id.Pay_Store_Addr);
        TextView PuDate = (TextView) findViewById(R.id.Pay_PU_Date);

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        md_id=intent.getStringExtra("md_id");
        mdName = intent.getStringExtra("mdName");
        purchaseNum = intent.getStringExtra("purchaseNum");
        prodPrice = intent.getStringExtra("prodPrice");
        store_name = intent.getStringExtra("store_name");
        store_id = intent.getStringExtra("store_id");
        store_loc=intent.getStringExtra("store_loc");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");

        //n 세트만큼 가격 결정.
        Log.d("purchaseNum",purchaseNum);
        for(int i=1; i<=Integer.parseInt(purchaseNum); i++){
            prodPrice= String.valueOf(Integer.parseInt(prodPrice)* i);
        }

        ProdName.setText(mdName);
        OrderCount.setText(purchaseNum);
        OrderPrice.setText(prodPrice);
        StoreName.setText(store_name);
        StoreAddr.setText(store_loc);
        PuDate.setText(pickupDate);

        //주문하기-> Order테이블에 데이터 값 삽입하기Post 요청
        body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);
        body.addProperty("select_qty", purchaseNum);
        body.addProperty("pu_date", pickupDate);
        body.addProperty("pu_time", pickupTime);

        Call<ResponseBody> call = service.postOrderData(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        Toast.makeText(ToPayActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ToPayActivity.this, "주문하기 post 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주문하기 post", t.getMessage());
            }
        });

        final Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address= null;
        try {
            address = geocoder.getFromLocationName(store_loc,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = address.get(0);
        double store_lat=location.getLatitude();
        double store_long=location.getLongitude();

        //----------------지도
        MapView mapView = new MapView(this);
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(store_lat, store_long), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //스토어위치 마커 아이콘 띄우기
        MapPoint s_MarkPoint = MapPoint.mapPointWithGeoCoord(store_lat, store_long);  //마커찍기

        MapPOIItem store_marker=new MapPOIItem();
        store_marker.setItemName(store_name); //클릭했을때 스토어 이름 나오기
        store_marker.setTag(0);
        store_marker.setMapPoint(s_MarkPoint);   //좌표입력받아 현위치로 출력

        //  (클릭 전)기본으로 제공하는 BluePin 마커 모양의 색.
        store_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // (클릭 후) 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        store_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(store_marker);

        //
        //결제버튼
        RadioButton PayAgree1=(RadioButton) findViewById(R.id.Pay_Agree1);
        RadioButton PayAgree2=(RadioButton) findViewById(R.id.Pay_Agree2);
        RadioButton PayAgree3=(RadioButton) findViewById(R.id.Pay_Agree3);
        Button PayBtn= (Button) findViewById(R.id.Pay_Btn); //결제하기 버튼
        RadioButton PayCard=(RadioButton) findViewById(R.id.Pay_Card);

        PayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (PayCard.isChecked()){
//                }
                if (PayAgree1.isChecked() && PayAgree2.isChecked() && PayAgree3.isChecked()) // 결제동의하기
                {
                    Intent i= new Intent(ToPayActivity.this, PayActivity.class);
                    i.putExtra("user_id",user_id);
                    i.putExtra("mdName",mdName);
                    i.putExtra("purchaseNum",purchaseNum);
                    i.putExtra("prodPrice",prodPrice);
                    startActivity(i);
                }else{
                    Toast.makeText(ToPayActivity.this, "개인정보 및 구매유의사항을 확인하시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}