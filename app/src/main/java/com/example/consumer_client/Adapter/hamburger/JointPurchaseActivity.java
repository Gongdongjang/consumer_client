package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

        Intent intent;
        String farm_name, md_name, store_name, pay_schedule, pu_term, buying_count, goal_people, farmer_name, farm_desc, store_desc;

        intent=getIntent(); //intent 값 받기

        farmer_name=intent.getStringExtra("farmName");
        md_name=intent.getStringExtra("mdName");
        pay_schedule=intent.getStringExtra("paySchedule");
        pu_term=intent.getStringExtra("puTerm");
        buying_count = intent.getStringExtra("buyingCount");
        goal_people = intent.getStringExtra("goalPeople");
        farm_name=intent.getStringExtra("farmerName");
        farm_desc=intent.getStringExtra("farmDesc");
        store_name=intent.getStringExtra("storeName");
        store_desc=intent.getStringExtra("storeDesc");

        TextView FarmerName = (TextView) findViewById(R.id.FarmerName);
        TextView MdName = (TextView) findViewById(R.id.JP_FarmName_Main);
        TextView PaySchedule = (TextView) findViewById(R.id.JP_PayDate);
        TextView PuTerm = (TextView) findViewById(R.id.JP_PUTerm);
        TextView BuyingCount = (TextView) findViewById(R.id.JP_Paying_People);
        TextView GoalPeople = (TextView) findViewById(R.id.JP_Goal_People);
        TextView FarmName = (TextView) findViewById(R.id.JP_FarmName);
        TextView FarmDesc = (TextView) findViewById(R.id.JP_FarmDesc);
        TextView StoreName = (TextView) findViewById(R.id.JP_StoreName);
        TextView StoreDesc = (TextView) findViewById(R.id.JP_StoreDesc);

        FarmerName.setText(farmer_name);
        MdName.setText(md_name);
        PaySchedule.setText(pay_schedule);
        PuTerm.setText(pu_term);
        BuyingCount.setText(buying_count);
        GoalPeople.setText(goal_people);
        FarmName.setText(farm_name);
        FarmDesc.setText(farm_desc);
        StoreName.setText(store_name);
        StoreDesc.setText(store_desc);

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
