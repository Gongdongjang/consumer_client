package com.example.consumer_client.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;
import com.example.consumer_client.order.ToPayActivity;
import com.google.gson.JsonArray;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface CartService{
    @GET("/cartList")
    Call<ResponseBody> cartListGet(@Query("user_id") String user_id);
    @POST("/cartList")
    Call<ResponseBody> cartListPost(@Body JsonObject body);
    @GET("/cartChecked")
    Call<ResponseBody> cartChecked(@Query("user_id") String user_id, @Query("row_num") int row_num);
}

public class CartListActivity extends AppCompatActivity {
    String TAG = CartListActivity.class.getSimpleName();

    CartService service;
    JsonParser jsonParser;
    JsonObject res;
    public static Context mContext;
    JsonArray cart_detail, store_count, cart_checked;

    String select_qty, pay_price, pay_comp;

    private RecyclerView mCartRecyclerView;
    private ArrayList<CartListInfo> mList, mList2;
    private CartListAdapter mCartListAdapter;
//    private CartListAdapter2 mCartListAdapter2;

    String md_name, purchase_num, md_price, prod_set;
    String store_name,store_loc;
    String pu_date, pu_time;

    public String user_id, md_id, store_id;

    Integer totalPrice = 0;
    TextView cartTotalPrice, purchaseNum;
    Button goToPay;

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

        cartTotalPrice = (TextView) findViewById(R.id.CartTotalPrice);
        goToPay = (Button) findViewById(R.id.CartPayButton);

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
        select_qty = intent.getStringExtra("select_qty");


        firstInit();

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);
        body.addProperty("pu_date", pu_date);
        body.addProperty("pu_time", pu_time);
        body.addProperty("purchase_num", purchase_num);

        Call<ResponseBody> call = service.cartListGet(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());
                        cart_detail = res.get("cart_detail").getAsJsonArray();
                        store_count = res.get("store_count").getAsJsonArray();
                        if (cart_detail.size() != 0){
                            store_name = cart_detail.get(0).getAsJsonObject().get("store_name").getAsString();
                            select_qty = cart_detail.get(0).getAsJsonObject().get("select_qty").getAsString();
                            pay_price = cart_detail.get(0).getAsJsonObject().get("pay_price").getAsString();
                            md_name = cart_detail.get(0).getAsJsonObject().get("md_name").getAsString();
                            pay_comp = cart_detail.get(0).getAsJsonObject().get("pay_comp").getAsString();
                            store_id = cart_detail.get(0).getAsJsonObject().get("store_id").getAsString();
                            md_id = cart_detail.get(0).getAsJsonObject().get("md_id").getAsString();
                            pu_time = cart_detail.get(0).getAsJsonObject().get("cart_pu_time").getAsString();
                            pu_date = cart_detail.get(0).getAsJsonObject().get("cart_pu_date").getAsString();
                            store_loc = cart_detail.get(0).getAsJsonObject().get("store_loc").getAsString();
                        }

                        //어뎁터 적용
                        mCartListAdapter = new CartListAdapter(mList);
                        mCartRecyclerView.setAdapter(mCartListAdapter);
//                        //어뎁터 적용
//                        mCartListAdapter2 = new CartListAdapter2(mList2);
//                        mCartRecyclerView2.setAdapter(mCartListAdapter2);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mCartRecyclerView.setLayoutManager(linearLayoutManager);
//                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext);
//                        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//                        mCartRecyclerView2.setLayoutManager(linearLayoutManager2);

                        purchaseNum = (TextView) findViewById(R.id.CL_PurchaseNum);

                        mCartListAdapter.setOnItemClickListener(new CartListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                String user_id = mList.get(pos).getUserId();
                                String md_name = mList.get(pos).getMdName();
                                String store_name = mList.get(pos).getStoreName();
                            }

                            @Override
                            public void onDeleteClick(View v, int pos) {
                                mList.remove(pos);
                                mCartListAdapter.notifyItemRemoved(pos);
                            }
                        });
                        for(int i=0;i<cart_detail.size();i++){
                            addCart(cart_detail.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + cart_detail.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt() * cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
                                    cart_detail.get(i).getAsJsonObject().get("pay_comp").getAsString());
                            totalPrice += cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt() * cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt() ;
                        }

