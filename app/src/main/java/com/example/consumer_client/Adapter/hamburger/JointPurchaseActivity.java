package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.R;

import java.util.ArrayList;

public class JointPurchaseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<JointPurchaseInfo> mList;
    private JointPurchaseAdapter mJointPurchaseAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joint_purchase);

        mContext = this;

        firstInit();

        //추후에 제품 이름 가져올 예정
        for(int i=1;i<3;i++){
            addJPProd("제품명" + i, "제품" + i + "짧은 설명입니다.", i + "", i+",000", i + 10 + "");
        }

        //어뎁터 적용
        mJointPurchaseAdapter = new JointPurchaseAdapter(mList);
        mRecyclerView.setAdapter(mJointPurchaseAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.JP_Farmer_Prod_View);
        mList = new ArrayList<>();
    }

    public void addJPProd(String jpProdName, String jpProdDesc, String jpPriceCount, String jpPrice, String jpMaxCount){
        JointPurchaseInfo jointPurchase = new JointPurchaseInfo();

        jointPurchase.setJpProdName(jpProdName);
        jointPurchase.setJpProdDesc(jpProdDesc);
        jointPurchase.setJpPriceCount(jpPriceCount);
        jointPurchase.setJpPrice(jpPrice);
        jointPurchase.setJpMaxCount(jpMaxCount);

        mList.add(jointPurchase);
    }

}
