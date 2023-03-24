package com.gdjang.consumer_client.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gdjang.consumer_client.MainActivity;
import com.gdjang.consumer_client.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
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
import java.util.Objects;

interface IntegratedLoginService {

    @POST("kakaoLogin/")
    Call<ResponseBody> kakaoLogin(@Body JsonObject body);

    @POST("googleLogin/")
    Call<ResponseBody> userGoogleLogin(@Body JsonObject body);
}

public class IntegratedLoginActivity extends AppCompatActivity {

    IntegratedLoginService service;
    JsonParser jsonParser;

    private static final String TAG="사용자";
    private ImageButton kakaobutton, google_sign_in_button;
    private GoogleSignInClient mGoogleSignInClient;
//    private SignInButton google_sign_in_button;
    private final int RC_SIGN_IN = 1;
    Button loginbutton, signupBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrated_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(IntegratedLoginService.class);
        jsonParser = new JsonParser();

        loginbutton = findViewById(R.id.loginbutton);
        kakaobutton= findViewById(R.id.kakaobutton);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in_button = findViewById(R.id.googleBtn);

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
            public void onClick(View v) {
                Intent intent = new Intent(IntegratedLoginActivity.this, StandardLoginActivity.class);
                startActivity(intent);
            }
        });

        signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntegratedLoginActivity.this, AccountInfoActivity.class);
                startActivity(intent);
            }
        });

        //이이디 비밀번호 찾기
        TextView find = (TextView) findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://open.kakao.com/o/suhyQ78e"));
                startActivity(intent);
            }
        });

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
                                //String nickname= user.getKakaoAccount().getProfile().getNickname();
                                String refresh_token= oAuthToken.getRefreshToken();
                                //String email=user.getKakaoAccount().getEmail();

                                Log.i(TAG, "username " + username); // 유저의 고유 아이디를 불러옵니다.

                                Log.d("id1",userid);
                                JsonObject body = new JsonObject();
                                body.addProperty("id", userid);
                                body.addProperty("username", username);
                                //body.addProperty("nickname", nickname);
                                body.addProperty("sns_type", "kakao");
                                body.addProperty("refresh_token", refresh_token);

                                Call<ResponseBody> call = service.kakaoLogin(body);

                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                        Log.d("194행",response.toString());
                                        if (response.isSuccessful()) {
                                            try {
                                                JsonObject res =  (JsonObject) jsonParser.parse(response.body().string());
//                                                Log.d("217행",res.toString());
                                                //Log.d("179행", res.get("id").getAsString());

                                                String first_login = res.get("first_login").getAsString();

                                                if(Objects.equals(first_login, "0")){ //최초 로그인 일 시 시작+튜토리얼화면
                                                    Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                                    intent.putExtra("sns_login","yes");
                                                    intent.putExtra("name",username);
                                                    intent.putExtra("user_id",res.get("id").getAsString());
                                                    startActivity(intent);
                                                } else{
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    intent.putExtra("user_id",res.get("id").getAsString());
                                                    startActivity(intent);
                                                }

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
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(IntegratedLoginActivity.this)) {
                    // 카카오톡이 있을 경우?
                    UserApiClient.getInstance().loginWithKakaoTalk(IntegratedLoginActivity.this, callback);
                } else { //카카오톡 없으면 카카오계정으로
                    UserApiClient.getInstance().loginWithKakaoAccount(IntegratedLoginActivity.this, callback);
                }
            }
        });

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
                            Toast.makeText(IntegratedLoginActivity.this, res.get("message").getAsString(), Toast.LENGTH_SHORT).show();

                            String first_login = res.get("first_login").getAsString();

                            if(Objects.equals(first_login, "0")){ //최초 로그인 일 시 시작+튜토리얼화면
                                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                intent.putExtra("sns_login","yes");
                                intent.putExtra("name",familyName + givenName);
                                intent.putExtra("user_id",res.get("id").getAsString());
                                startActivity(intent);
                            } else{
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("user_id",res.get("id").getAsString());
                                startActivity(intent);
                            }

                        }
                        catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(IntegratedLoginActivity.this, "로그인 에러 발생", Toast.LENGTH_SHORT).show();
                    Log.e("로그인 에러 발생", t.getMessage());
                }
            });
        } catch (ApiException e) {
            Log.w("failed", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}