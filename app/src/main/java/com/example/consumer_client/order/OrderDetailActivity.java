package com.example.consumer_client.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    String store_loc, store_my, store_name, md_name, order_id, pu_date, md_status, md_qty, md_total_price;

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
        order_id = intent.getStringExtra("order_id");

        //주문취소 버튼, 리뷰하기 버튼 활성&비활성화
        Button btn_orderDetail = (Button) findViewById(R.id.btn_orderDetail);

        //픽업상태 세팅
        TextView order_status= (TextView) findViewById(R.id.order_status); //+픽업날짜
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
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        order_detail= res.get("order_detail").getAsJsonArray();

                        md_qty= order_detail.get(0).getAsJsonObject().get("order_select_qty").getAsString();
                        md_total_price= order_detail.get(0).getAsJsonObject().get("order_price").getAsString();
                        pu_date= order_detail.get(0).getAsJsonObject().get("order_pu_time").getAsString();

                        //img 아직 안함
                        MdName.setText(md_name);
                        OrderCount.setText(md_qty+"세트");
                        OrderPrice.setText(md_total_price+"원");
                        StoreName.setText(store_name);
                        StoreName0.setText(store_name);
                        StoreAddr.setText(store_loc);

                        //상품 현재 픽업 상태 세팅
                        md_status= res.get("md_status").getAsJsonObject().get("stk_confirm").getAsString();
                        switch (md_status) {
                            case "공동구매 중":
                                order_status.setText("픽업 예정일 : " + pu_date);
                                order_status1.setImageResource(R.drawable.order_status_off);
                                txt_order_status1.setTextColor(Color.parseColor("#1EAA95"));

                                //주문취소 버튼 활성화
                                btn_orderDetail.setVisibility(View.VISIBLE);
                                btn_orderDetail.setText("주문 취소하기");

                                break;
                            case "공동구매 완료":
                                order_status.setText("픽업 예정일 : " + pu_date);
                                order_status2.setImageResource(R.drawable.order_status_off);
                                txt_order_status2.setTextColor(Color.parseColor("#1EAA95"));
                                break;
                            case "상품 준비 중":
                                order_status.setText("픽업 예정일 : " + pu_date);
                                order_status3.setImageResource(R.drawable.order_status_off);
                                txt_order_status3.setTextColor(Color.parseColor("#1EAA95"));
                                break;
                            case "배송 중":
                                order_status.setText("픽업 예정일 : " + pu_date);
                                order_status4.setImageResource(R.drawable.order_status_off);
                                txt_order_status4.setTextColor(Color.parseColor("#1EAA95"));
                                break;
                            case "스토어 도착":
                                order_status.setText("픽업 예정일 : " + pu_date);
                                order_status5.setImageResource(R.drawable.order_status_off);
                                txt_order_status5.setTextColor(Color.parseColor("#1EAA95"));
                                break;
                            case "픽업완료":
                                order_status.setText("픽업 완료");
                                order_status6.setImageResource(R.drawable.order_status_off);
                                txt_order_status6.setTextColor(Color.parseColor("#1EAA95"));

                                //리뷰버튼 활성화
                                btn_orderDetail.setVisibility(View.VISIBLE);
                                btn_orderDetail.setText("리뷰 작성");
                                break;
                            default:  //아마 공구 취소..?
                                order_status.setText(md_status);
                                break;
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

        btn_orderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btn_orderDetail.getText().toString().equals("리뷰 작성")){
                    Intent intent = new Intent(OrderDetailActivity.this, ReviewActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("md_name", md_name);
                    intent.putExtra("md_qty", md_qty);
                    intent.putExtra("md_fin_price", md_total_price);
                    startActivity(intent);
                } else{ //주문취소

                    //주문취소 팝업 만들고 취소 Retrofit 작성하기

                }
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
        store_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        store_marker.setCustomImageResourceId(R.drawable.ic_shop);
        store_marker.setItemName(store_name); //클릭했을때 가게이름 나오기
        store_marker.setTag(0);
        store_marker.setMapPoint(f_MarkPoint);   //좌표입력받아 현위치로 출력

        mapView.addPOIItem(store_marker);
    }
}
