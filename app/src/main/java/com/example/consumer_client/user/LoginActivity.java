package com.example.consumer_client.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
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
import com.example.consumer_client.TutorialActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Gender;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

interface LoginService {
    @POST("login")
    Call<ResponseBody> login(@Body JsonObject body);

    @POST("kakaoLogin")
    Call<ResponseBody> kakaoLogin(@Body JsonObject body);

    @POST("/googleLogin")
    Call<ResponseBody> userGoogleLogin(@Body JsonObject body);
}

public class LoginActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LoginService service = retrofit.create(LoginService.class);
    JsonParser jsonParser = new JsonParser();

    TextView sign;
    private static final String TAG="사용자";
    private TextView signup; //회원가입 창으로 가는 텍스트
    private Button loginbutton;
    private Button kakaobutton;
    private ProgressBar mProgressView;
    private EditText id;
    private EditText password;
    private Button registerButton;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton google_sign_in_button;
    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Log.d("GET_KEYHASH",getKeyHash()); //해시값 가져올때 사용

        //회원가입 버튼
        sign = findViewById(R.id.signin);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        kakaobutton= (Button) findViewById(R.id.kakaobutton);
        signup = findViewById(R.id.signin); //회원가입

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

        //기본로그인
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
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
        //네이버

        //간편로그인(카카오)
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                //String refresh_token="";
                if (oAuthToken != null) {
                    Log.i("user", oAuthToken.getAccessToken() + " " + oAuthToken.getRefreshToken());
                    //refresh_token=oAuthToken.getRefreshToken();

                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override public Unit invoke(User user, Throwable throwable) {

                            if (user != null) { // 유저 정보가 정상 전달 되었을 경우

                                String userid=Long.toString(user.getId());
                                String username=user.getKakaoAccount().getProfile().getNickname();
                                String nickname= user.getKakaoAccount().getProfile().getNickname();
                                String refresh_token= oAuthToken.getRefreshToken();
                                String gender= String.valueOf(user.getKakaoAccount().getGender()); //성별받기
                                //String email=user.getKakaoAccount().getEmail();

                                Log.i(TAG, "username " + username); // 유저의 고유 아이디를 불러옵니다.
                                Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname()); // 유저의 닉네임을 불러옵니다.
                                Log.i(TAG, "gender " + gender);

                                Log.d("id1",userid);
                                JsonObject body = new JsonObject();
                                body.addProperty("id", userid);
                                body.addProperty("username", username);
                                body.addProperty("nickname", nickname);
                                body.addProperty("sns_type", "kakao");
                                body.addProperty("refresh_token", refresh_token);
                                body.addProperty("gender", gender);

                                Call<ResponseBody> call = service.kakaoLogin(body);

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        Log.d("194행",response.toString());
                                        if (response.isSuccessful()) {
                                            try {
                                                JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
//                                                Log.d("217행",res.toString());
                                                Log.d("179행", res.get("id").getAsString());

                                                //로그인 버튼 클릭시, 메인 페이지로 이동 + 유저아이디 같이 넘기기
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.putExtra("kakaoid",res.get("id").getAsString());

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
                            if (throwable != null) { // 로그인 시 오류 났을 때
                                // 키해시가 등록 안 되어 있으면 오류 납니다.
                                Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                            }
                            return null;
                        }
                    });


                } if (throwable != null) { // TBD
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                //updateKakaoLoginUi(refresh_token);  //refresh_token 전달하는게 괜찮나..?
                return null;
            }
        };
        kakaobutton = findViewById(R.id.kakaobutton); //
        kakaobutton.setOnClickListener(new View.OnClickListener() {//로그인 버튼 클릭 시
            @Override public void onClick(View v) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    // 카카오톡이 있을 경우?
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                } else { //카카오톡 없으면 카카오계정으로
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                }
            }
        });

    }

    //기본 로그인
    void login() {
        id = (EditText) findViewById(R.id.editID);
        password = (EditText) findViewById(R.id.editPassword);
        JsonObject body = new JsonObject();
        body.addProperty("id", id.getText().toString());
        body.addProperty("password", password.getText().toString());

        Call<ResponseBody> call = service.login(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d("261", response.toString());
                if (response.isSuccessful()) {
                    try {
                        JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
                        String access_token = res.get("access_token").getAsString();
                        if (access_token.equals("id_false")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "아이디를 확인해주세요.", Toast.LENGTH_LONG);
                            toast.show();
                        } else if (access_token.equals("pwd_false")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            String refresh_token = res.get("refresh_token").getAsString();
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", access_token).apply();
                            editor.putString("refresh_token", refresh_token).apply();
                            //m으로
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("generalid",res.get("id").getAsString());
                            startActivity(intent);
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

    //카카오 키해시얻기
//    public String getKeyHash(){
//        try{
//            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            if(packageInfo == null) return null;
//            for(Signature signature: packageInfo.signatures){
//                try{
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    return android.util.Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//                }catch (NoSuchAlgorithmException e){
//                    Log.w("getKeyHash", "Unable to get MessageDigest. signature="+signature, e);
//                }
//            }
//        }catch(PackageManager.NameNotFoundException e){
//            Log.w("getPackageInfo", "Unable to getPackageInfo");
//        }
//        return null;
//    }

    private void googleSignIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Log.d("test","구글로그인확인" );
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //구글 로그인
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String familyName = account.getFamilyName();
            String givenName = account.getGivenName();
            String displayName = account.getDisplayName();
            String id = account.getId();
            String id_token = account.getIdToken();
            String server_auth_code = account.getServerAuthCode();

            JsonObject body = new JsonObject();
            body.addProperty("id", id);
            body.addProperty("username", familyName+givenName);
            body.addProperty("nickname", displayName);
//            body.addProperty("id_token", id_token);
//            body.addProperty("server_auth_code", server_auth_code);
            body.addProperty("sns_type", "google");


            Call<ResponseBody> call = service.userGoogleLogin(body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("test",response.toString());

                    if (response.isSuccessful()) {
                        try{
                            JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                            Toast.makeText(LoginActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                            //로그인 버튼 클릭시, 메인 페이지로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("googleid",res.get("id").getAsString());
                            startActivity(intent);
                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                    Log.e("로그인 에러 발생", t.getMessage());
                }
            });
        } catch (ApiException e) {
            Log.w("failed", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}