package com.example.consumer_client.Adapter.hamburger;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.consumer_client.LocationDistance.distance;

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

    List<List<Double>> mdCL = new ArrayList<>();
    //세부 페이지 리사이클러뷰를 위한 배열 및 변수
    List<List<String>> md_nameL = new ArrayList<>();
    List<List<String>> farm_nameL = new ArrayList<>();
    List<List<String>> pu_startL = new ArrayList<>();
    List<List<String>> pu_endL = new ArrayList<>();

    Double[] md_count = new Double[100];
    Integer[] storeIdL = new Integer[100];

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
                        storeIdL[i] = (Integer) result.getStore_id().get(i);
                        storeInfoL[i] = result.getStore_info().get(i).toString();
                        storeHoursL[i] = result.getStore_hours().get(i).toString();
                        storeRestDaysL[i] = result.getStore_restDays().get(i).toString();
                        storeLocL[i] = result.getStore_loc().get(i).toString();
                        storeLatL[i] = result.getStore_lat().get(i).toString();
                        storeLongL[i] = result.getStore_long().get(i).toString();

                        //백구현 완료하고 실행하기
//                        md_count[i] = (Double) result.getMd_count().get(i); //얘는 잘 돼
                        //storeIdL[i] = (Double) result.getStore_id().get(i);
//                        md_nameL.get(i) = result.getMd_name().get().toString();
                        //Log.d("93행", storeIdL[i].toString());
                    }

                    Toast.makeText(StoreActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> storeInfo = new ArrayList<>();
                        //List<String> mdNameL = new ArrayList<>();
                        List<Double> mdCountL = new ArrayList<>();
                        storeInfo.add(storeNameL[i]);   //0 스토어이름
                        storeInfo.add(storeInfoL[i]);   //1 스토어설명
                        storeInfo.add(storeHoursL[i]);  //2 스토어운영시간
                        storeInfo.add(storeRestDaysL[i]);   //3 스토어휴무일
                        storeInfo.add(storeLocL[i]);  //4 스토어위치
                        storeInfo.add(storeLatL[i]); //5 스토어 위도
                        storeInfo.add(storeLongL[i]); //6 스토어 경도
                        storeL.add(storeInfo);
                        //세부
//                        mdCountL.add(md_count[i]);
//                        mdCL.add(mdCountL);
                    }
                   //Log.d("스토어리스트 출력", storeL.toString());
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
        
                    //addStore("스토어 이미지", storeL.get(i).get(0),  storeL.get(i).get(1), storeL.get(i).get(3), storeL.get(i).get(2), 3);
                    //나중에 추가

                    //double distanceMeter =
                      //      distance(37.504198, 127.047967, Double.parseDouble(storeL.get(i).get(8)), Double.parseDouble(storeL.get(i).get(9)), "meter");

                    //Log.d(TAG, "현재거리계산 => " + String.format("%.2f", distanceMeter));

                    //addStore("product Img", storeL.get(i).get(0), String.format("%.2f", distanceMeter), storeL.get(i).get(1), "" + i, "" + i + "000", storeL.get(i).get(2) +" ~ " + storeL.get(i).get(3));

                }

                mStoreTotalAdapter.setOnItemClickListener(
                        new StoreTotalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Log.d("152행", storeL.get(pos).toString()); //클릭한 item 정보 보이기
                                Intent intent = new Intent(StoreActivity.this, StoreDetailActivity.class);

                                //배열로 보내고 싶은데.. 일단 putExtra로 값 보내기
                                intent.putExtra("storeName", storeL.get(pos).get(0));
                                intent.putExtra("storeInfo",storeL.get(pos).get(1));
                                intent.putExtra("storeLoc",storeL.get(pos).get(4));
                                intent.putExtra("storeLat",storeL.get(pos).get(5));
                                intent.putExtra("storeLong",storeL.get(pos).get(6));
                                intent.putExtra("storeHours",storeL.get(pos).get(2));
                                intent.putExtra("storeRestDays",storeL.get(pos).get(3));
//                                intent.putExtra("storeId",storeL.get(pos).get(7));
                                intent.putExtra("mdCount", mdCL.get(pos).get(0).toString());
                                startActivity(intent);
                            }
                        }
                );

            } }, 3000 ); // 1000 = 1초
    }

    public void firstInit(){
        mStoreRecyclerView = findViewById(R.id.totalStoreView);
        mList = new ArrayList<>();
    }

    public void addStore(String storeProdImgView, String storeName, String storeInfo, String storeRestDays, String storeHours, int storeSituation){
        StoreTotalInfo store = new StoreTotalInfo();

        store.setStoreProdImgView(storeProdImgView);
        store.setStoreName(storeName);
        store.setStoreInfo(storeInfo);
        store.setStoreRestDays(storeRestDays);
        store.setStoreHours(storeHours);
        store.setStoreSituation(storeSituation);

        mList.add(store);
    }
}
