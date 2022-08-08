package com.example.consumer_client.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

interface OrderDetailMdService{
    @POST("orderDetailMd")
    Call<ResponseBody> orderDetailMd(@Body JsonObject body);
}

public class OrderDetailActivity extends AppCompatActivity {
    String TAG = OrderDetailActivity.class.getSimpleName();

    String userid;

    JsonObject body;
    OrderDetailMdService service;
    JsonParser jsonParser;
    Context mContext;
    String store_loc, store_my, store_name, md_name, md_comp, md_price, order_id, pu_date;

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
        TextView StoreAddr = (TextView) findViewById(R.id.OrderStoreAddr);
        TextView PuDate = (TextView) findViewById(R.id.OrderPickUpDate);

        Intent intent = getIntent(); //intent 값 받기
        userid = intent.getStringExtra("userid");
        store_loc=intent.getStringExtra("store_loc");
        store_my = intent.getStringExtra("store_my");
        store_name = intent.getStringExtra("store_name");
        md_name = intent.getStringExtra("md_name");
        md_comp = intent.getStringExtra("md_comp");
        md_price = intent.getStringExtra("md_price");
        order_id = intent.getStringExtra("order_id");

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

                        //img 아직 안함
                        MdName.setText(md_name);
                        OrderCount.setText(md_comp);
                        OrderPrice.setText(md_price);
                        StoreName.setText(store_name);
                        StoreAddr.setText(store_loc);
                        PuDate.setText(pu_date);

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
    }
}