//                        for (int i=0; i<store_count.get(0).getAsJsonObject().get("COUNT(*)").getAsInt();i++){
//                            addPrice(cart_detail.get(i).getAsJsonObject().get("select_qty").getAsString(), cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
//                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt()* cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt());
//                        }
//                        cartTotalPrice.setText(totalPrice.toString());
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

        goToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean is_checked = mCartListAdapter.checked;
                if (is_checked) {
                    String store_id = mCartListAdapter.store_id_pub;
                    String md_id = mCartListAdapter.md_id_pub;
                    int pos = mCartListAdapter.pos_item;

                    // retrofit 통신 -> 해당 pos의 데이터 가져오기
                    Call<ResponseBody> call = service.cartChecked(user_id, pos);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    res = (JsonObject) jsonParser.parse(response.body().string());
                                    cart_checked = res.get("cart_checked").getAsJsonArray();
                                    if (cart_detail.size() != 0) {
                                        store_name = cart_checked.get(0).getAsJsonObject().get("store_name").getAsString();
                                        select_qty = cart_checked.get(0).getAsJsonObject().get("select_qty").getAsString();
                                        pay_price = cart_checked.get(0).getAsJsonObject().get("pay_price").getAsString();
                                        md_name = cart_checked.get(0).getAsJsonObject().get("md_name").getAsString();
                                        pay_comp = cart_checked.get(0).getAsJsonObject().get("pay_comp").getAsString();
                                        pu_time = cart_checked.get(0).getAsJsonObject().get("cart_pu_time").getAsString();
                                        pu_date = cart_checked.get(0).getAsJsonObject().get("cart_pu_date").getAsString();
                                        store_loc = cart_checked.get(0).getAsJsonObject().get("store_loc").getAsString();

                                        Intent i = new Intent(v.getContext(), ToPayActivity.class);// 넘어감
                                        i.putExtra("user_id",user_id);
                                        i.putExtra("md_id",md_id);
                                        i.putExtra("mdName",md_name);
                                        i.putExtra("purchaseNum",select_qty);
                                        i.putExtra("JP_ToTalPrice",String.valueOf(Integer.valueOf(pay_price) * Integer.valueOf(select_qty)+"원"));
                                        i.putExtra("store_name",store_name);
                                        i.putExtra("store_id",store_id);
                                        i.putExtra("store_loc",store_loc);
                                        i.putExtra("pickupDate", pu_date);
                                        i.putExtra("pickupTime", pu_time);
                                        v.getContext().startActivity(i);
                                    }
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
                else {
                    Toast.makeText(mContext, "상품을 선택해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void firstInit(){
        mCartRecyclerView = findViewById(R.id.CartListRecycler);
//        mCartRecyclerView2 = findViewById(R.id.CartPayRecyclerView);
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();
    }

    public void addCart(String storeName, String mdImg, String mdName, String qty, int eachMdPrice, int eachStoreTotalPrice, String mdSet){
        CartListInfo cart = new CartListInfo();

        cart.setStoreName(storeName);
        cart.setMdImg(mdImg);
        cart.setMdName(mdName);
        cart.setQty(qty);
        cart.setEachStoreTotalPrice(eachStoreTotalPrice);
        cart.setEachMdPrice(eachMdPrice);
        cart.setMdSet(mdSet);

        mList.add(cart);
    }

//
//    public void addPrice(String qty, int eachMdPrice, int totalPrice){
//        CartListInfo cart = new CartListInfo();
//
//        cart.setQty(qty);
//        cart.setEachMdPrice(eachMdPrice);
//        cart.setTotalPrice(totalPrice);
//
//        mList2.add(cart);
//    }
}