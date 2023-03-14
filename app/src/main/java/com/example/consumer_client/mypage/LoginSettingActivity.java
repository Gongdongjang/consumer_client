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

interface ChangeLoginService{
    @GET("/change_pw")
    Call<ResponseBody> change_pw(@Query("user_id") String user_id, @Query("password") String password);
}

public class LoginSettingActivity extends AppCompatActivity {
    public static Context mContext;
    public String user_id;
    private MyPage myPage;
    LoginSettingDialog loginSettingDialog;
    LinearLayout linearLayout;
    EditText ED_pw;
    String password;

    ChangeLoginService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray change_pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit_logininfo);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ChangeLoginService.class);
        jsonParser = new JsonParser();

        Button cancelBtn = findViewById(R.id.CancelBtn_MP);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSettingDialog = new LoginSettingDialog(mContext, user_id);
                loginSettingDialog.show();
            }
        });

        ED_pw = findViewById(R.id.EditPasswordConfirm_MP);
        password = ED_pw.getText().toString();

        Button changeBtn = findViewById(R.id.ConfirmBtn_MP);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 변경 프로세스
                if (ED_pw.getText().toString() != null){
                    Call<ResponseBody> call = service.change_pw(user_id, ED_pw.getText().toString());
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
                else{
                    Toast.makeText(LoginSettingActivity.this, "아이디, 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
