package com.example.consumer_client.shopping_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;
import com.example.consumer_client.order.OrderDetailActivity;
import com.example.consumer_client.order.OrderListInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface OrderDetails2Service {
    @POST("/orderDetailView")
    Call<ResponseBody> orderDetailsData(@Body JsonObject body);
}

public class OrderList extends AppCompatActivity {
    JsonParser jsonParser;
    OrderDetails2Service service;
    JsonObject res;
    JsonArray orderDetailArray, pu_date;
    private View view;
    private RecyclerView mOrderListRecyclerView;
    private OrderList2Adapter mOrderListAdapter;
    private ArrayList<OrderListInfo> mList;
    Activity mActivity;
    String user_id;
    String isPickuped, pickupDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_order_list);

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OrderDetails2Service.class);
        jsonParser = new JsonParser();
//
//        // Inflate the layout for this fragment
//        view= inflater.inflate(R.layout.activity_order_list, container, false);

        JsonObject body = new JsonObject();
        body.addProperty("user_id", user_id);
        Call<ResponseBody> call = service.orderDetailsData(body);

        firstInit();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());

                    orderDetailArray = res.get("order_detail").getAsJsonArray();
                    pu_date = res.get("pu_date").getAsJsonArray();

                    //어뎁터 적용
                    mOrderListAdapter = new OrderList2Adapter(mList);
                    mOrderListRecyclerView.setAdapter(mOrderListAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mOrderListRecyclerView.setLayoutManager(linearLayoutManager);

                    for(int i=0;i<orderDetailArray.size();i++) {

                        //픽업 여부 확인 후 pickupDate 설정
                        isPickuped=orderDetailArray.get(i).getAsJsonObject().get("order_md_status").toString();
                        if (Objects.equals(isPickuped, "1")) {
                            pickupDate="픽업 완료";
                        }
                        else {
                            pickupDate="픽업 예정일 "+ pu_date.get(i).getAsString();
                        }

                        addOrderList(user_id, orderDetailArray.get(i).getAsJsonObject().get("order_id").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("store_loc").getAsString(),
                                "https://gdjang.s3.ap-northeast-2.amazonaws.com/" + orderDetailArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("order_select_qty").getAsString()+"세트",
                                orderDetailArray.get(i).getAsJsonObject().get("pay_price").getAsString()+"원",
                                isPickuped,
                                pickupDate,
                                orderDetailArray.get(i).getAsJsonObject().get("store_lat").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("store_long").getAsString());
                    }

                    mOrderListAdapter.setOnItemClickListener (
                            new OrderList2Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(OrderList.this, OrderDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("md_img", mList.get(pos).getStoreProdImgView());
                                    intent.putExtra("store_loc", mList.get(pos).getStoreLoc());
                                    intent.putExtra("store_name", mList.get(pos).getStoreName());
                                    intent.putExtra("md_name", mList.get(pos).getMdName());
                                    intent.putExtra("md_comp", mList.get(pos).getMdQty());
                                    intent.putExtra("md_price", mList.get(pos).getMdPrice());
                                    intent.putExtra("order_id", mList.get(pos).getOrderId());
                                    startActivity(intent);
                                }
                            }
                    );

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "전체스토어 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
            }
        });
    }
    public void firstInit(){
        mOrderListRecyclerView = findViewById(R.id.totalOrderListView);
        mList = new ArrayList<>();
    }

    public void addOrderList(String userId, String orderId, String storeLoc, String mdImgView, String storeName, String mdName, String mdQty, String mdPrice, String mdStatus, String puDate, String storeLat, String storeLong){
        OrderListInfo order = new OrderListInfo();
        order.setUserId(userId);
        order.setOrderId(orderId);
        order.setStoreLoc(storeLoc);
        order.setStoreProdImgView(mdImgView);
        order.setStoreName(storeName);
        order.setMdName(mdName);
        order.setMdQty(mdQty);
        order.setMdPrice(mdPrice);
        order.setMdStatus(mdStatus);
        order.setPuDate(puDate);
        order.setStoreLat(storeLat);
        order.setStoreLong(storeLong);
        mList.add(order);
    }
}
