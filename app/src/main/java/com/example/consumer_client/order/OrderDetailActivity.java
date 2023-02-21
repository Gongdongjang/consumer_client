package com.example.consumer_client.order;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.review.ReviewActivity;
import com.google.gson.JsonArray;
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

interface OrderDetailMdService{
    @POST("orderDetailMd")
    Call<ResponseBody> orderDetailMd(@Body JsonObject body);
}

public class OrderDetailActivity extends AppCompatActivity {
    String TAG = OrderDetailActivity.class.getSimpleName();

    String user_id;

    JsonObject body;
    OrderDetailMdService service;
    JsonParser jsonParser;
    JsonArray order_detail;
    Context mContext;
    String store_loc, store_my, store_name, md_name, md_qty, md_price, order_id, pu_date, md_status, md_fin_price;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonParser = new JsonParser();
        service = retrofit.create(OrderDetailMdService.class);

        mContext = this;

        ImageView MdImgThumbnail = (ImageView) findViewById(R.id.ClientOrderProdIMG);
        TextView MdName = (TextView) findViewById(R.id.JP_ProdName);
        TextView OrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView OrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);
        TextView StoreName = (TextView) findViewById(R.id.OrderStoreName);
        TextView StoreName0 = (TextView) findViewById(R.id.OrderStoreName0);
        TextView StoreAddr = (TextView) findViewById(R.id.OrderStoreAddr);

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        store_loc=intent.getStringExtra("store_loc");
        //store_my = intent.getStringExtra("store_my"); >> 이건 어디에 쓰냐..?
        store_name = intent.getStringExtra("store_name");
        md_name = intent.getStringExtra("md_name");
        //md_qty = intent.getStringExtra("md_qty");
        //md_price = intent.getStringExtra("md_price");
        order_id = intent.getStringExtra("order_id");
        //md_status = intent.getStringExtra("md_status");

        //픽업상태 세팅
        TextView order_status= (TextView) findViewById(R.id.order_status);
        ImageView order_status1= (ImageView) findViewById(R.id.order_status1);
        ImageView order_status2= (ImageView) findViewById(R.id.order_status2);
        ImageView order_status3= (ImageView) findViewById(R.id.order_status3);
        ImageView order_status4= (ImageView) findViewById(R.id.order_status4);
        ImageView order_status5= (ImageView) findViewById(R.id.order_status5);
        ImageView order_status6= (ImageView) findViewById(R.id.order_status6);
        TextView txt_order_status1= (TextView) findViewById(R.id.txt_order_status1);
        TextView txt_order_status2= (TextView) findViewById(R.id.txt_order_status2);
        TextView txt_order_status3= (TextView) findViewById(R.id.txt_order_status3);
        TextView txt_order_status4= (TextView) findViewById(R.id.txt_order_status4);
        TextView txt_order_status5= (TextView) findViewById(R.id.txt_order_status5);
        TextView txt_order_status6= (TextView) findViewById(R.id.txt_order_status6);

        body = new JsonObject();
        body.addProperty("order_id", order_id);

        Call<ResponseBody> call = service.orderDetailMd(body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        pu_date = res.get("pu_date").getAsString();
                        order_detail= res.get("order_detail").getAsJsonArray();

                        //img 아직 안함
                        MdName.setText(md_name);
                        OrderCount.setText(order_detail.get(0).getAsJsonObject().get("order_select_qty").getAsString()+"세트");
                        //md_fin_price = String.valueOf(Integer.parseInt(md_price) * Integer.parseInt(md_qty));
                        OrderPrice.setText(order_detail.get(0).getAsJsonObject().get("order_price").getAsString()+"원");
                        StoreName.setText(store_name);
                        StoreName0.setText(store_name);
                        StoreAddr.setText(store_loc);
                        //PuDate.setText(pu_date);
                        //ProdStatus.setText(md_status);


                        //md_status= order_detail.get(0).getAsJsonObject().get("order_md_status").getAsString();
                        md_status= res.get("md_status").getAsJsonObject().get("stk_confirm").getAsString();
                        //if(md_status.equals(""))

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

        //지도
        MapView mapView = new MapView(this);
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(store_lat, store_long), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.store_map_view);
        mapViewContainer.addView(mapView);

        //스토어위치 마커 아이콘 띄우기
        MapPoint f_MarkPoint = MapPoint.mapPointWithGeoCoord(store_lat, store_long);  //마커찍기

        MapPOIItem store_marker=new MapPOIItem();
        store_marker.setItemName(store_name); //클릭했을때 가게이름 나오기
        store_marker.setTag(0);
        store_marker.setMapPoint(f_MarkPoint);   //좌표입력받아 현위치로 출력

        //  (클릭 전)기본으로 제공하는 BluePin 마커 모양의 색.
        store_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // (클릭 후) 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        store_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(store_marker);

    }
}
