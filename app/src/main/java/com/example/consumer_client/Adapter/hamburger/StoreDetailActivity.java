package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmDetailData;
import com.example.consumer_client.FarmDetailResponse;
import com.example.consumer_client.MdGet;
import com.example.consumer_client.R;
import com.example.consumer_client.StoreDetailData;
import com.example.consumer_client.StoreDetailResponse;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView reviewRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private ArrayList<StoreReviewInfo> mReviewList;
    private FarmDetailAdapter mFarmDetailAdapter;
    private StoreReviewAdapter mStoreReviewAdapter;
    private ServiceApi service;
    int md_count, store_id;
    String store_name;

    Context mContext;
    int count;
    //현재페이지
    String[] farmNameL = new String[30];
    String[] mdNameL = new String[30];
    String[] storeNameL = new String[30];
    String[] payScheduleL = new String[30];
    String[] puStartL = new String[30];
    String[] puEndL = new String[30];
    //세부페이지
    String[] buying_countL = new String[30];
    String[] goal_peopleL = new String[30];
    String[] farmerNameL = new String[30];
    String[] farmDescL = new String[30];
    String[] storeDescL = new String[30];

    List<List<String>> mdL = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mContext = this;

        //전체픽업 스토어-> 이용가능한 모든 픽업스토어의 리사이클러뷰 클릭하면 나오는 스토어 세부페이지1
        //intent로 값 넘길때
        Intent intent;
        String store_info, store_loc, store_hours, store_dayoff;
        Double store_lat,store_long;

        intent=getIntent(); //intent 값 받기

        store_name=intent.getStringExtra("storeName");
        store_info=intent.getStringExtra("storeInfo");
        store_loc=intent.getStringExtra("storeLoc");
        store_hours=intent.getStringExtra("storeHours");
        store_dayoff=intent.getStringExtra("storeRestDays");
        store_id = (int) Double.parseDouble(intent.getStringExtra("storeId"));
        md_count = (int) Double.parseDouble(intent.getStringExtra("mdCount"));
        Log.d("88행", String.valueOf(store_id));

        store_lat=Double.parseDouble(intent.getStringExtra("storeLat")); //위도-> double 형변환
        store_long=Double.parseDouble(intent.getStringExtra("storeLong")); //경도

        TextView StoreName = (TextView) findViewById(R.id.StoreName);
        TextView StoreExplain = (TextView) findViewById(R.id.StoreExplain);
        TextView StoreLocation = (TextView) findViewById(R.id.StoreLocation);
        TextView StoreHourTime = (TextView) findViewById(R.id.StoreHourTime);
        TextView StoreDayOff = (TextView) findViewById(R.id.StoreDayOff);
        TextView StoreJointPurchaseCount = (TextView) findViewById(R.id.StoreJointPurchaseCount);

        StoreName.setText(store_name);
        StoreExplain.setText(store_info);
        StoreLocation.setText(store_loc);
        StoreHourTime.setText(store_hours);
        StoreDayOff.setText(store_dayoff);
        StoreJointPurchaseCount.setText(md_count);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //지도
        MapView mapView = new MapView(mContext);
        // 중심점 변경
        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.01426900000000, 126.7169940), true);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(store_lat, store_long), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.store_map_view);
        mapViewContainer.addView(mapView);

        //스토어위치 마커 아이콘 띄우기
        MapPoint f_MarkPoint = MapPoint.mapPointWithGeoCoord(store_lat, store_long);  //마커찍기

        MapPOIItem store_marker=new MapPOIItem();
        store_marker.setItemName(store_name); //클릭했을때 가게이름 나오기
        store_marker.setTag(0);
        store_marker.setMapPoint(f_MarkPoint);   //좌표입력받아 현위치로 출력

        //  (클릭 전)기본으로 제공하는 BluePin 마커 모양의 색.
        store_marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        // (클릭 후) 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        store_marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        // 지도화면 위에 추가되는 아이콘을 추가하기 위한 호출(말풍선 모양)
        mapView.addPOIItem(store_marker);


        /// 세부페이지에 있는 리사이클러뷰
        firstInit();

        /*
        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<MdGet> call = service.getMdMainData();
        call.enqueue(new Callback<MdGet>() {
            @Override
            public void onResponse(Call<MdGet> call, Response<MdGet> response) {
                try{
                    MdGet result = response.body();
                    count = Integer.parseInt(result.getCount());
                    for (int i = 0; i < count; i++) {
                        //현재
                        farmNameL[i] = result.getFarm_name().get(i).toString();
                        mdNameL[i] = result.getMd_name().get(i).toString();
                        storeNameL[i] = result.getSt_name().get(i).toString();
                        payScheduleL[i] = result.getPay_schedule().get(i).toString();
                        puStartL[i] = result.getPu_start().get(i).toString();
                        puEndL[i] = result.getPu_end().get(i).toString();
                        //세부
                        buying_countL[i] = result.getBuying_count().get(i).toString();
                        goal_peopleL[i] =result.getGoal_people().get(i).toString();
                        farmerNameL[i] = result.getFarmer_name().get(i).toString();
                        farmDescL[i] = result.getFarm_desc().get(i).toString();
                        storeDescL[i] = result.getSt_desc().get(i).toString();
                    }
                    Toast.makeText(StoreDetailActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> mdInfo = new ArrayList<>();
                        mdInfo.add(farmNameL[i]);   //0
                        mdInfo.add(mdNameL[i]);      //1
                        mdInfo.add(storeNameL[i]);    //2
                        mdInfo.add(payScheduleL[i]);      //3
                        mdInfo.add(puStartL[i]);   //4
                        mdInfo.add(puEndL[i]);   //5 스토어설명
                        //세부
                        mdInfo.add(buying_countL[i]);  //5
                        mdInfo.add(goal_peopleL[i]);   //6
                        mdInfo.add(farmerNameL[i]);  //7
                        mdInfo.add(farmDescL[i]);   //8
                        mdInfo.add(storeDescL[i]);  //9
                        mdL.add(mdInfo);
                    }
                    Log.d("제품리스트 출력", mdL.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<MdGet> call, Throwable t) {
                Toast.makeText(StoreDetailActivity.this, "스토어 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });

        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
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
                GridLayoutManager gridLayoutManager = new GridLayoutManager(StoreDetailActivity.this, 2, GridLayoutManager.VERTICAL, true);
                reviewRecyclerView.setLayoutManager(gridLayoutManager);

                //for(int i=0;i<10;i++){
                 //   addFarmJointPurchase("농가명" + i, "제품 이름" + i, "스토어 이름" + i, "결제예정일" + i, "픽업기간" + i);
                //}

                for(int i=0;i<10;i++){
                    addReview("product Img", "@id " + i, "제품명" + i, "" + i, "제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 제품리뷰 " + i);
                }

                mFarmDetailAdapter.setOnItemClickListener(
                        new FarmDetailAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Log.d("216행", mdL.get(pos).toString()); //클릭한 item 정보 보이기
                                Intent intent = new Intent(StoreDetailActivity.this, JointPurchaseActivity.class);

                                intent.putExtra("farmName", mdL.get(pos).get(0));
                                intent.putExtra("mdName",mdL.get(pos).get(1));
                                intent.putExtra("storeName",mdL.get(pos).get(2));
                                intent.putExtra("paySchedule",mdL.get(pos).get(3));
                                intent.putExtra("puStart",mdL.get(pos).get(4));
                                intent.putExtra("puEnd",mdL.get(pos).get(5));
                                intent.putExtra("buyingCount",mdL.get(pos).get(6));
                                intent.putExtra("goalPeople",mdL.get(pos).get(7));
                                intent.putExtra("storeDesc",mdL.get(pos).get(8));
                                intent.putExtra("farmDesc",mdL.get(pos).get(9));
                                intent.putExtra("storeDesc",mdL.get(pos).get(10));
                                startActivity(intent);
                            }
                        }
                );
            } }, 1000 ); // 1000 = 1초
*/
        send_store_id(new StoreDetailData(store_id, md_count));
    }

    public void firstInit(){
        mRecyclerView = findViewById(R.id.FarmJointPurchaseView);
        reviewRecyclerView = findViewById(R.id.StoreReview);
        mList = new ArrayList<>();
        mReviewList = new ArrayList<>();
    }

    public void addFarmJointPurchase(String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo farmDetail = new FarmDetailInfo();

        farmDetail.setFarmName(farmName);
        farmDetail.setProdName(prodName);
        farmDetail.setStoreName(storeName);
        farmDetail.setPaySchedule(paySchedule);
        farmDetail.setPuTerm(puTerm);

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
    /*
    //store_id 보내기
    private void send_farm_id(FarmDetailData data) {
        service.farmDetail(data).enqueue(new Callback<FarmDetailResponse>() {
            @Override
            public void onResponse(Call<FarmDetailResponse> call, Response<FarmDetailResponse> response) {
                FarmDetailResponse result = response.body();
                Toast.makeText(StoreDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    Log.d("171", result.getMd_name().toString());

                    //어뎁터 적용
                    mFarmDetailAdapter = new FarmDetailAdapter(mList);
                    mRecyclerView.setAdapter(mFarmDetailAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

                    for(int i=0;i<10;i++){
                        addFarmJointPurchase("농가명" + i, "제품 이름" + i, "스토어 이름" + i, "결제예정일" + i, "픽업기간" + i);
                    }

                }
                else{
                    //같은 화면 다시 띄우기
                }
            }
            @Override
            public void onFailure(Call<FarmDetailResponse> call, Throwable t) {
                Toast.makeText(StoreDetailActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }
*/
    private void send_store_id(StoreDetailData data){
        service.storeDetail(data).enqueue(new Callback<StoreDetailResponse>() {
            @Override
            public void onResponse(Call<StoreDetailResponse> call, Response<StoreDetailResponse> response) {
                StoreDetailResponse result = response.body();
                Toast.makeText(StoreDetailActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    //Log.d("171", result.getMd_name().toString());

                    //어뎁터 적용
                    mFarmDetailAdapter = new FarmDetailAdapter(mList);
                    mRecyclerView.setAdapter(mFarmDetailAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mRecyclerView.setLayoutManager(linearLayoutManager);

//                추후에 제품 이름 가져올 예정
                    for(int i=0;i<md_count;i++){
                        addFarmJointPurchase(result.getFarm_name().get(i).toString(), result.getMd_name().get(i).toString(), "수정필요", result.getPay_schedule().get(i).toString(), result.getPu_start().get(i).toString() + "~" + result.getPu_end().get(i).toString());                    }
                    //for(int i=0;i<10;i++){
                    //   addFarmJointPurchase("농가명" + i, "제품 이름" + i, "스토어 이름" + i, "결제예정일" + i, "픽업기간" + i);
                    //}
                }
                else{
                    //같은 화면 다시 띄우기
                }
            }
            @Override
            public void onFailure(Call<StoreDetailResponse> call, Throwable t) {
                Toast.makeText(StoreDetailActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
            }
        });
    }
}
