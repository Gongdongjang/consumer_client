package com.gdjang.consumer_client.store;

import static com.gdjang.consumer_client.address.LocationDistance.distance;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.BackPressDialog;
import com.gdjang.consumer_client.MainActivity;
import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.cart.CartListActivity;
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

import retrofit2.http.GET;

interface StoreService {
    @GET("/storeView")
    Call<ResponseBody> getStoreData();

}

public class StoreActivity extends AppCompatActivity {
    StoreService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray storeArray, pu_start;

    private RecyclerView mStoreRecyclerView;
    private ArrayList<StoreTotalInfo> mList;
    private StoreTotalAdapter mStoreTotalAdapter;
    Context mContext;

    String user_id, standard_address;
    double myTownLat;
    double myTownLong;
    BackPressDialog backPressDialog;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_total_list);
        
        //상단바 지정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowTitleEnabled(false);    //기본 제목을 없애줍니다.
//        actionBar.setDisplayHomeAsUpEnabled(true);

        mContext = this;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(StoreService.class);
        jsonParser = new JsonParser();

        firstInit();

        Intent intent = getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        standard_address=intent.getStringExtra("standard_address");
        TextView change_address = (TextView) findViewById(R.id.change_address);
        change_address.setText(standard_address);

        //뒤로가기
        ImageView toolbar_goBack = findViewById(R.id.toolbar_goBack);
        toolbar_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LayoutInflater inflater = getLayoutInflater();
//
//                View layout = inflater.inflate(R.layout.activity_main, (ViewGroup) findViewById(R.id.HomeNavi));
////                onBackPressed();
////                FrameLayout fl = layout.findViewById(R.id.Main_Frame);
//                fm = getSupportFragmentManager();
//                ft = fm.beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("user_id", user_id);
//                TotalList tl = new TotalList();
//                tl.setArguments(bundle);
//                ft.replace(layout.getId(), tl).commit();
                Intent intent1 = new Intent(StoreActivity.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView toolbar_cart = findViewById(R.id.toolbar_cart);
        toolbar_cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StoreActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //===기준 주소정보
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);
        
        final Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocationName(standard_address,10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = address.get(0);
        myTownLat = location.getLatitude();
        myTownLong=location.getLongitude();

        // 지역명
        //상단바 주소변경 누르면 주소변경/선택 페이지로
//        change_address.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Log.d("클릭", "확인");
//                Intent intent = new Intent(StoreActivity.this, EditTownActivity.class);
//                intent.putExtra("user_id", user_id);
//                startActivity(intent);
//            }
//        });

        //-----스토어 정보 불러오기
        Call<ResponseBody> call = service.getStoreData();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    storeArray = res.get("store_result").getAsJsonArray();  //json배열
                    pu_start = res.get("pu_start").getAsJsonArray();

                    //어뎁터 적용
                    mStoreTotalAdapter = new StoreTotalAdapter(mList);
                    mStoreRecyclerView.setAdapter(mStoreTotalAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mStoreRecyclerView.setLayoutManager(linearLayoutManager);

                    final Geocoder geocoder = new Geocoder(getApplicationContext());

                    for(int i=0;i<storeArray.size() ;i++) {
                        //스토어위치->위도, 경도 구하기
                        String store_loc= storeArray.get(i).getAsJsonObject().get("store_loc").getAsString();
                        List<Address> address=  geocoder.getFromLocationName(store_loc,10);
                        Address location = address.get(0);
                        double store_lat=location.getLatitude();
                        double store_long=location.getLongitude();

                        //자신이 설정한 위치와 스토어 거리 distance 구하기
                        double distanceKilo =
                                distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");

                        addStore(storeArray.get(i).getAsJsonObject().get("store_id").getAsString(),
                                "https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + storeArray.get(i).getAsJsonObject().get("store_mainImg").getAsString(),
                                storeArray.get(i).getAsJsonObject().get("store_name").getAsString(), String.format("%.2f", distanceKilo), storeArray.get(i).getAsJsonObject().get("store_info").getAsString(), storeArray.get(i).getAsJsonObject().get("md_name").getAsString(), storeArray.get(i).getAsJsonObject().get("pay_price").getAsString(), pu_start.get(i).getAsString());
                    }
                    //거리 가까운순으로 정렬
                    mList.sort(new Comparator<StoreTotalInfo>() {
                        @Override
                        public int compare(StoreTotalInfo o1, StoreTotalInfo o2) {
                            int ret;
                            Double distance1 = Double.valueOf(o1.getStoreLocationFromMe());
                            Double distance2 = Double.valueOf(o2.getStoreLocationFromMe());
                            //거리비교
                            ret= distance1.compareTo(distance2);
                            return ret;
                        }
                    });

                    mStoreTotalAdapter.setOnItemClickListener (
                            new StoreTotalAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(StoreActivity.this, StoreDetailActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("standard_address", standard_address);
                                    intent.putExtra("storeid", mList.get(pos).getStoreid());
                                    startActivity(intent);
                                }
                            }
                    );

                    } catch (IOException e) {
                        e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(StoreActivity.this, "전체스토어 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("스토어", t.getMessage());
            }
        });
    }

    public void firstInit(){
        mStoreRecyclerView = findViewById(R.id.totalStoreView);
        mList = new ArrayList<>();
    }

    public void addStore(String storeId, String storeProdImgView, String storeName, String storeLocationFromMe, String storeInfo, String storeProdName, String storProdPrice, String storeProdDate){
        StoreTotalInfo store = new StoreTotalInfo();

        store.setStoreid(storeId);
        store.setStoreProdImgView(storeProdImgView);
        store.setStoreName(storeName);
        store.setStoreLocationFromMe(storeLocationFromMe);
        store.setStoreInfo(storeInfo);
        store.setStoreProdName(storeProdName);
        store.setStoreProdPrice(storProdPrice);
        store.setStoreProdDate(storeProdDate);
        mList.add(store);
    }

//    @Override
//    public void onBackPressed() {
//        backPressDialog = new BackPressDialog(mContext);
//        backPressDialog.show();
//    }
}
