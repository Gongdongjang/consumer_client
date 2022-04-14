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

public class StoreDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView reviewRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private ArrayList<StoreReviewInfo> mReviewList;
    private FarmDetailAdapter mFarmDetailAdapter;
    private StoreReviewAdapter mStoreReviewAdapter;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mContext = this;

        firstInit();

        //추후에 제품 이름 가져올 예정
        for(int i=0;i<10;i++){
            addFarmJointPurchase("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
        }
        for(int i=0;i<10;i++){
            addReview("product Img", "@id " + i, "제품명" + i, "" + i, "제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 " + i);
        }

        //어뎁터 적용
        mFarmDetailAdapter = new FarmDetailAdapter(mList);
        mRecyclerView.setAdapter(mFarmDetailAdapter);

        //세로로 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //어뎁터 적용
        mStoreReviewAdapter = new StoreReviewAdapter(mReviewList);
        reviewRecyclerView.setAdapter(mStoreReviewAdapter);

        //두 칸으로 세팅
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, true);
        reviewRecyclerView.setLayoutManager(gridLayoutManager);

    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmJointPurchaseView);
        reviewRecyclerView = findViewById(R.id.StoreReview);
        mList = new ArrayList<>();
        mReviewList = new ArrayList<>();
    }

    public void addFarmJointPurchase(String farmProdImg, String farmName, String farmProdName, String farmFeature, String farmSituation){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmDetailProdImgView(farmProdImg);
        farmDetail.setFarmDetailName(farmName);
        farmDetail.setFarmDetailProdName(farmProdName);

        mList.add(farmDetail);
    }

    public void addReview(String userProfileImg, String userID, String prodName, String starCount, String reviewMessage){
        StoreReviewInfo review = new StoreReviewInfo();

        review.setUserProfileImg(userProfileImg);
        review.setUserID(userID);
        review.setProdName(prodName);
        review.setStarCount(starCount);
        review.setReview(reviewMessage);

        mReviewList.add(review);
    }
}
