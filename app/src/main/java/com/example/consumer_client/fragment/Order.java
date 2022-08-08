package com.example.consumer_client.fragment;

import static com.example.consumer_client.address.LocationDistance.distance;
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
import android.widget.Button;
import android.widget.Toast;

import com.example.consumer_client.R;
import com.example.consumer_client.order.OrderDetailActivity;
import com.example.consumer_client.order.OrderListAdapter;
import com.example.consumer_client.order.OrderListInfo;
import com.example.consumer_client.review.ReviewActivity;
import com.example.consumer_client.store.StoreActivity;
import com.example.consumer_client.store.StoreDetailActivity;
import com.example.consumer_client.store.StoreTotalAdapter;
import com.example.consumer_client.store.StoreTotalInfo;
import com.example.consumer_client.user.RegisterActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
    String userid;
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        userid=intent.getStringExtra("userid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        body.addProperty("user_id", userid);
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
                        double distanceKilo =
                                distance(37.59272, 127.016544, Double.parseDouble(orderDetailArray.get(i).getAsJsonObject().get("store_lat").getAsString()), Double.parseDouble(orderDetailArray.get(i).getAsJsonObject().get("store_long").getAsString()), "kilometer");
                        addOrderList(userid, orderDetailArray.get(i).getAsJsonObject().get("order_id").getAsString(), orderDetailArray.get(i).getAsJsonObject().get("store_loc").getAsString(), "제품 이미지", orderDetailArray.get(i).getAsJsonObject().get("store_name").getAsString(), String.format("%.2f", distanceKilo), orderDetailArray.get(i).getAsJsonObject().get("md_name").getAsString(), orderDetailArray.get(i).getAsJsonObject().get("order_select_qty").getAsString(), orderDetailArray.get(i).getAsJsonObject().get("pay_price").getAsString(), orderDetailArray.get(i).getAsJsonObject().get("order_md_status").getAsString(), pu_date.get(i).getAsString(), orderDetailArray.get(i).getAsJsonObject().get("store_lat").getAsString(), orderDetailArray.get(i).getAsJsonObject().get("store_long").getAsString());

                    }

                    mOrderListAdapter.setOnItemClickListener (
                            new OrderListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                                    intent.putExtra("user_id", userid);
                                    intent.putExtra("store_loc", mList.get(pos).getStoreLoc());
                                    intent.putExtra("store_my", mList.get(pos).getStoreLocationFromMe());
                                    intent.putExtra("store_name", mList.get(pos).getStoreName());
                                    intent.putExtra("md_name", mList.get(pos).getMdName());
                                    intent.putExtra("md_qty", mList.get(pos).getMdQty());
                                    intent.putExtra("md_price", mList.get(pos).getMdPrice());
                                    intent.putExtra("md_status", mList.get(pos).getMdStatus());
                                    intent.putExtra("order_id", mList.get(pos).getOrderId());
                                    intent.putExtra("store_lat", mList.get(pos).getStoreLat());
                                    intent.putExtra("store_long", mList.get(pos).getStoreLong());
                                    startActivity(intent);
                                }
                            }
                    );

                    //거리 가까운순으로 정렬
                    mList.sort(new Comparator<OrderListInfo>() {
                        @Override
                        public int compare(OrderListInfo o1, OrderListInfo o2) {
                            int ret;
                            Double distance1 = Double.valueOf(o1.getStoreLocationFromMe());
                            Double distance2 = Double.valueOf(o2.getStoreLocationFromMe());
                            //거리비교
                            ret= distance1.compareTo(distance2);
                            return ret;
                        }
                    });


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

    public void addOrderList(String userId, String orderId, String storeLoc, String mdImgView, String storeName, String storeLocationFromMe, String mdName, String mdQty, String mdPrice, String mdStatus, String puDate, String storeLat, String storeLong){
        OrderListInfo order = new OrderListInfo();
        order.setUserId(userId);
        order.setOrderId(orderId);
        order.setStoreLoc(storeLoc);
        order.setStoreProdImgView(mdImgView);
        order.setStoreName(storeName);
        order.setStoreLocationFromMe(storeLocationFromMe);
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