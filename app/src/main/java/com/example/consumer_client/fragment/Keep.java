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
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.farm.FarmDetailAdapter;
import com.example.consumer_client.farm.FarmDetailInfo;
import com.example.consumer_client.md.JointPurchaseActivity;
import com.example.consumer_client.md.MdListMainActivity;
import com.example.consumer_client.store.StoreTotalAdapter;
import com.example.consumer_client.store.StoreTotalInfo;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;


interface KeeplistService {
    @POST("keeplist")
    Call<ResponseBody> postKeepList(@Body JsonObject body);
}

public class Keep extends Fragment {

    Context mContext;
    Activity mActivity;

    private View view;
    Activity mActivity;
    String userid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        userid=intent.getStringExtra("userid");
    }

    JsonParser jsonParser;
    KeeplistService service;

    private RecyclerView mMdListRecyclerView;
    private ArrayList<FarmDetailInfo> mList;
    private FarmDetailAdapter mMdListMainAdapter;

    JsonObject body;
    JsonObject res;
    JsonArray jsonArray;
    JsonArray pay_schedule;
    JsonArray pu_start;
    JsonArray pu_end;

    String user_id;

    ArrayList<String> keep_list = new ArrayList<String>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("userid");
        Log.d("79행 userid", user_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
//        setContentView(R.layout.activity_farm_detail);
        view= inflater.inflate(R.layout.fragment_keep, container, false);
//        keep_recyclear = (RecyclerView) view.findViewById(R.id.keep_recycler);
//        mStoreList = new ArrayList<>();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_keep, container, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(KeeplistService.class);
        jsonParser = new JsonParser();

        firstInit();

        body = new JsonObject();
        body.addProperty("user_id", user_id);
//        body.addProperty("md_id", md_id);

        //?
        //user_id 찍어보기
//        Intent intent = getActivity().getIntent(); //intent 값 받기
//        user_id=intent.getStringExtra("user_id");
        Log.d("105행 userid", user_id);

        Call<ResponseBody> call = service.postKeepList(body);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    res =  (JsonObject) jsonParser.parse(response.body().string());
                    jsonArray = res.get("keep_list_result").getAsJsonArray();
                    pay_schedule = res.get("pay_schedule").getAsJsonArray();
                    pu_start = res.get("pu_start").getAsJsonArray();
                    pu_end = res.get("pu_end").getAsJsonArray();

                    Log.d("123행 mList", String.valueOf(mList));

                    //어뎁터 적용
                    mMdListMainAdapter = new FarmDetailAdapter(mList);
                    mMdListRecyclerView.setAdapter(mMdListMainAdapter);

                    //세로로 세팅
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mMdListRecyclerView.setLayoutManager(linearLayoutManager);

                    for(int i=0;i<jsonArray.size();i++){
                        keep_list.add(jsonArray.get(i).getAsJsonObject().get("md_id").getAsString());

                        addKeepList("product Img",
                                jsonArray.get(i).getAsJsonObject().get("farm_name").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("md_name").getAsString(),
                                jsonArray.get(i).getAsJsonObject().get("store_name").getAsString(),
                                pay_schedule.get(i).getAsString(),
                                pu_start.get(i).getAsString() + " ~ " + pu_end.get(i).getAsString()
                        );
                    }

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
                }
                catch(Exception e) {
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


//        //추후에 제품 이름 가져올 예정
//        for(int i=0;i<10;i++){
//            //addStore("product Img", "스토어명" + i, "" + 100 + i, "제품명" + i, "" + i, "" + i + "000", "2000.04.0" + i);
//        }
//
//        //어뎁터 적용
//        mMdListMainAdapter = new FarmDetailAdapter(mFarmList);
//        mMdListRecyclerView.setAdapter(mMdListMainAdapter);
//
//        //세로로 세팅
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        keep_recyclear.setLayoutManager(linearLayoutManager);
//
//        return view;
    }

//    public void addStore(String storeProdImgView, String storeName, String storeLocationFromMe, String storeProdName, String storeProdNum, String storeProdPrice, String storePickUpDate){
//        StoreTotalInfo store = new StoreTotalInfo();
//
//        store.setStoreProdImgView(storeProdImgView);
//        store.setStoreName(storeName);
//        store.setStoreLocationFromMe(storeLocationFromMe);
//        store.setStoreProdName(storeProdName);
//        store.setStoreProdNum(storeProdNum);
//        store.setStoreProdPrice(storeProdPrice);
//        store.setStorePickUpDate(storePickUpDate);
//
//        mStoreList.add(store);
//    }

    public void addKeepList(String mdProdImg, String farmName, String prodName, String storeName, String paySchedule, String puTerm){
        FarmDetailInfo mdDetail = new FarmDetailInfo();
        mdDetail.setProdImg(mdProdImg);
        mdDetail.setFarmName(farmName);
        mdDetail.setProdName(prodName);
        mdDetail.setStoreName(storeName);
        mdDetail.setPaySchedule(paySchedule);
        mdDetail.setPuTerm(puTerm);

        mList.add(mdDetail);
    }

    public void firstInit(){
        mMdListRecyclerView = view.findViewById(R.id.keep_recycler);
        mList = new ArrayList<>();
    }
}