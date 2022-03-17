package com.example.consumer_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.fragment.Keep;
import com.example.consumer_client.fragment.MyPage;
import com.example.consumer_client.fragment.Order;
import com.example.consumer_client.fragment.TotalList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Fragment frag1;
    Fragment frag2;
    Fragment frag3;
    Fragment frag4;
    Fragment frag5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frag1= new TotalList();
        frag2= new Keep();
        frag3= new Home();
        frag4= new Order();
        frag5= new MyPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.HomeNavi,frag3).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navi);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()) {
                            case R.id.tab_total:
                                //Toast.makeText(getApplicationContext(), "첫번째", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.HomeNavi, frag1)
                                        .commit();
                                return true;

                            case R.id.tab_keep:
                                //Toast.makeText(getApplicationContext(), "두번째", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.HomeNavi, frag2)
                                        .commit();
                                return true;

                            case R.id.tab_home:
                                //Toast.makeText(getApplicationContext(), "두번째", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.HomeNavi, frag3)
                                        .commit();
                                return true;

                            case R.id.tab_order:
                                //Toast.makeText(getApplicationContext(), "두번째", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.HomeNavi, frag4)
                                        .commit();
                                return true;

                            case R.id.tab_mypage:
                                //Toast.makeText(getApplicationContext(), "두번째", Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.HomeNavi, frag5)
                                        .commit();
                                return true;
                        }

                        return false;
                    }

                });

    }
}