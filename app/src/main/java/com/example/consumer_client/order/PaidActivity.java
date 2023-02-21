package com.example.consumer_client.order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

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

public class PaidActivity extends AppCompatActivity {

    String user_id;
    String mdName;
    int purchaseNum;
    String totalPrice, store_name, store_loc;
    Double price;
    String md_id, store_id, pickupDate, pickupTime; //결제 성공시 order테이블 삽입
    String user_name, mobile_no; //user정보

    OrderInsertService service;
    JsonParser jsonParser;
    JsonObject res, body;

    //주문상세페이지로 갈떄 order_id 넘기기
    String order_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_complete);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OrderInsertService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        mdName = intent.getStringExtra("mdName");
        purchaseNum = Integer.parseInt(intent.getStringExtra("purchaseNum"));
        totalPrice = intent.getStringExtra("totalPrice");
        //price = Double.valueOf(totalPrice);
        // Order 테이블에 값 삽입하기 Post 요청
        md_id = intent.getStringExtra("md_id");
        store_id = intent.getStringExtra("store_id");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");
        //user 정보
        user_name = intent.getStringExtra("user_name");
        mobile_no = intent.getStringExtra("mobile_no");

        //Log.d("가격", String.valueOf(price));

        store_name = intent.getStringExtra("store_name");
        store_loc = intent.getStringExtra("store_loc");

        //setText 설정
        TextView StoreName = (TextView) findViewById(R.id.Paid_Store_Name);
        TextView StoreLoc = (TextView) findViewById(R.id.Paid_Store_Addr);
        TextView PurchaseNum = (TextView) findViewById(R.id.Paid_SelectCount);
        TextView TotalPrice = (TextView) findViewById(R.id.Paid_ToTalPrice);
        TextView PuDate = (TextView) findViewById(R.id.Paid_PU_Date);

        StoreName.setText(store_name);
        StoreLoc.setText(store_loc);
        PurchaseNum.setText(purchaseNum+"개");
        TotalPrice.setText(totalPrice);
        PuDate.setText(pickupDate+ " " + pickupTime);

        //Toast.makeText(getApplicationContext(), " 결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();

        int idx=totalPrice.indexOf("원"); // ex)5000원 이렇게 되어있으니 '원' 문자 자르기
        int order_price= Integer.parseInt(totalPrice.substring(0,idx));

        //주문하기-> Order테이블에 데이터 값 삽입하기Post 요청
        body = new JsonObject();
        body.addProperty("user_id", user_id); //
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);
        body.addProperty("select_qty", purchaseNum);
        body.addProperty("order_price", order_price);
        body.addProperty("pu_date", pickupDate);
        body.addProperty("pu_time", pickupTime);

        Call<ResponseBody> call = service.postOrderData(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        order_id= res.get("order_id").getAsJsonObject().get("LAST_INSERT_ID()").getAsString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PaidActivity.this, "주문하기 post 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주문하기 post", t.getMessage());
            }
        });

        //결제 성공 후 페이지 이동하기
        Button goHome = (Button) findViewById(R.id.Paid_goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaidActivity.this, MainActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        //(OrderDetail페이지 data바꾸면 OrderDetail 페이지로 이동한는 것으로 바꿀것)
        Button goOrderD = (Button) findViewById(R.id.Paid_goOrderDetail);
        goOrderD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaidActivity.this, OrderDetailActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("order_id", order_id);
                intent.putExtra("store_loc", store_loc);
                //intent.putExtra("store_my", mList.get(pos).getStoreLocationFromMe());
                intent.putExtra("store_name", store_name);
                intent.putExtra("md_name", mdName);
                //intent.putExtra("md_id", md_id);
                startActivity(intent);
            }
        });

    }
}