package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.fragment.MyPage;
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

interface ChangeAccountService{
    @GET("/change_name")
    Call<ResponseBody> change_name(@Query("user_id") String user_id, @Query("user_name") String user_name);
    @GET("/change_phone")
    Call<ResponseBody> change_phone(@Query("user_id") String user_id, @Query("mobile_no") String mobile_no);
}

public class AccountSettingActivity extends AppCompatActivity {
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    AccountSettingDialog accountSettingDialog;
    LinearLayout linearLayout;
    EditText ED_name, ED_mobile;
    String user_name, mobile_no;

    ChangeAccountService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray change_name, change_phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit_userinfo);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ChangeAccountService.class);
        jsonParser = new JsonParser();

        Button cancelBtn = findViewById(R.id.CancelBtn_MP);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountSettingDialog = new AccountSettingDialog(mContext, user_id);
                accountSettingDialog.show();
            }
        });

        ED_name = findViewById(R.id.EditProfileName_MP);
        user_name = ED_name.getText().toString();
        ED_mobile = findViewById(R.id.EditPhone_MP);
        mobile_no = ED_mobile.getText().toString();

        Button changeBtn = findViewById(R.id.ConfirmBtn_MP);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경 프로세스
                // 이름 변경
                if (ED_name.getText().toString() != null && ED_mobile.getText().toString() == null){
                    Log.d("???????? >>", ED_name.getText().toString());
                    Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                                     @Override
                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                         try {
                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<ResponseBody> call, Throwable t) {

                                         Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
                                     }
                                 }
                    );
                    // 뒤로가기
                    finish();
                }
                // 연락처변경
                else if (ED_name.getText().toString() == null && ED_mobile.getText().toString() == null){
                    // 있으면
                    Call<ResponseBody> call = service.change_phone(user_id, ED_mobile.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                                     @Override
                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                         try {
                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<ResponseBody> call, Throwable t) {

                                     }
                                 }
                    );
                    // 뒤로가기
                    finish();
                }

                else if (ED_name.getText().toString() != null && ED_mobile.getText().toString() != null){

                    Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
                    call.enqueue(new Callback<ResponseBody>() {
                                     @Override
                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                         try {
                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<ResponseBody> call, Throwable t) {

                                         Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
                                     }
                                 }
                    );

                    Call<ResponseBody> call2 = service.change_phone(user_id, ED_mobile.getText().toString());
                    call2.enqueue(new Callback<ResponseBody>() {
                                     @Override
                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                         try {
                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<ResponseBody> call, Throwable t) {

                                     }
                                 }
                    );
                    // 뒤로가기
                    finish();

                }

                if (ED_name.getText().toString() == null && ED_name.getText().toString() == null){
                    Toast.makeText(AccountSettingActivity.this, "프로필 이름 또는 연락처를 변경해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
