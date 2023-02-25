package com.example.consumer_client.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
}

public class CartListActivity extends AppCompatActivity {
    String TAG = CartListActivity.class.getSimpleName();

    CartService service;
    JsonParser jsonParser;
    JsonObject res;
    Context mContext;
    JsonArray cart_detail, store_count;

    String select_qty, pay_price, pay_comp;

    private RecyclerView mCartRecyclerView, mCartRecyclerView2;
    private ArrayList<CartListInfo> mList, mList2;
    private CartListAdapter mCartListAdapter;
    private CartListAdapter2 mCartListAdapter2;

    String md_name, purchase_num, md_price, prod_set;
    String store_name,store_loc;
    String pu_date, pu_time;
    String user_id, md_id, store_id;

    Integer totalPrice = 0;

    TextView cartTotalPrice, purchaseNum, eachMdTotalPrice;

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
        md_id = intent.getStringExtra("md_id");
        store_id = intent.getStringExtra("store_id");
        select_qty = intent.getStringExtra("select_qty");

//        CartStoreName.setText(store_name);

        firstInit();

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);
        body.addProperty("store_id", store_id);
        body.addProperty("pu_date", pu_date);
        body.addProperty("pu_time", pu_time);
        body.addProperty("purchase_num", purchase_num);
//        purchase_num = purchase_num.substring(0,1);
//        Log.d("purchase_num", purchase_num);

        Call<ResponseBody> call = service.cartListGet(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());
                        cart_detail = res.get("cart_detail").getAsJsonArray();
                        store_count = res.get("store_count").getAsJsonArray();
                        store_name = cart_detail.get(0).getAsJsonObject().get("store_name").getAsString();
                        select_qty = cart_detail.get(0).getAsJsonObject().get("select_qty").getAsString();
                        pay_price = cart_detail.get(0).getAsJsonObject().get("pay_price").getAsString();
                        md_name = cart_detail.get(0).getAsJsonObject().get("md_name").getAsString();
                        pay_comp = cart_detail.get(0).getAsJsonObject().get("pay_comp").getAsString();

                        //어뎁터 적용
                        mCartListAdapter = new CartListAdapter(mList);
                        mCartRecyclerView.setAdapter(mCartListAdapter);
                        //어뎁터 적용
                        mCartListAdapter2 = new CartListAdapter2(mList2);
                        mCartRecyclerView2.setAdapter(mCartListAdapter2);

                        //세로로 세팅
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mCartRecyclerView.setLayoutManager(linearLayoutManager);
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(mContext);
                        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
                        mCartRecyclerView2.setLayoutManager(linearLayoutManager2);

                        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "");
//                        Log.d("=>", cart_detail.getAsJsonObject().toString());
                        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "");
                        purchaseNum = (TextView) findViewById(R.id.CL_PurchaseNum);
                        eachMdTotalPrice = (TextView) findViewById(R.id.CL_EachMdTotalPrice);

                        for(int i=0;i<cart_detail.size();i++){
                            addCart(cart_detail.get(i).getAsJsonObject().get("store_name").getAsString(),
                                    "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + cart_detail.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("md_name").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsString(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt() * cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt() * cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
                                    cart_detail.get(i).getAsJsonObject().get("pay_comp").getAsString());
                            totalPrice += cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt() * cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt() ;
                        }

                        Log.d("@@@@@@@@@@@@@@@@@@@", store_count.get(0).getAsJsonObject().get("COUNT(*)").getAsString());
                        for (int i=0; i<store_count.get(0).getAsJsonObject().get("COUNT(*)").getAsInt();i++){
                            addPrice(cart_detail.get(i).getAsJsonObject().get("select_qty").getAsString(), cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt(),
                                    cart_detail.get(i).getAsJsonObject().get("select_qty").getAsInt()* cart_detail.get(i).getAsJsonObject().get("pay_price").getAsInt());
                        }

                        cartTotalPrice.setText(totalPrice.toString());
//                        Log.d("계산: ", String.valueOf(Integer.parseInt(md_price) * Integer.parseInt(purchase_num)));
//                        for(int i=0;i<1;i++){ //size수정하기
//                            addCart("product Img", md_name, purchase_num, md_price, pu_date + " "+ pu_time, String.valueOf(Integer.parseInt(md_price) * Integer.parseInt(purchase_num)), prod_set);
//                        }
//                        Log.d("cart", user_id);

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
                Intent i = new Intent(v.getContext(), ToPayActivity.class);// 넘어감
                //스토어정보+ dialog 값 전달
                i.putExtra("user_id",user_id);
                i.putExtra("md_id",md_id);
                i.putExtra("mdName",md_name);
                //i.putExtra("purchaseNum",PurchaseNumSpinner.getSelectedItem().toString());
                i.putExtra("purchaseNum",select_qty.toString());
                i.putExtra("JP_ToTalPrice",totalPrice.toString());
                i.putExtra("store_name",store_name);
                i.putExtra("store_id",store_id);
                i.putExtra("store_loc",store_loc);
//                i.putExtra("pickupDate",PickUpDate.getText());
//                i.putExtra("pickupTime",PickUpTime.getText());
                v.getContext().startActivity(i);
            }
        });
    }

    public void firstInit(){
        mCartRecyclerView = findViewById(R.id.CartListRecycler);
        mCartRecyclerView2 = findViewById(R.id.CartPayRecyclerView);
        mList = new ArrayList<>();
        mList2 = new ArrayList<>();
    }

    public void addCart(String storeName, String mdImg, String mdName, String qty, int totalEachPrice, int eachStoreTotalPrice, String mdSet){
        CartListInfo cart = new CartListInfo();

        cart.setStoreName(storeName);
        cart.setMdImg(mdImg);
        cart.setMdName(mdName);
        cart.setQty(qty);
        cart.setEachMdTotalPrice(totalEachPrice);
        cart.setEachStoreTotalPrice(eachStoreTotalPrice);
        cart.setMdSet(mdSet);

        mList.add(cart);
    }


    public void addPrice(String qty, int eachMdPrice, int totalPrice){
        CartListInfo cart = new CartListInfo();

        cart.setQty(qty);
        cart.setEachMdPrice(eachMdPrice);
        cart.setTotalPrice(totalPrice);

        mList2.add(cart);
    }
}