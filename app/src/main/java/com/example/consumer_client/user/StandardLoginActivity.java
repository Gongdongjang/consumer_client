package com.example.consumer_client.user;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.tutorial.TutorialActivity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Objects;

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

interface LoginService {
    @POST("login")
    Call<ResponseBody> login(@Body JsonObject body);
    @POST("postAutoLogin")
    Call<ResponseBody> postAutoLogin(@Body JsonObject body);
}

public class StandardLoginActivity extends AppCompatActivity {
    LoginService service;
    JsonParser jsonParser;
    private TextView find; //회원가입 창으로 가는 텍스트
    private EditText id, password;
    private Button loginbutton;
    private static final String TAG="사용자";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(LoginService.class);
        jsonParser = new JsonParser();

        loginbutton = findViewById(R.id.loginbutton);

        //기본로그인
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                tryLogin();
            }
        });
    }
    //기본 로그인
    void login() {
        id = (EditText) findViewById(R.id.inputId);
        password = (EditText) findViewById(R.id.inputPw);
        JsonObject body = new JsonObject();
        body.addProperty("id", id.getText().toString());
        body.addProperty("password", password.getText().toString());

        Call<ResponseBody> call = service.login(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("81", response.toString());
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        String access_token = res.get("access_token").getAsString();
                        if (access_token.equals("id_false")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "아이디를 확인해주세요.", Toast.LENGTH_LONG);
                            Log.d(TAG, "아이티틀림");
                            toast.show();
                        } else if (access_token.equals("pwd_false")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG);
                            toast.show();
                            Log.d(TAG, "비밀번호틀림");
                        } else {
                            String first_login = res.get("first_login").getAsString();
                            String refresh_token = res.get("refresh_token").getAsString();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", access_token).apply();
                            editor.putString("refresh_token", refresh_token).apply();

                            CheckBox AutoLoginBtn = findViewById(R.id.AutoLoginBtn);
                            if(AutoLoginBtn.isChecked()){
                                JsonObject body = new JsonObject();
                                body.addProperty("user_id", id.getText().toString());
                                Call<ResponseBody> call1 = service.postAutoLogin(body);
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        JsonObject res = (JsonObject) jsonParser.parse(response.body().toString());
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e(TAG, "onFailure: e " + t.getMessage());
                                    }
                                });
                            }

                            if(Objects.equals(first_login, "0")){ //최초 로그인 일 시 튜토리얼로
                                Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                                intent.putExtra("user_id",res.get("id").getAsString());
                                startActivity(intent);
                            } else{
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user_id",res.get("id").getAsString());
                                startActivity(intent);
                            }

                        }
                        Log.d(TAG, res.get("access_token").getAsString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
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

    //함수정리
    private void tryLogin() {
        id.setError(null);
        password.setError(null);

        String uid = id.getText().toString();
        String upw = password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 아이디의 유효성 검사
        if (uid.isEmpty()) {
            id.setError("아이디를 입력해주세요.");
            focusView = id;
            cancel = true;
        }

        // 패스워드의 유효성 검사
        if (upw.isEmpty()) {
            password.setError("비밀번호를 입력해주세요.");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //startLogin(new LoginData(uid, upw));
        }
    }

}