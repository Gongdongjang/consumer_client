package com.example.consumer_client.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.R;
import com.example.consumer_client.user.data.RegisterData;
import com.example.consumer_client.user.data.RegisterResponse;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText name;
    private EditText mobile_no;
    private String nickname;
    private EditText id;
    private EditText password;
    private EditText password_confirm;
    private RadioButton male;
    private RadioButton female;
    private Button registerButton;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.signName);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        id = (EditText) findViewById(R.id.userId);
        password = (EditText) findViewById(R.id.password);
        password_confirm = (EditText) findViewById(R.id.pwConfirm);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        registerButton = findViewById(R.id.signupbutton);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRegister();
            }
        });
    }

    private void tryRegister() {
        name.setError(null); //오류 있으면 or 비어있으면 멘트 띄우는 거 일단 null로
        mobile_no.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기
        id.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기
        password.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기

        String uname = name.getText().toString();
        String umobile_no = mobile_no.getText().toString();
        String uid = id.getText().toString();
        String upassword = password.getText().toString();
        String upassword_confirm = password_confirm.getText().toString();
        nickname = uid;
        String gender = "";
        if(male.isChecked()){
            gender = "남";
        }
        else if(female.isChecked()){
            gender = "여";
        }

        boolean cancel = false; //얘넨 뭐지? 알아내면 주석 지우기
        View focusView = null; //비어있는 칸에 대한 오류가 떴을 때 그 칸 강조..?

        // 이름의 유효성 검사
        if (uname.isEmpty()) {
            name.setError("이름을 입력해주세요.");
            focusView = name;
            cancel = true;
        }

        //전화번호의 유효성 검사
        //일단 넘겨..

        // 아이디의 유효성 검사
        if (uid.isEmpty()) {
            id.setError("아이디를 입력해주세요.");
            focusView = id;
            cancel = true;
        }

        // 패스워드의 유효성 검사
        if (upassword.isEmpty()) {
            password.setError("비밀번호를 입력해주세요.");
            focusView = password;
            cancel = true;
        } else if (!isPasswordValid(upassword)) {
            password.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = password;
            cancel = true;
        }
//        else if(upassword != upassword_confirm){
//            password.setError("비밀번호가 다릅니다. 다시 입력해주세요.");
//            focusView = password;
//            cancel = true;
//        } -> 왜 그런지 모르겠지만 자꾸 비밀번호 동일하게 쳤는데도 안되는 오류..

        if (cancel) {
            focusView.requestFocus();
        } else {
            startRegister(new RegisterData(uid, upassword, uname, umobile_no, nickname, gender));
        }
    }

    private void startRegister(RegisterData data) {
        service.userRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();
                Toast.makeText(RegisterActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getCode() == 200) {
                    //회원가입 버튼 클릭시, 회원가입 완료 페이지로 이동
                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }

// 비밀번호 조건
    private boolean isPasswordValid(String password) {
        return password.length() >= 6; //비밀번호 길이 6자리 이상
    }
}
