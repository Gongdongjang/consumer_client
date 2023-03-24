package com.gdjang.consumer_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.gdjang.consumer_client.fragment.Home;
import com.gdjang.consumer_client.fragment.Keep;
import com.gdjang.consumer_client.fragment.MyPage;
import com.gdjang.consumer_client.fragment.Order;
import com.gdjang.consumer_client.fragment.TotalList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface AddresssService {
    @POST("standard_address/getStdAddress")
    Call<ResponseBody> getStdAddress(@Body JsonObject body);  //post user_id
}

public class MainActivity extends AppCompatActivity {

    JsonParser jsonParser;
    AddresssService service;

    JsonObject res;

    private BottomNavigationView bottomNavigation;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private TotalList frag1;
    private Keep frag2;
    private Home frag3;
    private Order frag4;
    private MyPage frag5;
    private TextView change_address;
    private ImageView toolbar_cart;
    public Context mContext;
    BackPressDialog backPressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(AddresssService.class);
        jsonParser = new JsonParser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Intent intent = getIntent(); //intent 값 받기

        //유저id 받기
        String user_id;
        user_id=intent.getStringExtra("user_id");    //첫 튜토리얼시 findtown에서 넘어온 + EditTownActivity에서 넘어온
        intent.putExtra("user_id",user_id);

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

        bottomNavigation.setSelectedItemId(R.id.tab_home);
        setFrag(2); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택
    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction(); //실제 교체 시 ..
        switch (n) {
            case 0:
                ft.replace(R.id.Main_Frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Main_Frame, frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_Frame, frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.Main_Frame, frag4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.Main_Frame, frag5);
                ft.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backPressDialog = new BackPressDialog(mContext);
        backPressDialog.show();
    }
}