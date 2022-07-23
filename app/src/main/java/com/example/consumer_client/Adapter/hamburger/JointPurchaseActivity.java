package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

interface JointPurchaseService {
    @POST("jointPurchase")
    Call<ResponseBody> postMdId(@Body JsonObject body);
}

public class JointPurchaseActivity extends AppCompatActivity {
    String TAG = JointPurchaseActivity.class.getSimpleName();

    private String md_id;
    Context mContext;

    JointPurchaseService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray md_detail;
    String pay_schedule;
    String pu_start;
    String pu_end;

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
        TextView PaySchedule = (TextView) findViewById(R.id.JP_PayDate);
        TextView PuStart = (TextView) findViewById(R.id.JP_PU_Start);
        TextView PuEnd = (TextView) findViewById(R.id.JP_PU_End);

        //제품설명 단락
        ImageView MdImgDetail = (ImageView) findViewById(R.id.JP_MD_Datail_Img);

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

        Intent intent = getIntent();
        md_id = intent.getStringExtra("md_id");

        JsonObject body = new JsonObject();
        body.addProperty("md_id", md_id);

        Call<ResponseBody> call = service.postMdId(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response", response.toString());
                if (response.isSuccessful()) {
                    try {
                        res =  (JsonObject) jsonParser.parse(response.body().string());
                        md_detail = res.get("md_detail_result").getAsJsonArray();
                        pay_schedule = res.get("pay_schedule").getAsString();
                        pu_start = res.get("pu_start").getAsString();
                        pu_end = res.get("pu_end").getAsString();
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

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {

                //000 농부님의 000상품 setText
//                        MdImgThumbnail.setImageURI(Uri.parse(jsonArray.getAsJsonObject().get("mdimg_thumbnail").getAsString()));
                FarmerName.setText("임시농부이름");
                MdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());
                FarmName.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                StkRemain.setText(md_detail.get(0).getAsJsonObject().get("stk_remain").getAsString());
                StkGoal.setText(md_detail.get(0).getAsJsonObject().get("stk_goal").getAsString());
                PaySchedule.setText(pay_schedule);
                PuStart.setText(pu_start);
                PuEnd.setText(pu_end);

                //제품설명 setText
//                        jpProdName.setText(md_detail.get(0).getAsJsonObject().get("md_name").getAsString());

                //농가와 픽업스토어 setText
                FarmName2.setText(md_detail.get(0).getAsJsonObject().get("farm_name").getAsString());
                FarmInfo.setText(md_detail.get(0).getAsJsonObject().get("farm_info").getAsString());
                StoreName.setText(md_detail.get(0).getAsJsonObject().get("store_name").getAsString());
                StoreInfo.setText(md_detail.get(0).getAsJsonObject().get("store_info").getAsString());

                PaySchedule.setText(pay_schedule);
                PuStart.setText(pu_start);
                PuEnd.setText(pu_end);
            }
        }, 1000 ); // 1000 = 1초
    }
}
