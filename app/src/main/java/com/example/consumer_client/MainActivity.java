package com.example.consumer_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.consumer_client.address.EditTownActivity;
import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.fragment.Keep;
import com.example.consumer_client.fragment.MyPage;
import com.example.consumer_client.fragment.Order;
import com.example.consumer_client.fragment.TotalList;
import com.example.consumer_client.md.MdListMainActivity;
import com.example.consumer_client.tutorial.TutorialActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TotalList frag1;
    private Keep frag2;
    private Home frag3;
    private Order frag4;
    private MyPage frag5;
    private TextView change_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //상단바 지정
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);    //기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent(); //intent 값 받기

//        String standard_address;
//        String address=intent.getStringExtra("standard_address");
//        if(address != null) address="현재위치";
//        else address= standard_address;

        //유저id 받기
        String user_id;
        String generalid = intent.getStringExtra("generalid");
        String kakaoid = intent.getStringExtra("kakaoid");
        String googleid = intent.getStringExtra("googleid");
        
        if(generalid != null) user_id=generalid;
        else if(kakaoid !=null) user_id=kakaoid;
        else if(googleid !=null) user_id=googleid;
        else user_id=intent.getStringExtra("user_id");    //첫 튜토리얼시 findtown에서 넘어온 user_id

        // 최초 실행 여부를 판단 ->>>
        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);

       if(!checkFirst) //(false일 경우 최초 실행)
        {
            // 앱 최초 실행시 근처동네 찾기 세팅하기
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst",true);
            editor.apply();
            finish();

            intent = new Intent(MainActivity.this, TutorialActivity.class);
            intent.putExtra("user_id",user_id);
            //intent.putExtra("standard_address",standard_address);   //나중에..
            startActivity(intent);
        } else{
           //최초 로그인 아닐때
           intent.putExtra("user_id",user_id);
           //intent.putExtra("standard_address",standard_address);
       }

        bottomNavigation = findViewById(R.id.bottom_navi);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()) {
                            case R.id.tab_total:
                                setFrag(0);
                                break;

                            case R.id.tab_keep:
                                setFrag(1);
                                break;

                            case R.id.tab_home:
                                setFrag(2);
                                break;

                            case R.id.tab_order:
                                setFrag(3);
                                break;

                            case R.id.tab_mypage:
                                setFrag(4);
                                break;
                        }

                        return true;
                    }
                });
                frag1= new TotalList();
                frag2= new Keep();
                frag3= new Home();
                frag4= new Order();
                frag5= new MyPage();

                setFrag(2); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택

        // 지역명
        //상단바 주소변경 누르면 주소변경/선택 페이지로
        change_address = findViewById(R.id.change_address);
        change_address.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("클릭", "확인");
                Intent intent = new Intent(MainActivity.this, EditTownActivity.class);
                intent.putExtra("user_id",user_id);
                startActivity(intent);
            }
        });
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); //실제 교체 시 ..
        switch (n) {
            case 0:
//                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                ft.replace(R.id.Main_Frame, frag1);
                ft.commit();
                break;
            case 1:
//                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                ft.replace(R.id.Main_Frame, frag2);
                ft.commit();
                break;
            case 2:
//                Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
                ft.replace(R.id.Main_Frame, frag3);
                ft.commit();
                break;
            case 3:
//                Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
                ft.replace(R.id.Main_Frame, frag4);
                ft.commit();
                break;
            case 4:
//                Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
                ft.replace(R.id.Main_Frame, frag5);
                ft.commit();
                break;
        }

    }
}