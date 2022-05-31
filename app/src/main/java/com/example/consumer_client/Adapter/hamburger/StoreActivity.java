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

import com.example.consumer_client.R;
import com.example.consumer_client.StoreGet;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import java.util.ArrayList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView mStoreRecyclerView;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;
    String[] storeNameL = new String[100];
    String[] mdNameL = new String[100];
    int count;
   // int store_count;
    String[] pu_startL = new String[100];
    String[] pu_endL = new String[100];
    //세부페이지
    String[] storeInfoL = new String[100];
    String[] storeHoursL = new String[100];
    String[] storeRestDaysL = new String[100];
    String[] storeLocL = new String[100];
    String[] storeLatL = new String[100];
    String[] storeLongL = new String[100];

    List<List<String>> storeL = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_total_list);

        mContext = this;

        firstInit();

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<StoreGet> call = service.getStoreData();
        call.enqueue(new Callback<StoreGet>() {
            @Override
            public void onResponse(Call<StoreGet> call, Response<StoreGet> response) {
                try{
                    StoreGet result = response.body();
                    count = Integer.parseInt(result.getCount());
                    for (int i = 0; i < count; i++) {
                        storeNameL[i] = result.getSt_arr().get(i).toString();
                        mdNameL[i] = result.getMd_arr().get(i).toString();
                        pu_startL[i] = result.getPu_start().get(i).toString();
                        pu_endL[i] = result.getPu_end().get(i).toString();
                        //세부
                        storeInfoL[i] = result.getStore_info().get(i).toString();
                        storeHoursL[i] = result.getStore_hours().get(i).toString();
                        storeRestDaysL[i] = result.getStore_restDays().get(i).toString();
                        storeLocL[i] = result.getStore_loc().get(i).toString();
                        storeLatL[i] = result.getStore_lat().get(i).toString();
                        storeLongL[i] = result.getStore_long().get(i).toString();
                    }

                    Toast.makeText(StoreActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> storeInfo = new ArrayList<>();
                        storeInfo.add(storeNameL[i]);   //0
                        storeInfo.add(mdNameL[i]);      //1
                        storeInfo.add(pu_startL[i]);    //2
                        storeInfo.add(pu_endL[i]);      //3
                        //세부
                        storeInfo.add(storeInfoL[i]);   //4 스토어설명
                        storeInfo.add(storeHoursL[i]);  //5 스토어운영시간
                        storeInfo.add(storeRestDaysL[i]);   //6 스토어휴무일
                        storeInfo.add(storeLocL[i]);  //7 스토어위치
                        storeInfo.add(storeLatL[i]); //8 스토어 위도
                        storeInfo.add(storeLongL[i]); //9 스토어 경도
                        storeL.add(storeInfo);
                    }
                   Log.d("스토어리스트 출력", storeL.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<StoreGet> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "스토어 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                //어뎁터 적용
                mStoreTotalAdapter = new StoreTotalAdapter(mList);
                mStoreRecyclerView.setAdapter(mStoreTotalAdapter);

                //세로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mStoreRecyclerView.setLayoutManager(linearLayoutManager);

                for(int i=0;i<count;i++){
                    addStore("product Img", storeL.get(i).get(0), "" + 100 + i, storeL.get(i).get(1), "" + i, "" + i + "000", storeL.get(i).get(2) +" ~ " + storeL.get(i).get(3));
                }

                mStoreTotalAdapter.setOnItemClickListener(
                        new StoreTotalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Log.d("120행", storeL.get(pos).toString()); //클릭한 item 정보 보이기
                                Intent intent = new Intent(StoreActivity.this, StoreDetailActivity.class);

                                //배열로 보내고 싶은데.. 일단 putExtra로 값 보내기
                                intent.putExtra("storeName", storeL.get(pos).get(0));
                                intent.putExtra("storeInfo",storeL.get(pos).get(4));
                                intent.putExtra("storeLoc",storeL.get(pos).get(7));
                                intent.putExtra("storeLat",storeL.get(pos).get(8));
                                intent.putExtra("storeLong",storeL.get(pos).get(9));
                                intent.putExtra("storeHours",storeL.get(pos).get(5));
                                intent.putExtra("storeRestDays",storeL.get(pos).get(6));
                                startActivity(intent);
                            }
                        }
                );

            } }, 1000 ); // 1000 = 1초
    }

    public void firstInit(){
        mStoreRecyclerView = findViewById(R.id.totalStoreView);
        mList = new ArrayList<>();
    }

    public void addStore(String storeProdImgView, String storeName, String storeLocationFromMe, String storeProdName, String storeProdNum, String storeProdPrice, String storePickUpDate){
        StoreTotalInfo store = new StoreTotalInfo();

        store.setStoreProdImgView(storeProdImgView);
        store.setStoreName(storeName);
        store.setStoreLocationFromMe(storeLocationFromMe);
        store.setStoreProdName(storeProdName);
        store.setStoreProdNum(storeProdNum);
        store.setStoreProdPrice(storeProdPrice);
        store.setStorePickUpDate(storePickUpDate);

        mList.add(store);
    }
}
