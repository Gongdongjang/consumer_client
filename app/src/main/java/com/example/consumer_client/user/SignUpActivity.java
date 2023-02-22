package com.example.consumer_client.user;

import android.content.Intent;
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
import retrofit2.http.Body;
import retrofit2.http.POST;

interface SignUpService {
    @POST("signup")
    Call<ResponseBody> signUp(@Body JsonObject body);
}

public class SignUpActivity extends AppCompatActivity {

    String TAG = SignUpActivity.class.getSimpleName();

    SignUpService service;
    JsonParser jsonParser;

    private TextView id_verify_txt, pwd_verify_txt;
    private EditText id, password, pwConfirm;
    private Button id_verify_btn, signUpBtn;
    private String name, phone_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        id_verify_btn = findViewById(R.id.id_verify_button);
        id = findViewById(R.id.userId);
//        id_verify_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                idCheck(id.getText().toString());
//            }
//        });

        password = findViewById(R.id.password);
        pwConfirm = findViewById(R.id.pwConfirm);
        pwd_verify_txt = findViewById(R.id.pwd_check_state);
        pwConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "pwd test");
                if (password.getText().toString().equals(pwConfirm.getText().toString())) {
                    pwd_verify_txt.setText("O");
                } else {
                    pwd_verify_txt.setText("X");
                }
                Log.d(TAG, pwd_verify_txt.getText().toString());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pwd_verify_txt.getText().toString().equals("O")) {
                    tryRegister(id.getText().toString(), password.getText().toString());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }
//    void idCheck (String id){
//
//        JsonObject body = new JsonObject();
//        body.addProperty("id", id);
//
//        Call<ResponseBody> call = service.checkId(body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                        if (res.get("is_valid").getAsBoolean()) {
//                            id_verify_txt.setText("사용할 수 있는 아이디입니다.");
//                        } else {
//                            id_verify_txt.setText("사용할 수 없는 아이디입니다.");
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        Log.d(TAG, "Fail " + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e(TAG, "onFailure: e " + t.getMessage());
//            }
//        });
//    }

    private void tryRegister(String uid, String pwd) {

        id.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기
        password.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기

        boolean cancel = false;
        View focusView = null;

        // 아이디 빈칸 검사
        if (uid.isEmpty()) {
            id.setError("아이디를 입력해주세요.");
            focusView = id;
            cancel = true;
        }

        // 패스워드 빈칸 검사
        if (pwd.isEmpty()) {
            password.setError("비밀번호를 입력해주세요.");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent intent = getIntent();
            name = intent.getStringExtra("name");
            phone_number = intent.getStringExtra("phone_number");

            JsonObject body = new JsonObject();
            body.addProperty("phone_number", phone_number);
            body.addProperty("name", name);
            body.addProperty("id", uid);
            body.addProperty("password", pwd);

            Call<ResponseBody> call = service.signUp(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                            Log.d(TAG, res.get("id").getAsString());

                            //회원가입 버튼 클릭시, 로그인 페이지로 이동
                            Intent intent = new Intent(getApplicationContext(), IntegratedLoginActivity.class);
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
}
