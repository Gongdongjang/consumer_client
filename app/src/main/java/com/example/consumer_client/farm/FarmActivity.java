package com.example.consumer_client.farm;

import static com.example.consumer_client.address.LocationDistance.distance;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    String user_id;

    private TextView change_address;
    double myTownLat;   //추가
    double myTownLong;  //추가

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

        //===기준 주소정보
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);

        change_address = findViewById(R.id.change_address);

        Call<ResponseBody> address_call = service.getStdAddress(body);
        address_call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    JsonArray addressArray = res.get("std_address_result").getAsJsonArray();  //json배열
                    String standard_address = addressArray.get(0).getAsJsonObject().get("standard_address").getAsString();
                    change_address.setText(standard_address);
                    final Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> address = geocoder.getFromLocationName(standard_address,10);
                    Address location = address.get(0);
                    myTownLat = location.getLatitude();
                    myTownLong=location.getLongitude();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "기준 주소 정보 받기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });

        // 지역명
        //상단바 주소변경 누르면 주소변경/선택 페이지로
        change_address.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("클릭", "확인");
                Intent intent = new Intent(FarmActivity.this, EditTownActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });


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
//<<<<<<< feature_home_design
  //                      List<Address> address = geocoder.getFromLocationName(farmArray.get(i).getAsJsonObject().get("farm_loc").getAsString(),10);
    //                    Address location = address.get(0);
      //                  double store_lat=location.getLatitude();
        //                double store_long=location.getLongitude();

                        //자신이 설정한 위치와 스토어 거리 distance 구하기
          //              double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");
            //            addFarm("product Img", farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);
// =======
                        addFarm(
                                "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + farmArray.get(i).getAsJsonObject().get("farm_thumbnail").getAsString(),farmArray.get(i).getAsJsonObject().get("farm_name").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_mainItem").getAsString(), farmArray.get(i).getAsJsonObject().get("farm_info").getAsString(), count);
                    }

                    //거리 가까운순으로 정렬
//                    mList.sort(new Comparator<FarmTotalInfo>() {
//                        @Override
//                        public int compare(FarmTotalInfo o1, FarmTotalInfo o2) {
//                            int ret;
//                            Double distance1 = Double.valueOf(o1.getHomeDistance());
//                            Double distance2 = Double.valueOf(o2.getHomeDistance());
//                            //거리비교
//                            ret= distance1.compareTo(distance2);
//                            return ret;
//                        }
//                    });

                    mFarmTotalAdapter.setOnItemClickListener(
                            new FarmTotalAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(FarmActivity.this, FarmDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
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

    public void addFarm(String farmProdImg, String farmName, String farmProdName, String farmFeature,int farmSituation){
        FarmTotalInfo farm = new FarmTotalInfo();

        farm.setFarmProdImgView(farmProdImg);
        farm.setFarmName(farmName);
        farm.setFarmProdName(farmProdName);
        farm.setFarmFeature(farmFeature);
        farm.setFarmSituation(farmSituation);

        mList.add(farm);
    }
}
