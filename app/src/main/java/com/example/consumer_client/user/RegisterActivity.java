package com.example.consumer_client.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.http.POST;

interface SignUpService {
    @POST("signup/id-check")
    Call<ResponseBody> checkId(@Body JsonObject body);

    @POST("signup/phone-check")
    Call<ResponseBody> checkPhone(@Body JsonObject body);

    @POST("signup/phone-check/verify")
    Call<ResponseBody> phoneVerify(@Body JsonObject body);

    @POST("signup")
    Call<ResponseBody> signUp(@Body JsonObject body);

}

public class RegisterActivity extends AppCompatActivity {
    String TAG = RegisterActivity.class.getSimpleName();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.35.84:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    SignUpService service = retrofit.create(SignUpService.class);
    JsonParser jsonParser = new JsonParser();

    private TextView code_verify_txt, id_verify_txt, pwd_verify_txt;
    private EditText id, code_verify_input, password, pwConfirm, name, mobile_no;
    private RadioButton male, female, infoOk, pushOk;
    private Button registerButton, phone_verify_btn, code_verify_btn, id_verify_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.signName);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        phone_verify_btn = findViewById(R.id.mobileAuth);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        registerButton = findViewById(R.id.signupbutton);
        infoOk = (RadioButton) findViewById(R.id.infoOk);
        pushOk = (RadioButton) findViewById(R.id.pushOk);

        phone_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCheck(mobile_no.getText().toString());
            }
        });

        code_verify_btn = findViewById(R.id.code_verify_button);
        code_verify_input = findViewById(R.id.code_verify_input);
        code_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneVerify(code_verify_input.getText().toString(), mobile_no.getText().toString());
            }
        });

        id_verify_btn = findViewById(R.id.id_verify_button);
        id = findViewById(R.id.userId);
        id_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCheck(id.getText().toString());
            }
        });

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code_verify_txt = findViewById(R.id.code_verify_txt);
                id_verify_txt = findViewById(R.id.id_verify_txt);

                if (code_verify_txt.getText().equals("인증됐습니다.") && id_verify_txt.getText().equals("사용할 수 있는 아이디입니다.")) {
                    if (pwd_verify_txt.getText().toString().equals("O")) {
                        tryRegister(id.getText().toString(), password.getText().toString());
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "아이디와 전화번호를 확인해주세요.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    void phoneCheck(String phone_number) {
        JsonObject body = new JsonObject();
        body.addProperty("phone_number", phone_number);
        Call<ResponseBody> call = service.checkPhone(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("response", response.toString());
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        Log.d(TAG, res.get("msg").getAsString());
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

    void phoneVerify(String code, String phone_number) {
        code_verify_txt = findViewById(R.id.code_verify_txt);

        JsonObject body = new JsonObject();
        body.addProperty("phone_number", phone_number);
        body.addProperty("code", code);

        Call<ResponseBody> call = service.phoneVerify(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                    if (res.get("phone_valid").getAsBoolean()) {
                        code_verify_txt.setText("인증됐습니다.");
                    } else {
                        code_verify_txt.setText("다시 시도해주세요.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void idCheck(String id) {
        id_verify_txt = findViewById(R.id.id_verify_txt);

        JsonObject body = new JsonObject();
        body.addProperty("id", id);

        Call<ResponseBody> call = service.checkId(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        if (res.get("is_valid").getAsBoolean()) {
                            id_verify_txt.setText("사용할 수 있는 아이디입니다.");
                        } else {
                            id_verify_txt.setText("사용할 수 없는 아이디입니다.");
                        }
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

    private void tryRegister(String uid, String pwd) {
        name.setError(null); //오류 있으면 or 비어있으면 멘트 띄우는 거 일단 null로
        mobile_no.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기
        id.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기
        password.setError(null); //오류 있으면 or 비어있으면 멘트 띄우기

        String uname = name.getText().toString();
        String umobile_no = mobile_no.getText().toString();

        String gender = "";
        if(male.isChecked()){
            gender = "남";
        }
        else if(female.isChecked()){
            gender = "여";
        }

        boolean cancel = false;
        View focusView = null;

        // 이름 빈칸 검사
        if (uname.isEmpty()) {
            name.setError("이름을 입력해주세요.");
            focusView = name;
            cancel = true;
        }

        //전화번호 빈칸 검사
        if (umobile_no.isEmpty()) {
            mobile_no.setError("전화번호를 입력해주세요.");
            focusView = mobile_no;
            cancel = true;
        }

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

        //개인 정보 수집 동의 if 동의x 시 체크해달라고 표시하기기
        if(!(infoOk.isChecked())){
            Toast.makeText(RegisterActivity.this, "개인정보수집에 동의해주세요", Toast.LENGTH_SHORT).show();
            cancel = true;
            focusView = infoOk;
        }

        // 푸쉬 알림 동의 -> 일단은 1이나 0이나 상관없이 그냥 저장되게 만듦 즉, 체크하지 않아도 넘어가게 함
        int push_allow;
        if(pushOk.isChecked()){
            push_allow = 1;
        }
        else{
            push_allow = 0;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            JsonObject body = new JsonObject();
            body.addProperty("id", uid);
            body.addProperty("password", pwd);
            body.addProperty("nickname", uid);
            body.addProperty("phone_number", umobile_no);
            body.addProperty("gender", gender);
            body.addProperty("name", uname);
            body.addProperty("push_allow", push_allow);

            Call<ResponseBody> call = service.signUp(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                            Log.d(TAG, res.get("id").getAsString());

                            //회원가입 버튼 클릭시, 회원가입 완료 페이지로 이동
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
            });        }
    }

}
