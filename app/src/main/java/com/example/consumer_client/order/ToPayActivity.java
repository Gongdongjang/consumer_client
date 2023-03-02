package com.example.consumer_client.order;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class ToPayActivity extends AppCompatActivity {

    String user_id;
    String md_id, mdName, purchaseNum, JP_ToTalPrice;
    String store_id, store_name, store_loc;
    String pickupDate,pickupTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        TextView ProdName=(TextView) findViewById(R.id.JP_ProdName);
        TextView OrderCount = (TextView) findViewById(R.id.ClientOrderCount);
        TextView OrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);
        TextView MdTotalPrice = (TextView) findViewById(R.id.MdTotalPrice);
        TextView StoreName = (TextView) findViewById(R.id.Pay_Store_Name);
        TextView StoreAddr = (TextView) findViewById(R.id.Pay_Store_Addr);
        TextView PuDate = (TextView) findViewById(R.id.Pay_PU_Date);

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        md_id=intent.getStringExtra("md_id");
        mdName = intent.getStringExtra("mdName");
        purchaseNum = intent.getStringExtra("purchaseNum");
        JP_ToTalPrice = intent.getStringExtra("JP_ToTalPrice");
        store_name = intent.getStringExtra("store_name");
        store_id = intent.getStringExtra("store_id");
        store_loc=intent.getStringExtra("store_loc");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");

        //n 세트만큼 가격 결정.
        //상품남은 재고 >= 세트선택 * prodNum 계산하기 위해
        //int idx=JP_ToTalPrice.indexOf("원"); // ex)5000원 이렇게 되어있으니 '원' 문자 자르기
        //int totalPrice= Integer.parseInt(JP_ToTalPrice.substring(0,idx));

        ProdName.setText(mdName);
        OrderCount.setText(purchaseNum);
        OrderPrice.setText(JP_ToTalPrice);
        StoreName.setText(store_name);
        StoreAddr.setText(store_loc);
        PuDate.setText(pickupDate);
        MdTotalPrice.setText(JP_ToTalPrice);


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
        store_marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
        store_marker.setCustomImageResourceId(R.drawable.ic_shop);
        store_marker.setItemName(store_name); //클릭했을때 스토어 이름 나오기
        store_marker.setTag(0);
        store_marker.setMapPoint(s_MarkPoint);   //좌표입력받아 현위치로 출력

        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(store_marker);

        //
        //결제버튼
        RadioButton PayAgree1=(RadioButton) findViewById(R.id.Pay_Agree1);
        RadioButton PayAgree2=(RadioButton) findViewById(R.id.Pay_Agree2);
        RadioButton PayAgree3=(RadioButton) findViewById(R.id.Pay_Agree3);
        RadioButton PayAgree4=(RadioButton) findViewById(R.id.Pay_Agree4);
        Button Pay_Off_Btn= (Button) findViewById(R.id.Pay_Off_Btn); //결제하기 버튼 비활성화
        Button Pay_On_Btn= (Button) findViewById(R.id.Pay_On_Btn); //결제하기 버튼
        //RadioButton PayCard=(RadioButton) findViewById(R.id.Pay_Card);

//        if (PayAgree1.isChecked() && PayAgree2.isChecked() && PayAgree3.isChecked() && PayAgree4.isChecked()){
//            Pay_Off_Btn.setVisibility(View.GONE);
//            Pay_On_Btn.setVisibility(View.VISIBLE);
//        }

        EditText orderName= (EditText) findViewById(R.id.userName);

        Pay_Off_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PayAgree1.isChecked() && PayAgree2.isChecked() && PayAgree3.isChecked() && PayAgree4.isChecked()){
                    Pay_Off_Btn.setVisibility(View.GONE);
                    Pay_On_Btn.setVisibility(View.VISIBLE);


                }else{
                    Toast.makeText(ToPayActivity.this, "개인정보 및 구매유의사항을 확인하시오.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Pay_On_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if(orderName.getText().toString().equals("")){
                        Toast.makeText(ToPayActivity.this, "입금자 명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent i= new Intent(ToPayActivity.this, PaidActivity.class);
                        i.putExtra("user_id",user_id);
                        i.putExtra("mdName",mdName);
                        i.putExtra("purchaseNum",purchaseNum);
                        i.putExtra("totalPrice",JP_ToTalPrice);
                        i.putExtra("md_id",md_id);
                        i.putExtra("store_id",store_id);
                        i.putExtra("store_name",store_name);
                        i.putExtra("store_loc",store_loc);
                        i.putExtra("pickupDate",pickupDate);
                        i.putExtra("pickupTime",pickupTime);
                        i.putExtra("order_name",orderName.getText().toString());
                        startActivity(i);
                    }
                }
            }
        });
    }
}