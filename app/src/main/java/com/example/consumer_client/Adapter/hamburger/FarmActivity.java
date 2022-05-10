package com.example.consumer_client.Adapter.hamburger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.FarmGet;
import com.example.consumer_client.R;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmActivity extends AppCompatActivity {

    private RecyclerView mFarmRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;

    int count;
    //String[] farm_listArray = new String[30];
    String[] farmNameL = new String[30];
    String[] farmItemL = new String[30];
    String[] farmInfoL = new String[30];
    String[] farmLocL = new String[30];
    List<List<String>> farmL = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);

        mContext = this;

        firstInit();

        ServiceApi service = RetrofitClient.getClient().create(ServiceApi.class);
        Call<FarmGet> call = service.getFarmData();
        call.enqueue(new Callback<FarmGet>() {

            @Override
            public void onResponse(Call<FarmGet> call, Response<FarmGet> response) {
                try{
                    FarmGet result = response.body();
                    //Log.d("60행", result.toString());
                    count = Integer.parseInt(result.getCount());

                    for (int i = 0; i < count; i++) {
                        farmNameL[i] = result.getFarm_arr().get(i).toString();
                        farmItemL[i] = result.getFarm_mainItem().get(i).toString();
                        farmInfoL[i] = result.getFarm_info().get(i).toString();
                        farmLocL[i]= result.getFarm_loc().get(i).toString();
                    }
                    Toast.makeText(FarmActivity.this, "로딩중", Toast.LENGTH_SHORT).show();

                    for(int i = 0; i < count; i++){
                        List<String> farmList = new ArrayList<>();
                        //for(int j = 0; j < count; j++){   //2중for문 안해도 된다 지우기!!!!
                            //farm_listArray[i]=result.getFarm_listArr.toString();
                            farmList.add(farmNameL[i]);
                            farmList.add(farmItemL[i]);
                            farmList.add(farmInfoL[i]);
                            farmList.add(farmLocL[i]);
                        //}
                        farmL.add(farmList);
                    }
                    Log.d("85행", farmL.toString());
                    //Log.d("85행", farm_listArray.toString());
                }
                catch(Exception e){
                    e.printStackTrace();
                    throw e;
                }
            }
            @Override
            public void onFailure(Call<FarmGet> call, Throwable t) {
                Toast.makeText(FarmActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });


        Handler mHandler = new Handler();
        mHandler.postDelayed( new Runnable() {
            public void run() { // 0.1초 후에 받아오도록 설정 , 바로 시작 시 에러남
                //어뎁터 적용
                mFarmTotalAdapter = new FarmTotalAdapter(mList);
                mFarmRecyclerView.setAdapter(mFarmTotalAdapter);

                //세로로 세팅
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mFarmRecyclerView.setLayoutManager(linearLayoutManager);

                for(int i=0;i<count;i++){
                    //addFarm("product Img", "농가 이름" + i, "농가 제품 이름" + i, "농가 특징" + i, "" + i);
                    addFarm("product Img", farmL.get(i).get(0), farmL.get(i).get(1), farmL.get(i).get(2)," ");
                }

                mFarmTotalAdapter.setOnItemClickListener(
                        new FarmTotalAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Log.d("120행", farmL.get(pos).toString()); //클릭한 item 정보 보이기
                                Intent intent = new Intent(FarmActivity.this, FarmDetailActivity.class);

                                //배열로 보내고 싶은데... 각각 보내는게 맞나...?
                                intent.putExtra("farmName", farmL.get(pos).get(0));
                                intent.putExtra("farmInfo",farmL.get(pos).get(2));
                                intent.putExtra("farmLoc",farmL.get(pos).get(3));
                                startActivity(intent);
                            }
                        }
                );

            } }, 1000 ); // 1000 = 1초


    }

    public void firstInit(){
        mFarmRecyclerView = findViewById(R.id.totalFarmView);
        mList = new ArrayList<>();
    }

    public void addFarm(String farmProdImg, String farmName, String farmProdName, String farmFeature, String farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmProdName(farmProdName);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
