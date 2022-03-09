package com.example.consumer_client.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.user.data.GoogleLoginData;
import com.example.consumer_client.user.data.GoogleLoginResponse;
import com.example.consumer_client.user.data.LoginData;
import com.example.consumer_client.user.data.LoginResponse;
import com.example.consumer_client.user.network.RetrofitClient;
import com.example.consumer_client.user.network.ServiceApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView signup; //회원가입 창으로 가는 텍스트
    private Button loginbutton;
    private EditText id;
    private EditText password;
    private Button registerButton;
    private ServiceApi service;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_sign_in_button;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.editID);
        password = (EditText) findViewById(R.id.editPassword);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        signup = findViewById(R.id.signin); //회원가입
        service = RetrofitClient.getClient().create(ServiceApi.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in_button = findViewById(R.id.sign_in_button);
        google_sign_in_button.setSize(SignInButton.SIZE_STANDARD);

        //구글 로그인
        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

        //기본 로그인
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryLogin();
            }
        });

        //회원가입 텍스트 누르면 회원가입 창으로
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    //기본 로그인 유효성 검사
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
        }
    }

    //기본 로그인 시작하기
    private void startLogin(LoginData data) {
        service.userLogin(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse result = response.body();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

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
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void googleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);

            String email = account.getEmail();
            String familyName = account.getFamilyName();
            String givenName = account.getGivenName();
            String displayName = account.getDisplayName();
            String id = account.getId();

            Log.d("email", email);
            Log.d("familyName", familyName);
            Log.d("givenName", givenName);
            Log.d("displayName", displayName);
            Log.d("id", id);

            String name = familyName+givenName;

            GoogleLoginData googleD = new GoogleLoginData(id, name, displayName);

            service.userGoogleLogin(googleD).enqueue(new Callback<GoogleLoginResponse>() {
                @Override
                public void onResponse(Call<GoogleLoginResponse> call, Response<GoogleLoginResponse> response) {
                    GoogleLoginResponse result = response.body();
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();

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
                public void onFailure(Call<GoogleLoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                    Log.e("로그인 에러 발생", t.getMessage());
                }
            });
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
