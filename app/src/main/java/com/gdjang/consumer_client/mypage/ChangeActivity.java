package com.gdjang.consumer_client.mypage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gdjang.consumer_client.R;
import com.gdjang.consumer_client.fragment.MyPage;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;
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

interface SNS_Service{
    @GET("/check_id")
    Call<ResponseBody> check_id(@Query("user_id") String user_id);
}

public class ChangeActivity extends AppCompatActivity {
    private static final String[] PERMISSION_ARRAY = null;
    SNS_Service service;
    JsonParser jsonParser;
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    AccountSettingDialog accountSettingDialog;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_change);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SNS_Service.class);
        jsonParser = new JsonParser();
        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        //로그인 정보
        LinearLayout MyPage_MyLoginSetting = (LinearLayout) findViewById(R.id.MyPage_MyLoginSetting);
        TextView MyPage_MyLoginSetting_Text = findViewById(R.id.MyPage_MyLoginSetting_Text);
        ImageView MyPage_MyLoginSetting_Image = findViewById(R.id.MyPage_MyLoginSetting_Image);
        // 소셜 확인 서버 통신

        Call<ResponseBody> call = service.check_id(user_id);
        call.enqueue(new Callback<ResponseBody>() {
                         @Override
                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                             try {
                                 JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                 String message = res.get("message").getAsString();
                                 if (message.equals("sns")){
//                                     MyPage_MyLoginSetting.setVisibility(View.INVISIBLE);
                                     MyPage_MyLoginSetting.setBackgroundColor(Color.parseColor("#F6F6F6"));
                                     MyPage_MyLoginSetting_Text.setVisibility(View.INVISIBLE);
                                     MyPage_MyLoginSetting_Image.setVisibility(View.INVISIBLE);
                                 } else if (message.equals("not_sns")){
                                     MyPage_MyLoginSetting.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(ChangeActivity.this, LoginSettingActivity.class);
                                             intent.putExtra("user_id", user_id);
                                             startActivity(intent);
                                         }
                                     });
                                 } else {
                                     Toast.makeText(ChangeActivity.this, "아이디 오류 발생", Toast.LENGTH_SHORT).show();
                                 }

                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }

                         @Override
                         public void onFailure(Call<ResponseBody> call, Throwable t) {

                         }
                     }
        );

        //회원 정보
        LinearLayout MyPage_MyAccountSetting = (LinearLayout) findViewById(R.id.MyPage_MyAccountSetting);
        MyPage_MyAccountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeActivity.this, AccountSettingActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //알림 권한 설정
        Switch switch_notification=(Switch) findViewById(R.id.switch_notification);
        switch_notification.setVisibility(View.INVISIBLE);

        LinearLayout MyPage_Notification=(LinearLayout) findViewById(R.id.MyPage_Notification_Setting);
        MyPage_Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        //Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }
                };

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    TedPermission.create()
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("공동장의 알림을 받고 싶다면 \n\n [설정]>[권한]에서 알림을 허용해주세요.")
                            .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
                            .check();
                }

            }

        });


//        if(getNotificationPermisseionEnable(mContext)) {
//            Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//            switch_notification.setChecked(true);
//        }
//        else {
//            Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//            switch_notification.setChecked(false);
//        }

//        switch_notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                PermissionListener permissionlistener = new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
//                        Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
//                        //if(getNotificationPermisseionEnable(mContext)) {
//                          //  Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                            //switch_notification.setChecked(true);
//                        //}
//                        //else {
//                          //  Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                           // switch_notification.setChecked(false);
//                       // }
//                    }
//
//                    @Override
//                    public void onPermissionDenied(List<String> deniedPermissions) {
//                        Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//                       // if(getNotificationPermisseionEnable(mContext)) {
//                         //   Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                          //  switch_notification.setChecked(true);
//                       // }
//                       // else {
//                         //   Log.d("알림팝업:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                            //switch_notification.setChecked(false);
//                        //}
//                    }
//                };
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    TedPermission.create()
//                            .setPermissionListener(permissionlistener)
//                            .setDeniedMessage("공동장의 알림을 받고 싶다면 \n\n [설정]>[권한]에서 알림을 허용해주세요.")
//                            .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
//                            .check();
//                }
//
//                if(getNotificationPermisseionEnable(mContext)) {
//                    Log.d("알림팝업129:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                    switch_notification.setChecked(true);
//                }
//                else {
//                    Log.d("알림팝업133:", String.valueOf(getNotificationPermisseionEnable(mContext)));
//                    switch_notification.setChecked(false);
//                }
//
//            }
//
//        });

//        LinearLayout MyPage_Notification_Setting = (LinearLayout) findViewById(R.id.MyPage_Notification_Setting);
//        MyPage_MyAccountSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ChangeActivity.this, AccountSettingActivity.class);
//                intent.putExtra("user_id", user_id);
//                startActivity(intent);
//            }
//        });
    }

    public static boolean getNotificationPermisseionEnable(Context mContext){

        // [초기 리턴 변수 선언]
        boolean resultData = true;

        // [로직 처리 수행 실시]
        try {
            if (ContextCompat.checkSelfPermission(mContext, PERMISSION_ARRAY[16]) == PackageManager.PERMISSION_GRANTED){
                Log.d("알림권한", "허용O");
                // [리턴 결과 삽입 실시]
                resultData = true;

            }
            else {
                Log.d("알림권한", "허용X");
                // [리턴 결과 삽입 실시]
                resultData = false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // [결과 리턴 실시]
        return resultData;
    }
}
