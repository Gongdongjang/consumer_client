package com.example.consumer_client.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface CartService{
    @POST("/cartListView")
    Call<ResponseBody> cartList(@Body JsonObject body);
}

public class CartListActivity extends AppCompatActivity {
    String TAG = CartListActivity.class.getSimpleName();

    CartService service;
    JsonParser jsonParser;
    JsonObject res;
    Context mContext;

    private RecyclerView mCartRecyclerView;
    private ArrayList<CartListInfo> mList;
    private CartListAdapter mCartListAdapter;

    String md_name, purchase_num, md_price, prod_set;
    String store_name,store_loc;
    String pu_date, pu_time;
    String user_id, md_id, store_id;

    TextView CartStoreName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_cart);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(CartService.class);
        jsonParser = new JsonParser();

        mContext = this;

        CartStoreName = (TextView) findViewById(R.id.CartStoreName);

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        md_name = intent.getStringExtra("mdName");
        purchase_num = intent.getStringExtra("purchaseNum");
        prod_set = intent.getStringExtra("prod_set");
        md_price = intent.getStringExtra("prodPrice");
        store_name = intent.getStringExtra("store_name");
        store_loc=intent.getStringExtra("store_loc");
        pu_date = intent.getStringExtra("pickupDate");
        pu_time = intent.getStringExtra("pickupTime");
        md_id = intent.getStringExtra("md_id");
        store_id = intent.getStringExtra("store_id");

        CartStoreName.setText(store_name);

        firstInit();

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);
        body.addProperty("pu_date", pu_date);
        body.addProperty("pu_time", pu_time);
        body.addProperty("purchase_num", purchase_num);
        purchase_num = purchase_num.substring(0,1);
        Log.d("purchase_num", purchase_num);

        Call<ResponseBody> call = service.cartList(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());

                        //어뎁터 적용
                        mCartListAdapter = new CartListAdapter(mList);
                        mCartRecyclerView.setAdapter(mCartListAdapter);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mCartRecyclerView.setLayoutManager(linearLayoutManager);

                        Log.d("계산: ", String.valueOf(Integer.parseInt(md_price) * Integer.parseInt(purchase_num)));
                        for(int i=0;i<1;i++){ //size수정하기
                            addCart("product Img", md_name, purchase_num, md_price, pu_date + " "+ pu_time, String.valueOf(Integer.parseInt(md_price) * Integer.parseInt(purchase_num)), prod_set);
                        }
                        Log.d("cart", user_id);

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

    public void firstInit(){
        mCartRecyclerView = findViewById(R.id.CartGetRecycler);
        mList = new ArrayList<>();
    }

    public void addCart(String storeProdImgView, String mdName, String paycount, String mdPrice, String puDate, String totalPrice, String prod_set){
        CartListInfo cart = new CartListInfo();

        cart.setStoreProdImgView(storeProdImgView);
        cart.setMdName(mdName);
        cart.setPayCount(paycount);
        cart.setMdPrice(mdPrice);
        cart.setPuDate(puDate);
        cart.setTotalPrice(totalPrice);
        cart.setProdSet(prod_set);
        cart.setProdCount(paycount);

        mList.add(cart);
    }
}