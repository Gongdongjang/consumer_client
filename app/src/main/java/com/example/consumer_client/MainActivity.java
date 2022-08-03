package com.example.consumer_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.fragment.Keep;
import com.example.consumer_client.fragment.MyPage;
import com.example.consumer_client.fragment.Order;
import com.example.consumer_client.fragment.TotalList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); //intent 값 받기

        //유저id 받기
        String userid;
        String generalid = intent.getStringExtra("generalid");
        String kakaoid = intent.getStringExtra("kakaoid");
        String googleid = intent.getStringExtra("googleid");

        if(generalid != null) userid=generalid;
        else if(kakaoid !=null) userid=kakaoid;
        else if(googleid !=null) userid=googleid;
        else userid=intent.getStringExtra("userid");    //첫 튜토리얼시 findtown에서 넘어온 userid

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
            intent.putExtra("userid",userid);
            startActivity(intent);
        } else{
           //최초 로그인 아닐때
           intent.putExtra("userid",userid);
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