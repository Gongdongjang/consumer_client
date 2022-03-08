package com.example.consumer_client.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.user.data.LoginData;
import com.example.consumer_client.user.data.LoginResponse;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView signup; //회원가입 창으로 가는 텍스트
    private Button loginbutton;
    private ProgressBar mProgressView;
    private EditText id;
    private EditText password;
    private Button registerButton;
    private ServiceApi service;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.editID);
        password = (EditText) findViewById(R.id.editPassword);
        loginbutton = (Button) findViewById(R.id.loginbutton);
//        mProgressView = (ProgressBar) findViewById(R.id.login_progress); //이거 왜 필요한지 모르겠음 == 회원가입도
        signup = findViewById(R.id.signin); //회원가입
        service = RetrofitClient.getClient().create(ServiceApi.class);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
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
        } else if (!isPasswordValid(upw)) {
            password.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startLogin(new LoginData(uid, upw));
//            showProgress(true);
        }
    }

    private void startLogin(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
//                showProgress(false);

                if (result.getCode() == 200) {
                    //로그인 버튼 클릭시, 메인 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    //같은 화면 다시 띄우기

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("로그인 에러 발생", t.getMessage());
//                showProgress(false);
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

//    private void showProgress(boolean show) {
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
}
