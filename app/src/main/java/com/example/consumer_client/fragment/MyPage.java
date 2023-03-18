package com.example.consumer_client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.consumer_client.R;
import com.example.consumer_client.alarm.Alarm;
import com.example.consumer_client.cart.CartListActivity;
import com.example.consumer_client.mypage.AboutGDJActivity;
import com.example.consumer_client.mypage.ChangeActivity;
import com.example.consumer_client.shopping_info.ShoppingInfoActivity;
import com.example.consumer_client.user.IntegratedLoginActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
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
import retrofit2.http.GET;
import retrofit2.http.Query;

interface MyPageService {
    @GET("mypage")
    Call<ResponseBody> getUserName(@Query("user_id") String user_id);
    @GET("logout")
    Call<ResponseBody> logout();
}

public class MyPage extends Fragment {
    private View view;
    Activity mActivity;
    String user_id;
    Context mContext;
    JsonParser jsonParser;
    MyPageService service;
    JsonArray user_info;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_my_page, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);

        mContext = view.getContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = (MyPageService) retrofit.create(MyPageService.class);
        jsonParser = new JsonParser();

        Call<ResponseBody> call = service.getUserName(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        user_info = res.get("user_info").getAsJsonArray();
                        String user_name = user_info.get(0).getAsJsonObject().get("user_name").getAsString();
                        TextView MyPage_UserName = view.findViewById(R.id.MyPage_UserName);
                        MyPage_UserName.setText(user_name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("MyPage", "onFailure: e " + t.getMessage());
            }
        });

        //상단바 장바구니
        ImageView gotoCart = (ImageView) view.findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(mActivity, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        LinearLayout myPage_Keep = (LinearLayout) view.findViewById(R.id.MyPage_Keep);
        myPage_Keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Keep keep = new Keep();
                transaction.replace(R.id.Main_Frame, keep);
                transaction.commit();
            }
        });

        //알림리스트
        LinearLayout MyPage_Notification = (LinearLayout) view.findViewById(R.id.MyPage_Notification);
        MyPage_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Alarm.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //나의 쇼핑정보
        LinearLayout shoppingInfo = (LinearLayout) view.findViewById(R.id.MyPage_MyShopping);
        shoppingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShoppingInfoActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //나의 계정 정보
        LinearLayout MyPage_MyAccountSetting = (LinearLayout) view.findViewById(R.id.MyPage_MyAccountSetting);
        MyPage_MyAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeActivity.class);
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

        //오픈소스 라이선스
        LinearLayout myPage_OpenSource = (LinearLayout) view.findViewById(R.id.MyPage_OpenSource);
        myPage_OpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OssLicensesMenuActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃
        TextView btn_logout = (TextView) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = service.logout();
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("MyPage", "onFailure: e " + t.getMessage());
                    }
                });

                Intent intent = new Intent(getActivity(), IntegratedLoginActivity.class);
                startActivity(intent);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                System.exit(0);
            }
        });

        return view;
    }
}