package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    String[] storeNameL = new String[30];
    String[] mdNameL = new String[30];
    int count;
    String[] pu_startL = new String[30];
    String[] pu_endL = new String[30];
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
                    }

                    Toast.makeText(StoreActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> storeInfo = new ArrayList<>();
                        storeInfo.add(storeNameL[i]);
                        storeInfo.add(mdNameL[i]);
                        storeInfo.add(pu_startL[i]);
                        storeInfo.add(pu_endL[i]);
                        storeL.add(storeInfo);
                    }
                   Log.d("85행", storeL.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<StoreGet> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
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
