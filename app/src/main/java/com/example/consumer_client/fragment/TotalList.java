package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.consumer_client.farm.FarmActivity;
import com.example.consumer_client.farm.FarmDetailAdapter;
import com.example.consumer_client.farm.FarmDetailInfo;
import com.example.consumer_client.store.StoreActivity;
import com.example.consumer_client.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class TotalList extends Fragment {
    private View view;
    Activity mActivity;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_total_list, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_total_list, container, false);

        TextView totalFarmTextView = (TextView) view.findViewById(R.id.showTotalFarm);
        Log.d("Totallist", user_id);
        totalFarmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FarmActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        TextView totalStoreTextView = (TextView) view.findViewById(R.id.showTotalStore);
        totalStoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StoreActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        return view;
    }
}