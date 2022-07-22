package com.example.consumer_client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.Adapter.hamburger.FarmActivity;
import com.example.consumer_client.Adapter.hamburger.FarmDetailActivity;
import com.example.consumer_client.Adapter.hamburger.FarmDetailAdapter;
import com.example.consumer_client.Adapter.hamburger.FarmDetailInfo;
import com.example.consumer_client.Adapter.hamburger.FarmTotalAdapter;
import com.example.consumer_client.Adapter.hamburger.JointPurchaseActivity;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

interface MdService {
    @GET("mdView_main")
    Call<ResponseBody> getMdMainData();
}

public class MdListMainActivity extends AppCompatActivity {

    JsonParser jsonParser;
    MdService service;

    private RecyclerView mMdListRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mMdListMainAdapter;
    Context mContext;

    //제품 가져올 때 필요한 변수
    int count;
    String[] farmNameL = new String[100];
    String[] mdNameL = new String[100];
    String[] storeNameL = new String[100];
    String[] payScheduleL = new String[100];
    String[] puStartL = new String[100];
    String[] puEndL = new String[100];

    List<List<String>> mdL = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_total_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MdService.class);
        jsonParser = new JsonParser();

        mContext = this;

        firstInit();

        JsonObject body = new JsonObject();
        Call<ResponseBody> call = service.getMdMainData();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("91행", response.toString());
                try{
                    JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                    JsonArray jsonArray = res.get("result").getAsJsonArray();
                    String mdName = null;
                    for(int i = 0; i < jsonArray.size(); i++){
                        Log.d("113", jsonArray.get(i).getAsJsonObject().get("md_name").getAsString());
                    }

                    Toast.makeText(MdListMainActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> mdList = new ArrayList<>();
//                        List<String> mdNameL = new ArrayList<>();
//                        List<Double> mdCountL = new ArrayList<>();
//                        List<String> storeNameL = new ArrayList<>();
//                        List<String> puStartL = new ArrayList<>();
//                        List<String> puEndL = new ArrayList<>();
                        mdList.add(farmNameL[i]); //0 농장이름
                        mdList.add(mdNameL[i]); //1 판매물품 이름
                        mdList.add(storeNameL[i]); //2 스토어 이름
                        mdList.add(payScheduleL[i]);  //3 결제 예정일
                        mdList.add(puStartL[i]);  //4 픽업 시작 시점
                        mdList.add(puEndL[i]); //5 픽업 끝나는 시점
//                        farmList.add(farmHoursL[i]); //6 영업시간
//                        farmList.add(farmIdL[i].toString());
//                        mdCountL.add(md_count[i]); //1 md 개수
                        mdL.add(mdList);
//                        mdCL.add(mdCountL);
//                        mdNameL.add()
                    }
                    Log.d("123행", mdL.toString());

                    Handler mHandler = new Handler();
                    String finalMdName = mdName;
                    mHandler.postDelayed(new Runnable() {
                        public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                            //어뎁터 적용
                            mMdListMainAdapter = new FarmDetailAdapter(mList);
                            mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                            //세로로 세팅
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mMdListRecyclerView.setLayoutManager(linearLayoutManager);

                            for(int i=0;i<jsonArray.size();i++){
                                addMdList("product Img",
                                        jsonArray.get(i).getAsJsonObject().get("farm_name").getAsString(),
                                        jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                        jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                        jsonArray.get(i).getAsJsonObject().get("pay_schedule").getAsString().substring(0,10),
                                        jsonArray.get(i).getAsJsonObject().get("pu_start").getAsString().substring(0,10) + " ~ " +
                                                jsonArray.get(i).getAsJsonObject().get("pu_end").getAsString().substring(0,10)
                                );
                            }

                            mMdListMainAdapter.setOnItemClickListener(
                                    new FarmDetailAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int pos) {
                                            Log.d("120행", mdL.get(pos).toString()); //클릭한 item 정보 보이기
                                            Intent intent = new Intent(MdListMainActivity.this, JointPurchaseActivity.class);

                                            //배열로 보내고 싶은데... 각각 보내는게 맞나...? 일단 putExtra로 값 보내기
                                            intent.putExtra("farmName", mdL.get(pos).get(0));
                                            intent.putExtra("mdName",mdL.get(pos).get(1));
                                            intent.putExtra("storeName",mdL.get(pos).get(2));
                                            intent.putExtra("paySchedule",mdL.get(pos).get(3));
                                            intent.putExtra("puStart",mdL.get(pos).get(4));
                                            intent.putExtra("puEnd",mdL.get(pos).get(5));
//                                intent.putExtra("farmId",farmL.get(pos).get(7));
//                                intent.putExtra("mdCount", mdCL.get(pos).get(0).toString());
//                                Log.d("179행", mdCL.get(pos).get(0).toString());
//                                intent.putExtra("mdName", md_nameL);
//                                intent.putExtra("storeName", store_nameL);
//                                intent.putExtra("puStart", pu_startL);
//                                intent.putExtra("puEnd", pu_endL);
//                                intent.putStringArrayListExtra("mdName", mdNameL);
                                            startActivity(intent);
                                        }
                                    }
                            );

                        } }, 5000 ); // 1000 = 1초

                }
                catch(Exception e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MdListMainActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void firstInit(){
        mMdListRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addMdList(String img, String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo mdDetail = new FarmDetailInfo();

        mdDetail.setFarmName(farmName);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setPaySchedule(paySchedule);
        mdDetail.setPuTerm(puTerm);

        mList.add(mdDetail);
    }
}

