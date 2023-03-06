package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.farm.FarmDetailAdapter;
import com.example.consumer_client.md.MdDetailInfo;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.R;
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
import retrofit2.http.POST;


interface KeeplistService {
    @POST("standard_address/getStdAddress")
    Call<ResponseBody> getStdAddress(@Body JsonObject body);

    @POST("keeplist")
    Call<ResponseBody> postKeepList(@Body JsonObject body);
}

public class Keep extends Fragment {

    Context mContext;
    Activity mActivity;

    private View view;

    JsonParser jsonParser;
    KeeplistService service;

    private RecyclerView mMdListRecyclerView;
    private ArrayList<MdDetailInfo> mList;
    private FarmDetailAdapter mMdListMainAdapter;

    JsonObject body;
    JsonObject res;
    JsonArray jsonArray, pu_start, dDay;

    String user_id, standard_address;
    TextView noKeep;
    double myTownLat, myTownLong;

    ArrayList<String> keep_list = new ArrayList<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        standard_address=intent.getStringExtra("standard_address");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getContext();
        view = inflater.inflate(R.layout.fragment_keep, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_keep, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(KeeplistService.class);
        jsonParser = new JsonParser();

        firstInit();

        //상단바 장바구니
        ImageView gotoCart = (ImageView) view.findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        Log.d("user_id", user_id);
        //===주소정보
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);

        Call<ResponseBody> call = service.getStdAddress(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    JsonArray addressArray = res.get("std_address_result").getAsJsonArray();  //json배열
                    standard_address = addressArray.get(0).getAsJsonObject().get("standard_address").getAsString();
//                    if(standard_address.equals("현재위치")){
//                        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
//                        myTownLat=mCurrentLat;
//                        myTownLong=mCurrentLng;
//                    }else{
//                        //mapView.setCurrentLocationTrackingMode( MapView.CurrentLocationTrackingMode.TrackingModeOff );  //현재위치 탐색 중지
                    final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());
                    List<Address> address = geocoder.getFromLocationName(standard_address, 10);
                    Address location = address.get(0);
                    myTownLat = location.getLatitude();
                    myTownLong = location.getLongitude();
//                        // 중심점 변경
//                        //mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(myTownLat, myTownLong), true);
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "기준 주소 정보 받기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });

        body = new JsonObject();
        body.addProperty("user_id", user_id);

        Call<ResponseBody> keepCall = service.postKeepList(body);
        keepCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());
                    jsonArray = res.get("keep_list_result").getAsJsonArray();
//                    pay_schedule = res.get("pay_schedule").getAsJsonArray();
                    pu_start = res.get("pu_start").getAsJsonArray();
                    dDay = res.get("dDay").getAsJsonArray();

                    noKeep = view.findViewById(R.id.noKeep);
//                    Log.d("jsonArray: ", jsonArray.toString());
                    if (jsonArray.size() == 0) noKeep.setText("찜한 상품이 없습니다.");
                    else noKeep.setText("");

                    //어뎁터 적용
                    mMdListMainAdapter = new FarmDetailAdapter(mList);
                    mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mMdListRecyclerView.setLayoutManager(linearLayoutManager);

                    final Geocoder geocoder = new Geocoder(mActivity.getApplicationContext());


                    for (int i = 0; i < jsonArray.size(); i++) {
                        keep_list.add(jsonArray.get(i).getAsJsonObject().get("md_id").getAsString());

                        List<Address> address = geocoder.getFromLocationName(jsonArray.get(i).getAsJsonObject().get("store_loc").getAsString(), 8);
                        Address location = address.get(0);
                        double store_lat = location.getLatitude();
                        double store_long = location.getLongitude();

                        //자신이 설정한 위치와 스토어 거리 distance 구하기
                        double distanceKilo = distance(myTownLat, myTownLong, store_lat, store_long, "kilometer");

                        String realIf0;
                        if (dDay.get(i).getAsString().equals("0")) realIf0 = "D - day";
                        else if (dDay.get(i).getAsInt() < 0)
                            realIf0 = "D + " + Math.abs(dDay.get(i).getAsInt());
                        else realIf0 = "D - " + dDay.get(i).getAsString();

                        addKeepList("https://ggdjang.s3.ap-northeast-2.amazonaws.com/" + jsonArray.get(i).getAsJsonObject().get("mdimg_thumbnail").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                String.format("%.2f", distanceKilo) + "km",
                                jsonArray.get(i).getAsJsonObject().get("pay_price").getAsString(),
                                realIf0,
                                pu_start.get(i).getAsString());
                    }

                    //거리 가까운순으로 정렬
                    mList.sort(new Comparator<MdDetailInfo>() {
                        @Override
                        public int compare(MdDetailInfo o1, MdDetailInfo o2) {
                            int ret;
                            Double distance1 = Double.valueOf(o1.getDistance().substring(o1.getDistance().length() - 2));
                            Double distance2 = Double.valueOf(o2.getDistance().substring(o2.getDistance().length() - 2));
                            //거리비교
                            ret = distance1.compareTo(distance2);
                            Log.d("ret", String.valueOf(distance1));
                            return ret;
                        }
                    });

                    mMdListMainAdapter.setOnItemClickListener(
                            new FarmDetailAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View v, int pos) {
                                    Intent intent = new Intent(mActivity, JointPurchaseActivity.class);
                                    intent.putExtra("md_id", keep_list.get(pos));
                                    intent.putExtra("user_id", user_id);
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
                Toast.makeText(mActivity, "상품 띄우기 에러 발생", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void addKeepList(String mdProdImg, String prodName, String storeName, String distance, String mdPrice, String dDay, String puTime) {
        MdDetailInfo mdDetail = new MdDetailInfo();
        mdDetail.setProdImg(mdProdImg);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setDistance(distance);
        mdDetail.setMdPrice(mdPrice);
        mdDetail.setDday(dDay);
        mdDetail.setPuTime(puTime);

        mList.add(mdDetail);
    }

    public void firstInit() {
        mMdListRecyclerView = view.findViewById(R.id.keep_recycler);
        mList = new ArrayList<>();
    }
}
