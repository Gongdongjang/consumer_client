package com.example.consumer_client.md;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.cart.CartDialog;
import com.example.consumer_client.OrderDialog;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface JointPurchaseService {
    @POST("jointPurchase")
    Call<ResponseBody> postMdId(@Body JsonObject body);

    @POST("isKeep")
    Call<ResponseBody> postisKeep(@Body JsonObject body);

    @POST("keep")
    Call<ResponseBody> postKeep(@Body JsonObject body);
}

public class JointPurchaseActivity extends AppCompatActivity {
    String TAG = JointPurchaseActivity.class.getSimpleName();

    private String md_id;
    Context mContext;

    JointPurchaseService service;
    JsonParser jsonParser;

    JsonObject res, body;
    JsonArray md_detail, keep_date;
//    String pay_schedule;
    String pu_start;
    String pu_end;
    String user_id, store_id;
    JsonArray keep_data;
    String message;
    String store_loc,store_lat,store_long;

    //Dialog 선언
    OrderDialog orderDialog;
    CartDialog cartDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_purchase);

        mContext = this;

        //000 농부님의 000상품 단락
        ImageView MdImgThumbnail = (ImageView) findViewById(R.id.JP_MD_Img);
        TextView FarmerName = (TextView) findViewById(R.id.FarmerName);
        TextView MdName = (TextView) findViewById(R.id.ProdName);
        TextView FarmName = (TextView) findViewById(R.id.JP_FarmName_Main);
        TextView StkRemain = (TextView) findViewById(R.id.JP_Remain_Count);
        TextView StkGoal = (TextView) findViewById(R.id.JP_Goal_Count);
//        TextView PaySchedule = (TextView) findViewById(R.id.JP_PayDate);
        TextView PuStart = (TextView) findViewById(R.id.JP_PU_Start);
        TextView PuEnd = (TextView) findViewById(R.id.JP_PU_End);

        //고정 하단바 (찜하기, 장바구니, 주문하기)
        ImageView Keep = (ImageView) findViewById(R.id.JP_KeepBtn);
        ImageView Cart = (ImageView) findViewById(R.id.JP_CartBtn);
        Button Order = (Button) findViewById(R.id.JP_OrderBtn);


        //제품설명 단락 + (가격추가함.)
        ImageView MdImgDetail = (ImageView) findViewById(R.id.JP_MD_Datail_Img);
        TextView ProdName= (TextView) findViewById(R.id.JP_ProdName);
        TextView ProdNum= (TextView) findViewById(R.id.JP_Prod_Num);
        TextView ProdPrice= (TextView) findViewById(R.id.JP_Prod_Price);


        //농가와 픽업 스토어 소개 단락
        ImageView FarmFileName = (ImageView) findViewById(R.id.JP_FarmIMG);
        TextView FarmName2 = (TextView) findViewById(R.id.JP_FarmName);
        TextView FarmInfo = (TextView) findViewById(R.id.JP_FarmDesc);
        ImageView StoreFileName = (ImageView) findViewById(R.id.JP_StoreIMG);
        TextView StoreName = (TextView) findViewById(R.id.JP_StoreName);
        TextView StoreInfo = (TextView) findViewById(R.id.JP_StoreDesc);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(JointPurchaseService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        md_id = intent.getStringExtra("md_id");

        body = new JsonObject();
        body.addProperty("user_id", user_id);
        body.addProperty("md_id", md_id);

        Log.d("JointPurchase", user_id);

        //상세페이지 데이터 등록
        Call<ResponseBody> call2 = service.postMdId(body);
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call2, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());
                        md_detail = res.get("md_detail_result").getAsJsonArray();
