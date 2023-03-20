package com.example.consumer_client.order;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.consumer_client.R;
import com.example.consumer_client.agree.Agree3;
import com.example.consumer_client.agree.Agree4;
import com.example.consumer_client.md.JointPurchaseActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;

public class ToPayActivity extends AppCompatActivity {

    String user_id;
    String md_id, mdName, purchaseNum, JP_ToTalPrice, mdimg_thumbnail;
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
        ImageView ClientOrderProdIMG = findViewById(R.id.ClientOrderProdIMG);

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
        mdimg_thumbnail = intent.getStringExtra("mdimg_thumbnail");

        ProdName.setText(mdName);
        OrderCount.setText(purchaseNum);
        OrderPrice.setText(JP_ToTalPrice);
        StoreName.setText(store_name);
        StoreAddr.setText(store_loc);
        String p= "픽업 예정일   "+ pickupDate + " ("+pickupTime+")";
        PuDate.setText(p);
        MdTotalPrice.setText(JP_ToTalPrice);
        Glide.with(ToPayActivity.this)
                .load(mdimg_thumbnail)
                .into(ClientOrderProdIMG);

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

        //결제버튼
        RadioButton PayAgree3=(RadioButton) findViewById(R.id.Pay_Agree3);
        RadioButton PayAgree4=(RadioButton) findViewById(R.id.Pay_Agree4);
        TextView PayAgree3_Content=(TextView) findViewById(R.id.Pay_Agree3_Content);
        TextView PayAgree4_Content=(TextView) findViewById(R.id.Pay_Agree4_Content);
        Button Pay_Off_Btn= (Button) findViewById(R.id.Pay_Off_Btn); //결제하기 버튼 비활성화
        Button Pay_On_Btn= (Button) findViewById(R.id.Pay_On_Btn); //결제하기 버튼

        EditText orderName= (EditText) findViewById(R.id.userName);

        PayAgree3_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Agree3.class);
                startActivity(intent);
            }
        });
        PayAgree4_Content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Agree4.class);
                startActivity(intent);
            }
        });

        Pay_Off_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( PayAgree3.isChecked() && PayAgree4.isChecked()){
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