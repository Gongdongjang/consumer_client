package com.example.consumer_client.Adapter.hamburger;

import static com.example.consumer_client.LocationDistance.distance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.GET;


interface StoreService {
    @GET("/storeView")
    Call<ResponseBody> getStoreData();
}

public class StoreActivity extends AppCompatActivity {
    StoreService service;
    JsonParser jsonParser;
    //Json (Get 응답 받을시 넣을 변수)
    JsonObject res;
    JsonArray storeArray;

    private RecyclerView mStoreRecyclerView;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_total_list);

        mContext = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StoreService.class);
        jsonParser = new JsonParser();

        firstInit();

        Call<ResponseBody> call = service.getStoreData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    storeArray = res.get("store_result").getAsJsonArray();  //json배열
                    Toast.makeText(StoreActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "전체스토어 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
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

                for(int i=0;i<storeArray.size() ;i++) {
                    Log.d("스토어_arr", storeArray.get(i).getAsJsonObject().get("store_id").getAsString());

                    double distanceKilo =
                            distance(37.59272, 127.016544, Double.parseDouble(storeArray.get(i).getAsJsonObject().get("store_lat").getAsString()), Double.parseDouble(storeArray.get(i).getAsJsonObject().get("store_long").getAsString()), "kilometer");

                    addStore(storeArray.get(i).getAsJsonObject().get("store_id").getAsString(),"스토어 이미지", storeArray.get(i).getAsJsonObject().get("store_name").getAsString(), String.format("%.2f", distanceKilo), storeArray.get(i).getAsJsonObject().get("store_info").getAsString(), storeArray.get(i).getAsJsonObject().get("store_hours").getAsString(), storeArray.get(i).getAsJsonObject().get("store_restDays").getAsString());
                }
                //거리 가까운순으로 정렬
                mList.sort(new Comparator<StoreTotalInfo>() {
                    @Override
                    public int compare(StoreTotalInfo o1, StoreTotalInfo o2) {
                        int ret;
                        Double distance1 = Double.valueOf(o1.getStoreLocationFromMe());
                        Double distance2 = Double.valueOf(o2.getStoreLocationFromMe());
                        //거리비교
                        ret= distance1.compareTo(distance2);
                        return ret;
                    }
                });
                //mStoreTotalAdapter.notifyDataSetChanged() ;
                mStoreTotalAdapter.setOnItemClickListener (
                        new StoreTotalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Intent intent = new Intent(StoreActivity.this, StoreDetailActivity.class);
                                intent.putExtra("storeid", mList.get(pos).getStoreid());
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

    public void addStore(String storeId, String storeProdImgView, String storeName, String storeLocationFromMe, String storeInfo, String storeRestDays, String storeHours){
        StoreTotalInfo store = new StoreTotalInfo();

        store.setStoreid(storeId);
        store.setStoreProdImgView(storeProdImgView);
        store.setStoreName(storeName);
        store.setStoreLocationFromMe(storeLocationFromMe);
        store.setStoreInfo(storeInfo);
        store.setStoreRestDays(storeRestDays);
        store.setStoreHours(storeHours);
        mList.add(store);
    }
}
