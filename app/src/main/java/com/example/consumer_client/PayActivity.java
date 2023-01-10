package com.example.consumer_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.fragment.Order;
import com.example.consumer_client.order.ToPayActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.BootpayAnalytics;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;
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

public class PayActivity extends AppCompatActivity {

    String user_id;
    String mdName;
    int purchaseNum;
    String prodPrice;
    Double price;
    String md_id, store_id, pickupDate, pickupTime; //결제 성공시 order테이블 삽입
    String user_name, mobile_no; //user정보

    OrderInsertService service;
    JsonParser jsonParser;
    JsonObject res, body;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_order_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OrderInsertService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        mdName = intent.getStringExtra("mdName");
        purchaseNum = Integer.parseInt(intent.getStringExtra("purchaseNum"));
        prodPrice = intent.getStringExtra("prodPrice");
        price= Double.valueOf(prodPrice);
        // Order 테이블에 값 삽입하기 Post 요청
        md_id=intent.getStringExtra("md_id");
        store_id = intent.getStringExtra("store_id");
        pickupDate = intent.getStringExtra("pickupDate");
        pickupTime = intent.getStringExtra("pickupTime");
        //user 정보
        user_name = intent.getStringExtra("user_name");
        mobile_no = intent.getStringExtra("mobile_no");

        Log.d("가격", String.valueOf(price));

        BootpayAnalytics.init(this, getString(R.string.applicationID));

        BootUser user = new BootUser().setPhone(mobile_no); // 구매자 정보

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // 일시불, 2개월, 3개월 할부 허용, 할부는 최대 12개월까지 사용됨 (5만원 이상 구매시 할부허용 범위)


        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName(mdName).setId("ITEM_CODE_MOUSE").setQty(1).setPrice(price);
        //BootItem item2 = new BootItem().setName("키보드").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(500d);
        items.add(item1);
        //items.add(item2);

        Payload payload = new Payload();
        payload.setApplicationId(getString(R.string.applicationID))
                .setOrderName(mdName + " " + purchaseNum +"세트") //결제할 상품명
                .setOrderId("1234")     //개발사에서 관리하는 주문번호  String.valueOf(System.currentTimeMillis())
                .setPrice(Double.valueOf(price))        //결제금액
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Map<String, Object> map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//      payload.setMetadata(new Gson().toJson(map));

        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClose(String data) {
                        Log.d("bootpay", "close: " + data);
                        Bootpay.removePaymentWindow();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " +data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
//                        Bootpay.transactionConfirm(data); //재고가 있어서 결제를 진행하려 할때 true (방법 1)
                        return true; //재고가 있어서 결제를 진행하려 할때 true (방법 2)
//                        return false; //결제를 진행하지 않을때 false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("결제완료 정보", data);
                        Toast.makeText(getApplicationContext()," 결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        //주문하기-> Order테이블에 데이터 값 삽입하기Post 요청
                        body = new JsonObject();
                        body.addProperty("user_id", user_id); //
                        body.addProperty("md_id", md_id);
                        body.addProperty("store_id", store_id);
                        body.addProperty("select_qty", purchaseNum);
                        body.addProperty("pu_date", pickupDate);
                        body.addProperty("pu_time", pickupTime);

                        Call<ResponseBody> call = service.postOrderData(body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                        Toast.makeText(PayActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(PayActivity.this, "주문하기 post 에러 발생", Toast.LENGTH_SHORT).show();
                                Log.e("주문하기 post", t.getMessage());
                            }
                        });
                        //결제 성공 후 페이지 이동하기
                        //(OrderDetail페이지 data바꾸면 OrderDetail 페이지로 이동한는 것으로 바꿀것)
                        Intent i= new Intent(PayActivity.this, MainActivity.class);
                        i.putExtra("user_id",user_id);
                        startActivity(i);

                    }
                }).requestPayment();

    }

}
