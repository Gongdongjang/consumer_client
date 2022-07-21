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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MdListMainActivity extends AppCompatActivity {
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

        mContext = this;

        firstInit();

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<MdGet> call = service.getMdMainData();
        call.enqueue(new Callback<MdGet>() {

            @Override
            public void onResponse(Call<MdGet> call, Response<MdGet> response) {
                try{
                    MdGet result = response.body();
                    //Log.d("60행", result.toString());
                    count = Integer.parseInt(result.getCount());

                    for (int i = 0; i < count; i++) {
                        farmNameL[i] = result.getFarm_name().get(i).toString();
                        mdNameL[i] = result.getMd_name().get(i).toString();
                        storeNameL[i] = result.getSt_name().get(i).toString();
                        payScheduleL[i]= result.getPay_schedule().get(i).toString();
                        puStartL[i]= result.getPu_start().get(i).toString();
                        puEndL[i]= result.getPu_end().get(i).toString();
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
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<MdGet> call, Throwable t) {
                Toast.makeText(MdListMainActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });


        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                //어뎁터 적용
                mMdListMainAdapter = new FarmDetailAdapter(mList);
                mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                //세로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mMdListRecyclerView.setLayoutManager(linearLayoutManager);

                for(int i=0;i<count;i++){
                    //addFarm("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
                    addMdList(mdL.get(i).get(0), mdL.get(i).get(1), mdL.get(i).get(2), mdL.get(i).get(3), mdL.get(i).get(4) + "~" + mdL.get(i).get(5));
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

            } }, 2000 ); // 1000 = 1초

    }
    public void firstInit(){
        mMdListRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addMdList(String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo mdDetail = new FarmDetailInfo();

        mdDetail.setFarmName(farmName);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setPaySchedule(paySchedule);
        mdDetail.setPuTerm(puTerm);

        mList.add(mdDetail);
    }
}

