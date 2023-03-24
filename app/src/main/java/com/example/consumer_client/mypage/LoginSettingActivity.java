package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.cart.CartListActivity;
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
    LoginSettingDialog loginSettingDialog;
    EditText id, password, pwConfirm;

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

        id = (EditText) findViewById(R.id.EditID_MP);
        password = findViewById(R.id.EditPassword_MP);
        pwConfirm = findViewById(R.id.EditPasswordConfirm_MP);

        Button changeBtn = findViewById(R.id.ConfirmBtn_MP);

        //상단바 뒤로가기
        ImageView gotoBack = findViewById(R.id.gotoBack);
        gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSettingActivity.this, MainActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });
        //상단바 장바구니
        ImageView gotoCart = findViewById(R.id.gotoCart);
        gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginSettingActivity.this, CartListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });


        // 비밀번호 확인
        TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //EditText 변경 전 발생할 이벤트

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //텍스트가 변경될 때마다 발생할 이벤트
                password.setTextColor(Color.parseColor("#1EAA95"));
            }
        };

        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //EditText 변경 전 발생할 이벤트

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //텍스트가 변경될 때마다 발생할 이벤트
                if (password.getText().toString().equals(pwConfirm.getText().toString())) {
                    pwConfirm.setTextColor(Color.parseColor("#1EAA95"));
                    changeBtn.setEnabled(true);
                } else {
                    pwConfirm.setTextColor(Color.parseColor("#F75D39"));
                    changeBtn.setEnabled(false);
                }
            }
        };

        password.addTextChangedListener(watcher1);
        pwConfirm.addTextChangedListener(watcher2);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id.getText().toString().equals("")){
                    Toast.makeText(LoginSettingActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                if (!id.getText().toString().equals(user_id)){
                    Toast.makeText(LoginSettingActivity.this, "아이디가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                    changeBtn.setEnabled(false);
                }
                if (!changeBtn.isEnabled()){
                    Toast.makeText(LoginSettingActivity.this, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 변경 프로세스
                    if (pwConfirm.getText().toString() != null){
                        Call<ResponseBody> call = service.change_pw(user_id, pwConfirm.getText().toString());
                        call.enqueue(new Callback<ResponseBody>() {
                                         @Override
                                         public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                             try {
                                                 JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                                 String message = res.get("message").getAsString();
                                                 if (message.equals("id_not_exist")){
                                                     Toast.makeText(LoginSettingActivity.this, "아이디가 틀렸습니다.", Toast.LENGTH_SHORT).show();
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
                        // 뒤로가기
                        finish();
                    }
                    else{
                        Toast.makeText(LoginSettingActivity.this, "아이디, 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
