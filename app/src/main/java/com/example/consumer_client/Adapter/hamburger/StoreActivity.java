package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView mStoreRecyclerView;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;
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

        //추후에 제품 이름 가져올 예정
        for(int i=0;i<10;i++){
            addStore("product Img", "스토어명" + i, "" + 100 + i, "제품명" + i, "" + i, "" + i + "000", "2000.04.0" + i);
        }

        //어뎁터 적용
        mStoreTotalAdapter = new StoreTotalAdapter(mList);
        mStoreRecyclerView.setAdapter(mStoreTotalAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mStoreRecyclerView.setLayoutManager(linearLayoutManager);
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
