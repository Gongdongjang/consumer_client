package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.consumer_client.farm.FarmActivity;
import com.example.consumer_client.md.MdListMainActivity;
import com.example.consumer_client.my_town.StoreMap;
import com.example.consumer_client.store.StoreActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface AddressService {

    @POST("standard_address/getStdAddress")
    Call<ResponseBody> getStdAddress(@Body JsonObject body);  //post user_id
}

public class TotalList extends Fragment {

    AddressService service;
    JsonParser jsonParser;
    JsonObject res;
    String standard_address;

    private View view;
    Activity mActivity;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(AddressService.class);
        jsonParser = new JsonParser();

        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);


        //기준 주소 정보
        Call<ResponseBody> address_call = service.getStdAddress(body);
        address_call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    JsonArray addressArray = res.get("std_address_result").getAsJsonArray();  //json배열
                    standard_address = addressArray.get(0).getAsJsonObject().get("standard_address").getAsString();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               //Toast.makeText(getApplicationContext(), "기준 주소 정보 받기 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_total_list, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_total_list, container, false);

        //서비스 전체보기 (전체 입점농가)
        TextView totalFarmTextView = (TextView) view.findViewById(R.id.showTotalFarm);

        totalFarmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FarmActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", standard_address);
                startActivity(intent);
            }
        });

        //서비스 전체보기 (전체 스토어)
        TextView totalStoreTextView = (TextView) view.findViewById(R.id.showTotalStore);
        totalStoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", standard_address);
                startActivity(intent);
            }
        });

        //우리동네 상품보기 (지도로 스토어보기)
        TextView showStoreLocTextView = (TextView) view.findViewById(R.id.showStoreLoc);
        showStoreLocTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreMap.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", standard_address);
                startActivity(intent);
            }
        });

        //우리동네 상품보기 (진행중인 제품보기)
        TextView showProductTextView = (TextView) view.findViewById(R.id.showProduct);
        showProductTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MdListMainActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("standard_address", standard_address);
                startActivity(intent);
            }
        });

        MyPage myPage = new MyPage();
        LinearLayout mypage = (LinearLayout) view.findViewById(R.id.H_MyPage);
        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MyPage myPage = new MyPage();
                transaction.replace(R.id.Main_Frame, myPage);
                transaction.commit();
            }
        });
        return view;
    }
}