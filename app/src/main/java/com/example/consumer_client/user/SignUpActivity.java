package com.example.consumer_client.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface SignUpService {
    @POST("signup")
    Call<ResponseBody> signUp(@Body JsonObject body);

    @GET("is_id_dup")
    Call<ResponseBody> is_id_dup(@Query("id") String id);
}

public class SignUpActivity extends AppCompatActivity {

    String TAG = SignUpActivity.class.getSimpleName();

    SignUpService service;
    JsonParser jsonParser;

    private EditText id, password, pwConfirm;
    private Button signUpBtn, ID_Dup_Check;
    private String name, phone_number;
    private Boolean is_id_dup = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(SignUpService.class);
        jsonParser = new JsonParser();

        id = findViewById(R.id.userId);
        password = findViewById(R.id.password);
        pwConfirm = findViewById(R.id.pwConfirm);
        signUpBtn = findViewById(R.id.startGdjangBtn);
        ID_Dup_Check = findViewById(R.id.ID_Dup_Check);

        ID_Dup_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<ResponseBody> call = service.is_id_dup(id.getText().toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {

                                JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                                String message = res.get("message").getAsString();
                                if (message.equals("id_dup")) {
                                    is_id_dup = true;
                                } else {
                                    is_id_dup = false;
                                }
                                if (is_id_dup) {
                                    Toast.makeText(SignUpActivity.this, "ID가 중복입니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        //비밀번호 칸 입력 시 발생할 이벤트
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
                //텍스트가 변경 후 발생할 이벤트
                password.setTextColor(Color.parseColor("#1EAA95"));
            }
        };

        //비밀번호 확인 칸 입력 시 발생할 이벤트
        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //EditText 변경 전 발생할 이벤트

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트
                if (charSequence.length() > 0) {
                    //하나라도 작성했다면
                    signUpBtn.setClickable(true);
                    signUpBtn.setBackgroundResource(R.drawable.button_round2);
                    signUpBtn.setTextColor(0xFFFFFFFF);
                }
                else{
                    //한글자도 작성되지 않았다면
                    signUpBtn.setBackgroundResource(R.drawable.button_enable_false);
                    signUpBtn.setTextColor(0xFFBEBEBE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    //하나라도 작성했다면
                    signUpBtn.setClickable(true);
                    signUpBtn.setBackgroundResource(R.drawable.button_round2);
                    signUpBtn.setTextColor(0xFFFFFFFF);
                }
                //id가 중복이 아니면
                if (!is_id_dup) {
                    //텍스트가 변경될 때마다 발생할 이벤트
                    if (password.getText().toString().equals(pwConfirm.getText().toString())) {
                        pwConfirm.setTextColor(Color.parseColor("#1EAA95"));
                        signUpBtn.setEnabled(true);
                    } else {
                        pwConfirm.setTextColor(Color.parseColor("#F75D39"));
                        signUpBtn.setEnabled(false);
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "ID 중복을 확인해주세요.", Toast.LENGTH_LONG).show();
                    //텍스트가 변경될 때마다 발생할 이벤트
                    if (password.getText().toString().equals(pwConfirm.getText().toString())) {
                        pwConfirm.setTextColor(Color.parseColor("#1EAA95"));
                        signUpBtn.setEnabled(true);
                    } else {
                        pwConfirm.setTextColor(Color.parseColor("#F75D39"));
                        signUpBtn.setEnabled(false);
                    }
                }
            }
        };

        password.addTextChangedListener(watcher1);
        pwConfirm.addTextChangedListener(watcher2);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(id.getText().toString(), password.getText().toString());
            }
        });
    }

    private void register(String uid, String pwd) {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone_number = intent.getStringExtra("phone_number");

        JsonObject body = new JsonObject();
        body.addProperty("phone_number", phone_number);
        body.addProperty("name", name);
        body.addProperty("id", uid);
        body.addProperty("password", pwd);
        Log.d("body_check", body.toString());

        Call<ResponseBody> call = service.signUp(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        Toast.makeText(SignUpActivity.this, res.get("message").getAsString(), Toast.LENGTH_LONG).show();
                        //회원가입 버튼 클릭시, 회원가입 완료 페이지로 이동
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: e " + t.getMessage());
            }
        });
    }
}
