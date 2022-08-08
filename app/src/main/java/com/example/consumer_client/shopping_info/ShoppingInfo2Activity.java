package com.example.consumer_client.shopping_info;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.consumer_client.R;

public class ShoppingInfo2Activity extends TabActivity {
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_tab_host);;

        Intent intent = getIntent(); //intent 값 받기
        userid=intent.getStringExtra("userid");


        TabHost tabHost = getTabHost(); //탭 호스트 객체 생성

        // 탭스팩 선언하고, 탭의 내부 명칭, 탭에 출력될 글 작성
        TabHost.TabSpec spec;

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this,OrderList.class);
        intent.putExtra("userid", userid);
        spec = tabHost.newTabSpec("ShoppingInfo"); // 객체를 생성
        spec.setIndicator("상세주문내역"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        //탭에서 액티비티를 사용할 수 있도록 인텐트 생성
        intent = new Intent().setClass(this, ReviewList.class);
        intent.putExtra("userid", userid);
        spec = tabHost.newTabSpec("ReviewList"); // 객체를 생성
        spec.setIndicator("상품리뷰"); //탭의 이름 설정
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1); //먼저 열릴 탭을 선택
    }
}
