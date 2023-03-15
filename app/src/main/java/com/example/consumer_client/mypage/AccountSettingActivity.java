package com.example.consumer_client.mypage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface ChangeAccountService{
    @GET("/change_name")
    Call<ResponseBody> change_name(@Query("user_id") String user_id, @Query("user_name") String user_name);
    @GET("/change_phone")
    Call<ResponseBody> change_phone(@Query("user_id") String user_id, @Query("mobile_no") String mobile_no);

    @POST("signup/phone-check")
    Call<ResponseBody> checkPhone(@Body JsonObject body);
    @POST("signup/phone-check/verify")
    Call<ResponseBody> phoneVerify(@Body JsonObject body);
}

public class AccountSettingActivity extends AppCompatActivity {
    public static Context mContext;
    public String user_id;
    AccountSettingDialog accountSettingDialog;
    EditText ED_name, ED_mobile, code_verify_input;
    String user_name;
    Button phone_verify_btn;

    ChangeAccountService service;
    JsonParser jsonParser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_edit_userinfo);

        mContext = this;

        Intent intent = getIntent(); //intent 값 받기
        user_id = intent.getStringExtra("user_id");
        Log.d("user_id ",user_id);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseurl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ChangeAccountService.class);
        jsonParser = new JsonParser();

        Button cancelBtn = findViewById(R.id.CancelBtn_MP);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountSettingDialog = new AccountSettingDialog(mContext, user_id);
                accountSettingDialog.show();
            }
        });

        ED_name = findViewById(R.id.EditProfileName_MP);
        user_name = ED_name.getText().toString();
        ED_mobile = findViewById(R.id.EditPhone_MP);

        phone_verify_btn = findViewById(R.id.PhoneCheckBtn);
        code_verify_input = findViewById(R.id.inputNum2);
        phone_verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneCheck(ED_mobile.getText().toString());
            }
        });


        Button changeBtn = findViewById(R.id.ConfirmBtn_MP);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneVerify(code_verify_input.getText().toString(), ED_mobile.getText().toString());
                // 변경 프로세스
                // 이름 변경
//                if (!ED_name.getText().toString().equals("") && ED_mobile.getText().toString().equals("")){
//                    Log.d("???????? >>", ED_name.getText().toString());
//                    Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
//                    call.enqueue(new Callback<ResponseBody>() {
//                                     @Override
//                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                         try {
//                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                                         } catch (IOException e) {
//                                             e.printStackTrace();
//                                         }
//                                     }
//
//                                     @Override
//                                     public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                         Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
//                                     }
//                                 }
//                    );
//                    // 뒤로가기
//                    finish();
//                }
//                // 연락처변경
//                else if (ED_name.getText().toString().equals("") && !ED_mobile.getText().toString().equals("")){
//                    // 있으면
//                    Call<ResponseBody> call = service.change_phone(user_id, ED_mobile.getText().toString());
//                    call.enqueue(new Callback<ResponseBody>() {
//                                     @Override
//                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                         try {
//                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                                         } catch (IOException e) {
//                                             e.printStackTrace();
//                                         }
//                                     }
//
//                                     @Override
//                                     public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                     }
//                                 }
//                    );
//                    // 뒤로가기
//                    finish();
//                }
//
//                // 둘 다 변경
//                else if (!ED_name.getText().toString().equals("") && !ED_mobile.getText().toString().equals("")){
//
//                    Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
//                    call.enqueue(new Callback<ResponseBody>() {
//                                     @Override
//                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                         try {
//                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                                         } catch (IOException e) {
//                                             e.printStackTrace();
//                                         }
//                                     }
//
//                                     @Override
//                                     public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                         Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
//                                     }
//                                 }
//                    );
//
//                    Call<ResponseBody> call2 = service.change_phone(user_id, ED_mobile.getText().toString());
//                    call2.enqueue(new Callback<ResponseBody>() {
//                                     @Override
//                                     public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                         try {
//                                             JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
//                                         } catch (IOException e) {
//                                             e.printStackTrace();
//                                         }
//                                     }
//
//                                     @Override
//                                     public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                                     }
//                                 }
//                    );
//                    // 뒤로가기
//                    finish();
//
//                }
//
//                else if (ED_name.getText().toString().equals("") && ED_mobile.getText().toString().equals("")){
//                    Toast.makeText(AccountSettingActivity.this, "프로필 이름 또는 연락처를 변경해주세요.", Toast.LENGTH_SHORT).show();
//                }
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
                        JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                        Log.d("회원정보변경", res.get("msg").getAsString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d("회원정보변경", "Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("회원정보변경", "onFailure: e " + t.getMessage());
            }
        });
    }

    void phoneVerify(String code, String phone_number) {
        JsonObject body = new JsonObject();
        body.addProperty("phone_number", phone_number);
        body.addProperty("code", code);

        Call<ResponseBody> call = service.phoneVerify(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JsonObject res = (JsonObject) jsonParser.parse(response.body().string());
                    if (res.get("phone_valid").getAsString().equals("true")) {
                        change();

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "인증 번호 및 전화번호를 확인해주세요.", Toast.LENGTH_LONG);
                        toast.show();
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

    void change(){
        if (!ED_name.getText().toString().equals("") && ED_mobile.getText().toString().equals("")){
            Log.d("???????? >>", ED_name.getText().toString());
            Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
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

                                 Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
                             }
                         }
            );
            // 뒤로가기
            finish();
        }
        // 연락처변경
        else if (ED_name.getText().toString().equals("") && !ED_mobile.getText().toString().equals("")){
            // 있으면
            Call<ResponseBody> call = service.change_phone(user_id, ED_mobile.getText().toString());
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
                         }
            );
            // 뒤로가기
            finish();
        }

        // 둘 다 변경
        else if (!ED_name.getText().toString().equals("") && !ED_mobile.getText().toString().equals("")){

            Call<ResponseBody> call = service.change_name(user_id, ED_name.getText().toString());
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

                                 Log.d("D_name.getText().toString() >>", ED_name.getText().toString());
                             }
                         }
            );

            Call<ResponseBody> call2 = service.change_phone(user_id, ED_mobile.getText().toString());
            call2.enqueue(new Callback<ResponseBody>() {
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
                          }
            );
            // 뒤로가기
            finish();

        }

        else if (ED_name.getText().toString().equals("") && ED_mobile.getText().toString().equals("")){
            Toast.makeText(AccountSettingActivity.this, "프로필 이름 또는 연락처를 변경해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
