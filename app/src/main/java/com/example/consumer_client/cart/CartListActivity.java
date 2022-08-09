package com.example.consumer_client.cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;

public class CartListActivity extends AppCompatActivity {

    String mdName, purchaseNum, prodPrice;
    String store_name,store_loc, store_lat, store_long;
    String pickupDate,pickupTime;
    String user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cart);

//        TextView ProdName=(TextView) findViewById(R.id.JP_ProdName);
//        TextView OrderCount = (TextView) findViewById(R.id.ClientOrderCount);
//        TextView OrderPrice = (TextView) findViewById(R.id.ClientOrderPrice);
//        TextView StoreName = (TextView) findViewById(R.id.Pay_Store_Name);
//        TextView StoreAddr = (TextView) findViewById(R.id.Pay_Store_Addr);
//        TextView PuDate = (TextView) findViewById(R.id.Pay_PU_Date);

        Intent intent = getIntent(); //intent 값 받기
        mdName = intent.getStringExtra("mdName");
        purchaseNum = intent.getStringExtra("purchaseNum");
        prodPrice = intent.getStringExtra("prodPrice");
        store_name = intent.getStringExtra("store_name");
        store_loc=intent.getStringExtra("store_loc");
        store_lat = intent.getStringExtra("store_lat");
        store_long = intent.getStringExtra("store_long");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");
        user_id = intent.getStringExtra("user_id");

    }
}