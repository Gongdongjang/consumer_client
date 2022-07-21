package com.example.consumer_client.Adapter.hamburger;

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

import com.example.consumer_client.FarmGet;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;

interface FarmService{
    @GET("/farmView")
    Call<ResponseBody> getFarmData();
}

public class FarmActivity extends AppCompatActivity {

    FarmService service;
    JsonParser jsonParser;

    private RecyclerView mFarmRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;

    int count;
    String[] farmNameL = new String[100];
    String[] farmItemL = new String[100];
    String[] farmInfoL = new String[100];
    String[] farmLocL = new String[100];
    String[] farmLatL = new String[100];
    String[] farmLongL = new String[100];
    String[] farmHoursL = new String[100];

    List<List<String>> farmL = new ArrayList<>();
    List<List<Double>> mdCL = new ArrayList<>();
    //세부 페이지 리사이클러뷰를 위한 배열 및 변수
    List<List<String>> md_nameL = new ArrayList<>();
    List<List<String>> store_nameL = new ArrayList<>();
    List<List<String>> pu_startL = new ArrayList<>();
    List<List<String>> pu_endL = new ArrayList<>();

//    String[] md_nameL = new String[30];
//    List md_nameL = new ArrayList();
//    String[] store_nameL = new String[30];
//    String[] pu_startL = new String[30];
//    String[] pu_endL = new String[30];

//    List<Integer> md_count = new ArrayList<>();
    Double[] md_count = new Double[100];
    Double[] farmIdL = new Double[100];


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

        mContext = this;

        firstInit();

        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

        JsonObject body = new JsonObject();
        Call<ResponseBody> call = service.getFarmData();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
//                    Log.d("179행", response.toString());
                    JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());

                    JsonArray farmArray = res.get("result").getAsJsonArray();
                    JsonArray mdArray = res.get("md_result").getAsJsonArray();

                    Handler mHandler = new Handler();
                    mHandler.postDelayed( new Runnable() {
                        public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                            //어뎁터 적용
                            mFarmTotalAdapter = new FarmTotalAdapter(mList);
                            mFarmRecyclerView.setAdapter(mFarmTotalAdapter);

                            //세로로 세팅
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            mFarmRecyclerView.setLayoutManager(linearLayoutManager);

                            for(int i=0;i<farmArray.size();i++){
                                //addFarm("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
                                int count = 0;
                                for( int j = 0; j < mdArray.size(); j++){
                                    Log.d(mdArray.get(j).getAsJsonObject().get("farm_id").toString()+"번", farmArray.get(i).getAsJsonObject().get("farm_id").toString());
                                    if(mdArray.get(j).getAsJsonObject().get("farm_id").getAsInt() == farmArray.get(i).getAsJsonObject().get("farm_id").getAsInt()){
                                        count++;
                                    }
                                }
                                addFarm("product Img", farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);
                            }

//                            mFarmTotalAdapter.setOnItemClickListener(
//                                    new FarmTotalAdapter.OnItemClickListener() {
//                                        @Override
//                                        public void onItemClick(View v, int pos) {
//                                            Log.d("120행", farmL.get(pos).toString()); //클릭한 item 정보 보이기
//                                            Intent intent = new Intent(FarmActivity.this, FarmDetailActivity.class);
//
//                                            //배열로 보내고 싶은데... 각각 보내는게 맞나...? 일단 putExtra로 값 보내기
//                                            intent.putExtra("farmName", farmL.get(pos).get(0));
//                                            intent.putExtra("farmInfo",farmL.get(pos).get(2));
//                                            intent.putExtra("farmLoc",farmL.get(pos).get(3));
//                                            intent.putExtra("farmLat",farmL.get(pos).get(4));
//                                            intent.putExtra("farmLong",farmL.get(pos).get(5));
//                                            intent.putExtra("farmHours",farmL.get(pos).get(6));
//                                            intent.putExtra("farmId",farmL.get(pos).get(7));
//                                            intent.putExtra("mdCount", mdCL.get(pos).get(0).toString());
////                                Log.d("179행", mdCL.get(pos).get(0).toString());
////                                intent.putExtra("mdName", md_nameL);
////                                intent.putExtra("storeName", store_nameL);
////                                intent.putExtra("puStart", pu_startL);
////                                intent.putExtra("puEnd", pu_endL);
////                                intent.putStringArrayListExtra("mdName", mdNameL);
//                                            startActivity(intent);
//                                        }
//                                    }
//                            );

                        } }, 1000 ); // 1000 = 1초
                    //Log.d("60행", result.toString());
//                    count = Integer.parseInt(result.getCount());
//
//                    for (int i = 0; i < count; i++) {
//                        farmNameL[i] = result.getFarm_arr().get(i).toString();
//                        farmItemL[i] = result.getFarm_mainItem().get(i).toString();
//                        farmInfoL[i] = result.getFarm_info().get(i).toString();
//                        farmLocL[i]= result.getFarm_loc().get(i).toString();
//                        farmLatL[i]= result.getFarm_lat().get(i).toString();
//                        farmLongL[i]= result.getFarm_long().get(i).toString();
//                        farmHoursL[i]= result.getFarm_hours().get(i).toString();
//                        md_count[i] = (Double) result.getMd_count().get(i); //얘는 잘 돼
//                        farmIdL[i] = (Double) result.getFarm_id().get(i);
////                        md_nameL.get(i) = result.getMd_name().get().toString();
//                        Log.d("90행", farmIdL[i].toString());
//                    }
//
//                    Toast.makeText(FarmActivity.this, "로딩중", Toast.LENGTH_SHORT).show();
//
//                    for(int i = 0; i < count; i++){
//                        List<String> farmList = new ArrayList<>();
//                        List<String> mdNameL = new ArrayList<>();
//                        List<Double> mdCountL = new ArrayList<>();
////                        List<String> storeNameL = new ArrayList<>();
////                        List<String> puStartL = new ArrayList<>();
////                        List<String> puEndL = new ArrayList<>();
//                        farmList.add(farmNameL[i]); //0 농장이름
//                        farmList.add(farmItemL[i]); //1 판매물품
//                        farmList.add(farmInfoL[i]); //2 소개
//                        farmList.add(farmLocL[i]);  //3 위치주소
//                        farmList.add(farmLatL[i]);  //4 위도
//                        farmList.add(farmLongL[i]); //5 경도
//                        farmList.add(farmHoursL[i]); //6 영업시간
//                        farmList.add(farmIdL[i].toString());
//                        mdCountL.add(md_count[i]); //1 md 개수
//                        farmL.add(farmList);
//                        mdCL.add(mdCountL);
////                        mdNameL.add()
//                    }
//                    Log.d("123행", farmL.toString());
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FarmActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });





    }

    public void firstInit(){
        mFarmRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addFarm(String farmProdImg, String farmName, String farmProdName, String farmFeature,int farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmProdName(farmProdName);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
