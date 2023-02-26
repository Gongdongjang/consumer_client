package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.farm.FarmActivity;
import com.example.consumer_client.mypage.AboutGDJActivity;
import com.example.consumer_client.mypage.AccountSettingActivity;
import com.example.consumer_client.mypage.UserCenterActivity;
import com.example.consumer_client.notification.NotificationList;
import com.example.consumer_client.shopping_info.ShoppingInfo2Activity;
import com.example.consumer_client.shopping_info.ShoppingInfoActivity;


public class MyPage extends Fragment {
    private View view;
    Activity mActivity;
    String user_id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        Intent intent = mActivity.getIntent(); //intent 값 받기
        user_id=intent.getStringExtra("user_id");
        Bundle extra = getArguments();
        if(extra != null){
            user_id = extra.getString("user_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_my_page, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);

//        LinearLayout myPage_Keep = (LinearLayout) view.findViewById(R.id.MyPage_Keep);
//        myPage_Keep.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), Keep.class);
//                intent.putExtra("user_id", user_id);
//                startActivity(intent);
//            }
//        });
        //알림리스트
        TextView MyPage_Notification = (TextView) view.findViewById(R.id.MyPage_Notification);
        MyPage_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationList.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //나의 쇼핑정보
        LinearLayout shoppingInfo = (LinearLayout) view.findViewById(R.id.MyPage_MyShopping);
        shoppingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mActivity, "현재 오류 수정 중 ♥3♥", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ShoppingInfoActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //나의 계정 정보
        LinearLayout myPage_MyAccountSetting = (LinearLayout) view.findViewById(R.id.MyPage_MyAccountSetting);
        myPage_MyAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //고객 센터
        LinearLayout myPage_UserCenter = (LinearLayout) view.findViewById(R.id.MyPage_UserCenter);
        myPage_UserCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserCenterActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //about 공동장
        LinearLayout myPage_AboutGDJ = (LinearLayout) view.findViewById(R.id.MyPage_AboutGDJ);
        myPage_AboutGDJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutGDJActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
        return view;
    }
}