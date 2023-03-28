package com.gdjang.consumer_client.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gdjang.consumer_client.MainActivity;
import com.gdjang.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PaidActivity extends AppCompatActivity {

    String user_id;
    String mdName;
    int purchaseNum;
    String totalPrice, store_name, store_loc;

    //결제 성공시 order테이블 삽입
    String md_id, store_id, pickupDate, pickupTime, order_name; //(입금자명도 추가)


    //주문상세페이지로 갈떄 order_id 넘기기
    String order_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_complete);

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        mdName = intent.getStringExtra("mdName");
        purchaseNum = Integer.parseInt(intent.getStringExtra("purchaseNum"));
        totalPrice = intent.getStringExtra("totalPrice");
        //price = Double.valueOf(totalPrice);
        // Order 테이블에 값 삽입하기 Post 요청

        order_id = intent.getStringExtra("order_id");
        md_id = intent.getStringExtra("md_id");
        store_id = intent.getStringExtra("store_id");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");
        order_name = intent.getStringExtra("order_name");
        //Log.d("가격", String.valueOf(price));

        store_name = intent.getStringExtra("store_name");
        store_loc = intent.getStringExtra("store_loc");

        //setText 설정
        TextView Paid_MdName= (TextView) findViewById(R.id.Paid_MdName);
        TextView StoreName = (TextView) findViewById(R.id.Paid_Store_Name);
        TextView StoreLoc = (TextView) findViewById(R.id.Paid_Store_Addr);
        TextView PurchaseNum = (TextView) findViewById(R.id.Paid_SelectCount);
        TextView TotalPrice = (TextView) findViewById(R.id.Paid_ToTalPrice);
        TextView PuDate = (TextView) findViewById(R.id.Paid_PU_Date);

        Paid_MdName.setText(mdName);
        StoreName.setText(store_name);
        StoreLoc.setText(store_loc);
        PurchaseNum.setText(purchaseNum+"세트");
        TotalPrice.setText(totalPrice);
        PuDate.setText("픽업 일시 "+ pickupDate+ " " + pickupTime);

        //1. 결제 성공 후 메인페이지로
        Button goHome = (Button) findViewById(R.id.Paid_goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaidActivity.this, MainActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        //2. 결제 성공 후 주문상세페이지로
        Button goOrderD = (Button) findViewById(R.id.Paid_goOrderDetail);
        goOrderD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaidActivity.this, OrderDetailActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("store_loc", store_loc);
                intent.putExtra("store_name", store_name);
                intent.putExtra("md_name", mdName);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaidActivity.this, MainActivity.class);
        Toast.makeText(PaidActivity.this, "주문이 완료되어 홈화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
        intent.putExtra("user_id", user_id);
        startActivity(intent);
    }
}