//                        pay_schedule = res.get("pay_schedule").getAsString();
                        pu_start = res.get("pu_start").getAsString();
                        pu_end = res.get("pu_end").getAsString();

                        Log.d("md_detail", md_detail.toString());
                        store_id = md_detail.get(0).getAsJsonObject().get("store_id").getAsString();

                        //스토어 위치(주문하기에서)
                        store_loc=md_detail.get(0).getAsJsonObject().get("store_loc").getAsString();
                        store_lat=md_detail.get(0).getAsJsonObject().get("store_lat").getAsString();
                        store_long=md_detail.get(0).getAsJsonObject().get("store_long").getAsString();

                        //000 농부님의 000상품 setText
                        MdImgThumbnail.setImageURI(Uri.parse(md_detail.get(0).getAsJsonObject().get("mdimg_thumbnail").getAsString()));
                        FarmerName.setText(md_detail.get(0).getAsJsonObject().get("farm_farmer").getAsString());
                        MdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());
                        FarmName.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                        StkRemain.setText(md_detail.get(0).getAsJsonObject().get("stk_remain").getAsString());
                        StkGoal.setText(md_detail.get(0).getAsJsonObject().get("stk_goal").getAsString());
//                        PaySchedule.setText(pay_schedule);
                        PuStart.setText(pu_start);
                        PuEnd.setText(pu_end);

                        //제품설명 setText
//                        MdImgDetail.setImageURI(Uri.parse(md_detail.get(0).getAsJsonObject().get("mdImg_detail").getAsString()));
                        Picasso.get().load(md_detail.get(0).getAsJsonObject().get("mdImg_detail").getAsString()).into(MdImgDetail);
                        ProdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());
                        ProdNum.setText(md_detail.get(0).getAsJsonObject().get("pay_comp").getAsString());
                        ProdPrice.setText(md_detail.get(0).getAsJsonObject().get("pay_price").getAsString());

                        //농가와 픽업스토어 setText
                        FarmName2.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                        FarmInfo.setText(md_detail.get(0).getAsJsonObject().get("farm_info").getAsString());
                        StoreName.setText(md_detail.get(0).getAsJsonObject().get("store_name").getAsString());
                        StoreInfo.setText(md_detail.get(0).getAsJsonObject().get("store_info").getAsString());

//                        PaySchedule.setText(pay_schedule);
                        PuStart.setText(pu_start);
                        PuEnd.setText(pu_end);
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
            public void onFailure(Call<ResponseBody> call2, Throwable t) {
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });

        //----고정하단바-----
        //찜 한 정보 불러오기 (해당 사용자가 해당 상품에 찜했으면, 하트)
        Call<ResponseBody> call = service.postisKeep(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                    Log.d("116행", res.toString());
                    message = res.get("message").getAsString();
                    if (message.equals("exist")) {
                        Keep.setImageResource(R.drawable.ic_baseline_favorite_24);
                        Keep.setTag("liked");
                    } else if (message.equals("notexist")) {
                        Keep.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        Keep.setTag("like");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });

        //찜 클릭시 취소 or 등록 제어
        Keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = service.postKeep(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                            keep_data = res.get("keep_data").getAsJsonArray();
                            message = res.get("message").getAsString();
                            if (Keep.getTag().equals("like")) {
                                Toast.makeText(JointPurchaseActivity.this, "찜한 상품에 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                Keep.setImageResource(R.drawable.ic_baseline_favorite_24);
                                Keep.setTag("liked");
                            } else {
                                Toast.makeText(JointPurchaseActivity.this, "찜한 상품에서 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                Keep.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                Keep.setTag("like");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e(TAG, "onFailure: e " + t.getMessage());
                    }
                });
            }
        });

        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        //장바구니 클릭
        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDialog = new CartDialog(mContext, (String) MdName.getText(),(String) ProdNum.getText(), (String) ProdPrice.getText()
                        , pu_start,pu_end, (String) StoreName.getText(), store_loc, store_lat, store_long, user_id, md_id, store_id);
                cartDialog.show();
            }
        });
        
        //주문 클릭
        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDialog = new OrderDialog(mContext, (String) MdName.getText(),(String) ProdNum.getText(), (String) ProdPrice.getText()
                        , pu_start,pu_end, (String) StoreName.getText(), store_loc, store_lat, store_long);
                //orderDialog = new OrderDialog(mContext,md_detail.get(0).getAsJsonObject().get("md_name").getAsString(),pu_start,pu_end);
                orderDialog.show();
            }
        });

    }
}
