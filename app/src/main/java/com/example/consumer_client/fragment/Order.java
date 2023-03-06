package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.consumer_client.R;
import com.example.consumer_client.order.OrderDetailActivity;
import com.example.consumer_client.order.OrderListAdapter;
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

interface OrderDetailsService {
    @POST("/orderDetailView")
    Call<ResponseBody> orderDetailsData(@Body JsonObject body);
}

public class Order extends Fragment {
    JsonParser jsonParser;
    OrderDetailsService service;
    JsonObject res;
    JsonArray orderDetailArray, pu_date;
    private View view;
    private RecyclerView mOrderListRecyclerView;
    private OrderListAdapter mOrderListAdapter;
    private ArrayList<OrderListInfo> mList;
    Activity mActivity;
    String user_id;
    Context mContext;
    String isPickuped, pickupDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OrderDetailsService.class);
        jsonParser = new JsonParser();
        mContext = getContext();

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.activity_order_list, container, false);

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
                    mOrderListAdapter = new OrderListAdapter(mList);
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
                                 "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + orderDetailArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                //String.format("%.2f", distanceKilo),
                                orderDetailArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                orderDetailArray.get(i).getAsJsonObject().get("order_select_qty").getAsString()+"세트",
                                orderDetailArray.get(i).getAsJsonObject().get("order_price").getAsString()+"원",
                                isPickuped, //mdStatus
                                pickupDate);
                    }

                    mOrderListAdapter.setOnItemClickListener (
                            new OrderListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("store_loc", mList.get(pos).getStoreLoc());
                                    intent.putExtra("store_name", mList.get(pos).getStoreName());
                                    intent.putExtra("md_name", mList.get(pos).getMdName());
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

        return view;
    }

    public void firstInit(){
        mOrderListRecyclerView = view.findViewById(R.id.totalOrderListView);
        mList = new ArrayList<>();
    }

    public void addOrderList(String userId, String orderId, String storeLoc, String mdImgView, String storeName, String mdName, String mdQty, String mdPrice, String mdStatus, String puDate){
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
        mList.add(order);
    }
}