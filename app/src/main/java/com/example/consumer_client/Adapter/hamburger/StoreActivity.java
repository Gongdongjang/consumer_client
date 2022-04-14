package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmGet;
import com.example.consumer_client.R;
import com.example.consumer_client.StoreGet;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView mStoreRecyclerView;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;
    String[] storeNameL = new String[8]; //나중에 숫자 바꾸기 - 동적할당으로
    int count;
//    Calendar cal = Calendar.getInstance();
//    Date date = cal.getTime();
//    int year = cal.get(Calendar.YEAR);
//    int month = cal.get(Calendar.MONTH);
//    int day = cal.get(Calendar.DATE);
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

                if (response.isSuccessful()) {
                    StoreGet result = response.body();
                    List store = result.getStore();
                    count = Integer.parseInt(result.getCount());
                    for (int i = 0; i < Integer.parseInt(result.getCount()); i++) {
//                        Object obj = farm.get(i);
                        String str = store.get(i).toString();
//                        Log.d("77행", result.getFarm_name());
//                        Log.d("78행", result.getFarm_info());
//                        Log.d("79행", result.getFarm_mainItem());
                        String[] list = str.split(", ");
                        Log.d("82행", list[1].substring(11));
                        storeNameL[i] = list[1].substring(11);
//                        count++;
                        Log.d("92행", storeNameL[i].getClass().toString());
//                        Toast.makeText(FarmActivity.this, "로딩중", Toast.LENGTH_SHORT).show();
                    }
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
                    Log.d("storeName", storeNameL[i]);
                    String s = storeNameL[i];
                    addStore("product Img", s, "" + 100 + i, "제품명" + i, "" + i, "" + i + "000", "2000.04.0" + i);
                }
            } }, 100 ); // 1000 = 1초
    }

//        //어뎁터 적용
//        mStoreTotalAdapter = new StoreTotalAdapter(mList);
//        mStoreRecyclerView.setAdapter(mStoreTotalAdapter);
//
//        //세로로 세팅
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mStoreRecyclerView.setLayoutManager(linearLayoutManager);}

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
