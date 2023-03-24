package com.gdjang.consumer_client.md;

import static com.gdjang.consumer_client.address.LocationDistance.distance;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdjang.consumer_client.CustomSpinnerAdapter;
import com.gdjang.consumer_client.MainActivity;
import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.cart.CartListActivity;
import com.gdjang.consumer_client.farm.FarmDetailAdapter;
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

interface MdService {
    @GET("mdView_main")
    Call<ResponseBody> getMdMainData();
}

public class MdListMainActivity extends AppCompatActivity {

    JsonParser jsonParser;
    MdService service;

    private RecyclerView mMdListRecyclerView;
    private ArrayList<MdDetailInfo> mList;
    private FarmDetailAdapter mMdListMainAdapter;
    Context mContext;

    JsonObject res;
    JsonArray jsonArray;
    JsonArray pu_start;
    JsonArray dDay;

    String user_id, standard_address;
    double myTownLat;
    double myTownLong;

    private List<String> list = new ArrayList<>();
    private Spinner spinner;
    private CustomSpinnerAdapter adapter;
    private String selectedItem, distance_what;
    private Double distance_std;
    private ImageView gotoBack;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_total_list);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MdService.class);
        jsonParser = new JsonParser();
        mContext = this;

        firstInit();

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        standard_address = intent.getStringExtra("standard_address");
        TextView myaddress = (TextView) findViewById(R.id.myaddress);
        myaddress.setText(standard_address);

        //뒤로가기
        gotoBack = findViewById(R.id.gotoBack);
        gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MdListMainActivity.this, MainActivity.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }
        });

        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MdListMainActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        final Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> myAddr = null;
        try {
            myAddr = geocoder.getFromLocationName(standard_address, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Address location = myAddr.get(0);
        myTownLat = location.getLatitude();
        myTownLong = location.getLongitude();

        Call<ResponseBody> call = service.getMdMainData();
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());
                    jsonArray = res.get("md_result").getAsJsonArray();
//                    pay_schedule = res.get("pay_schedule").getAsJsonArray();
                    pu_start = res.get("pu_start").getAsJsonArray();
                    dDay = res.get("dDay").getAsJsonArray();

                    //제품 상세 어뎁터 적용
                    mMdListMainAdapter = new FarmDetailAdapter(mList);
                    mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MdListMainActivity.this, 2, GridLayoutManager.VERTICAL, false);
                    mMdListRecyclerView.setLayoutManager(gridLayoutManager);

                    for (int i = 0; i < jsonArray.size(); i++) {
                        List<Address> address = null;
                        try {
                            address = geocoder.getFromLocationName(jsonArray.get(i).getAsJsonObject().get("store_loc").getAsString(), 8);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address location = address.get(0);
                        double store_lat = location.getLatitude();
                        double store_long = location.getLongitude();
                        //자신이 설정한 위치와 스토어 거리 distance 구하기
                        double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");
                        //double distanceMeter = distance(myTownLat, myTownLong, store_lat, store_long, "meter");

                        //if (Double.compare(distance_std, distanceKilo) > 0) { //4km 이내 제품들만 보이기
                        //(스토어 데이터가 많이 없으므로 0.4대신 1로 test 중, 기능은 완료)

                        String realIf0;
                        if (dDay.get(i).getAsString().equals("0")) realIf0 = "D - day";
                        else if (dDay.get(i).getAsInt() < 0)
                            realIf0 = "D + " + Math.abs(dDay.get(i).getAsInt());
                        else realIf0 = "D - " + dDay.get(i).getAsString();

                        addMdList("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("md_id").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                String.format("%.2f", distanceKilo) + "km",
                                jsonArray.get(i).getAsJsonObject().get("pay_price").getAsString(),
                                realIf0,
                                pu_start.get(i).getAsString()
                        );
                    }

                    //거리 가까운순으로 정렬
                    mList.sort(new Comparator<MdDetailInfo>() {
                        @Override
                        public int compare(MdDetailInfo o1, MdDetailInfo o2) {
                            int ret;
                            Double distance1 = Double.valueOf(o1.getDistance().substring(0, o1.getDistance().length() - 2));
                            Double distance2 = Double.valueOf(o2.getDistance().substring(0, o2.getDistance().length() - 2));
                            //거리비교
                            ret = distance1.compareTo(distance2);
                            return ret;
                        }
                    });

                    mMdListMainAdapter.setOnItemClickListener(
                            new FarmDetailAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(MdListMainActivity.this, JointPurchaseActivity.class);
                                    intent.putExtra("user_id", user_id);
                                    intent.putExtra("standard_address", standard_address);
                                    intent.putExtra("md_id", mList.get(pos).getMdId());
                                    startActivity(intent);
                                }
                            }
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MdListMainActivity.this, "상품 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void firstInit() {
        mMdListRecyclerView = findViewById(R.id.totalMdListView);
        mList = new ArrayList<>();
    }

    public void addMdList(String mdProdImg, String mdId,
                          String prodName, String storeName, String distance, String mdPrice, String dDay, String puTime) {
        MdDetailInfo mdDetail = new MdDetailInfo();
        mdDetail.setProdImg(mdProdImg);
        mdDetail.setMdId(mdId);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setDistance(distance);
        mdDetail.setMdPrice(mdPrice);
        mdDetail.setDday(dDay);
        // 미터 및 픽업 예정일 추가해야돼
        mdDetail.setPuTime(puTime);

        mList.add(mdDetail);
    }
}
