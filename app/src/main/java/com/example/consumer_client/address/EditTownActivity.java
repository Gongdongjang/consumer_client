package com.example.consumer_client.address;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.consumer_client.MainActivity;
import com.example.consumer_client.R;
import com.example.consumer_client.fragment.Home;
import com.example.consumer_client.network.NetworkStatus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface EditTownService {
    @POST("get_address")
    Call<ResponseBody> addressInfo(@Body JsonObject body);  //post user_id

    @POST("standard_address/register")
    Call<ResponseBody> postStdAddress(@Body JsonObject body);  //post user_id,standard_address
}

public class EditTownActivity extends AppCompatActivity {

    EditTownService service;
    JsonParser jsonParser;
    JsonObject res;
    JsonArray addressArray;

    // 주소 요청코드 상수 requestCode
    //private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    TextView txt_address0, txt_address1,txt_address2,txt_address3;
    String standard_address;    //홈화면으로 intent 넘기기

    //주소1,2,3 위도경도 list
    //List<String> addresslist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(EditTownService.class);
        jsonParser = new JsonParser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_town);

        Intent intent = getIntent(); //intent 값 받기
        String userid = intent.getStringExtra("user_id");

        //Log.d("68행_userid", userid);

//        Button btn_addAdress = findViewById(R.id.btn_addAdress);
//        Button btn_finish_address = findViewById(R.id.btn_finish_address);

        txt_address0 = findViewById(R.id.txt_address0);  //현재위치
        txt_address1 = findViewById(R.id.txt_address1);
        txt_address2 = findViewById(R.id.txt_address2);
        txt_address3 = findViewById(R.id.txt_address3);

        JsonObject body = new JsonObject();
        body.addProperty("id", userid);

        Call<ResponseBody> call = service.addressInfo(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    res = (JsonObject) jsonParser.parse(response.body().string());  //json응답
                    addressArray = res.get("address_result").getAsJsonArray();  //json배열
                    int address_count = res.get("address_count").getAsInt();
                    Log.d("근처동네", String.valueOf(address_count));

                    //사용자가 등록한 주소 불러오기
                    if (address_count == 1) {
                        String address_loc1 = addressArray.get(0).getAsJsonObject().get("loc1").getAsString();
                        txt_address1.setText(address_loc1);
                    } else if (address_count == 2) {
                        String address_loc1 = addressArray.get(0).getAsJsonObject().get("loc1").getAsString();
                        String address_loc2 = addressArray.get(0).getAsJsonObject().get("loc2").getAsString();
                        txt_address1.setText(address_loc1);
                        txt_address2.setText(address_loc2);
                    } else if (address_count == 3) {
                        String address_loc1 = addressArray.get(0).getAsJsonObject().get("loc1").getAsString();
                        String address_loc2 = addressArray.get(0).getAsJsonObject().get("loc2").getAsString();
                        String address_loc3 = addressArray.get(0).getAsJsonObject().get("loc3").getAsString();
                        txt_address1.setText(address_loc1);
                        txt_address2.setText(address_loc2);
                        txt_address3.setText(address_loc3);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditTownActivity.this, "주소정보 받아오기 오류 발생", Toast.LENGTH_SHORT).show();
                Log.e("주소정보", t.getMessage());
            }
        });

        //Activity->fragment로 데이터 보내기 intent X

        //기준 변경점 선택하면 바로 intent로 홈화면 가는것으로 구현
        txt_address0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard_address = "현재위치";
                postStdAddress(userid, standard_address);
                Intent intent = new Intent(EditTownActivity.this, MainActivity.class);
                intent.putExtra("user_id", userid); //MainActivity로 갈떄 userid가 없으면 오류남
                startActivity(intent);
            }
        });

        //주소1
        txt_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard_address = txt_address1.getText().toString();
                if (!standard_address.equals("주소1")) {   //주소가 있어야 클릭 가능하고 홈화면으로 전환할 수 있게
                    postStdAddress(userid, standard_address);
                    Intent intent = new Intent(EditTownActivity.this, MainActivity.class);
                    intent.putExtra("user_id", userid);
                    startActivity(intent);
                }
            }
        });
        //주소2
        txt_address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard_address = txt_address2.getText().toString();
                if (!standard_address.equals("주소2")) {
                    postStdAddress(userid, standard_address);
                    Intent intent = new Intent(EditTownActivity.this, MainActivity.class);
                    intent.putExtra("user_id", userid);
                    startActivity(intent);
                }
            }
        });
        //주소3
        txt_address3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standard_address = txt_address3.getText().toString();
                if (!standard_address.equals("주소3")) {
                    postStdAddress(userid, standard_address);
                    Intent intent = new Intent(EditTownActivity.this, MainActivity.class);
                    intent.putExtra("user_id", userid);
                    startActivity(intent);
                }
            }
        });

    }

    //기준주소 등록하기
    void postStdAddress(String user_id, String address){
        JsonObject body = new JsonObject();
        body.addProperty("id", user_id);
        body.addProperty("standard_address", address);  //기준 주소

        Call<ResponseBody> call = service.postStdAddress(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

