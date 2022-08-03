package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.consumer_client.R;
import com.example.consumer_client.farm.FarmActivity;


public class MyPage extends Fragment {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_my_page, container, false);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);

        TextView totalFarmTextView = (TextView) view.findViewById(R.id.MyPage_MS_ProdReview);
//        totalFarmTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FarmActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }
}