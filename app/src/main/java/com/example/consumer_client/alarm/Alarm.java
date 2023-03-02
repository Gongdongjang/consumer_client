package com.example.consumer_client.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.address.FindTownActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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

interface FcmService {
    @POST("alarm_token")
    Call<ResponseBody> fcmTokenRegister(@Body JsonObject body);
}

public class Alarm extends AppCompatActivity {

    FcmService service;
    JsonParser jsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FcmService.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_town);

        //토픽 구독
        FirebaseMessaging.getInstance().subscribeToTopic("userTopic");

        Intent intent = getIntent(); //intent 값 받기
        String userid=intent.getStringExtra("user_id");

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Fetching FCm registration token filed", task.getException());
                            return;
                        }
                        String msg = task.getResult();
                        Log.d("token", msg);

                        registerFcmToken(userid, msg);
                        JsonObject body = new JsonObject();
                        body.addProperty("id", userid);
                    }
                });
        //String token = FirebaseMessaging.getInstance().getToken().getResult();
    }

    private void registerFcmToken(String userid, String token) {
        JsonObject body = new JsonObject();
        body.addProperty("id", userid);
        body.addProperty("token", token);

        Call<ResponseBody> call = service.fcmTokenRegister(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        //메인 페이지로 이동
                        Intent intent = new Intent(Alarm.this, MainActivity.class);
                        intent.putExtra("user_id",userid);
                        startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Alarm.this, "FCM 토큰 등록 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("FCM 토큰등록 에러", t.getMessage());
            }
        });
    }

}
