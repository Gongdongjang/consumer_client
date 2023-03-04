package com.example.consumer_client.farm;

import static com.example.consumer_client.address.LocationDistance.distance;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.address.EditTownActivity;
import com.example.consumer_client.home.HomeProductItem;
import com.example.consumer_client.store.StoreActivity;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface FarmService{
    @GET("/farmView")
    Call<ResponseBody> getFarmData();

    @POST("standard_address/getStdAddress")
    Call<ResponseBody> getStdAddress(@Body JsonObject body);  //post user_id
}

public class FarmActivity extends AppCompatActivity {

    FarmService service;
    JsonParser jsonParser;

    private RecyclerView mFarmRecyclerView;
    private ArrayList<FarmTotalInfo> mList;
    private FarmTotalAdapter mFarmTotalAdapter;
    Context mContext;

    JsonObject res;
    JsonArray farmArray, mdArray;

    String user_id, standard_address;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_total_list);
        
        //상단바 지정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);    //기본 제목을 없애줍니다.
        //actionBar.setDisplayHomeAsUpEnabled(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

        mContext = this;

        firstInit();

        service = retrofit.create(FarmService.class);
        jsonParser = new JsonParser();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        standard_address=intent.getStringExtra("standard_address");
        TextView change_address = (TextView) findViewById(R.id.change_address);
        change_address.setText(standard_address);

        ImageView toolbar_goBack = findViewById(R.id.toolbar_goBack);
        toolbar_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(FarmActivity.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });


        // 지역명
        //상단바 주소변경 누르면 주소변경/선택 페이지로
//        change_address.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Log.d("클릭", "확인");
//                Intent intent = new Intent(FarmActivity.this, EditTownActivity.class);
//                intent.putExtra("user_id", user_id);
//                startActivity(intent);
//            }
//        });


        //농장 정보 불러오기
        Call<ResponseBody> call = service.getFarmData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    res =  (JsonObject) jsonParser.parse(response.body().string());

                    farmArray = res.get("result").getAsJsonArray();
                    mdArray = res.get("md_result").getAsJsonArray();

                    //어뎁터 적용
                    mFarmTotalAdapter = new FarmTotalAdapter(mList);
                    mFarmRecyclerView.setAdapter(mFarmTotalAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mFarmRecyclerView.setLayoutManager(linearLayoutManager);

                    final Geocoder geocoder = new Geocoder(getApplicationContext());

                    for(int i=0;i<farmArray.size();i++){
                        int count = 0;
                        for( int j = 0; j < mdArray.size(); j++){
                            if(mdArray.get(j).getAsJsonObject().get("farm_id").getAsInt() == farmArray.get(i).getAsJsonObject().get("farm_id").getAsInt()){
                                count++;
                            }
                        }
  //                      List<Address> address = geocoder.getFromLocationName(farmArray.get(i).getAsJsonObject().get("farm_loc").getAsString(),10);
    //                    Address location = address.get(0);
      //                  double store_lat=location.getLatitude();
        //                double store_long=location.getLongitude();

                        //자신이 설정한 위치와 스토어 거리 distance 구하기
          //              double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");
                     addFarm("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + farmArray.get(i).getAsJsonObject().get("farm_mainImg").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);

                        //2023.02.21. 이미지 때문인지 오류나서 주석처리함...
                        //addFarm(
                          //      "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + farmArray.get(i).getAsJsonObject().get("farm_thumbnail").getAsString(),farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);
                    }

                    mFarmTotalAdapter.setOnItemClickListener(
                            new FarmTotalAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(FarmActivity.this, FarmDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("standard_address", standard_address);
                                    intent.putExtra("farm_id",farmArray.get(pos).getAsJsonObject().get("farm_id").getAsString());
                                    startActivity(intent);
                                }
                            });
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(FarmActivity.this, "농가 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void firstInit(){
        mFarmRecyclerView = findViewById(R.id.totalOrderListView);
        mList = new ArrayList<>();
    }

    public void addFarm(String farmProdImg, String farmName, String farmMainItem, String farmFeature,int farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmMainItem(farmMainItem);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
