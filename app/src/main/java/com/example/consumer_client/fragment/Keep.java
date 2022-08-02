package com.example.consumer_client.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.consumer_client.store.StoreTotalAdapter;
import com.example.consumer_client.store.StoreTotalInfo;
import com.example.consumer_client.R;

import java.util.ArrayList;


public class Keep extends Fragment {

    private RecyclerView keep_recyclear;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        //setContentView(R.layout.activity_farm_detail);
        view= inflater.inflate(R.layout.fragment_keep, container, false);
        keep_recyclear = (RecyclerView) view.findViewById(R.id.keep_recycler);
        mList = new ArrayList<>();

        //추후에 제품 이름 가져올 예정
        for(int i=0;i<10;i++){
            //addStore("product Img", "스토어명" + i, "" + 100 + i, "제품명" + i, "" + i, "" + i + "000", "2000.04.0" + i);
        }

        //어뎁터 적용
        mStoreTotalAdapter = new StoreTotalAdapter(mList);
        keep_recyclear.setAdapter(mStoreTotalAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        keep_recyclear.setLayoutManager(linearLayoutManager);

        return view;
    }

//    public void addStore(String storeProdImgView, String storeName, String storeLocationFromMe, String storeProdName, String storeProdNum, String storeProdPrice, String storePickUpDate){
//        StoreTotalInfo store = new StoreTotalInfo();
//
//        store.setStoreProdImgView(storeProdImgView);
//        store.setStoreName(storeName);
//        store.setStoreLocationFromMe(storeLocationFromMe);
//        store.setStoreProdName(storeProdName);
//        store.setStoreProdNum(storeProdNum);
//        store.setStoreProdPrice(storeProdPrice);
//        store.setStorePickUpDate(storePickUpDate);
//
//        mList.add(store);
//    }

}