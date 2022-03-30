package com.example.consumer_client.user.network;

import com.example.consumer_client.user.data.KakaoLoginData;
import com.example.consumer_client.user.data.KakaoLoginResponse;
import com.example.consumer_client.user.data.GoogleLoginData;
import com.example.consumer_client.user.data.GoogleLoginResponse;
import com.example.consumer_client.user.data.LoginData;
import com.example.consumer_client.user.data.LoginResponse;
import com.example.consumer_client.user.data.RegisterData;
import com.example.consumer_client.user.data.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST("/login")
    Call<LoginResponse> userLogin(@Body LoginData data);

    @POST("/signup")
    Call<RegisterResponse> userRegister(@Body RegisterData data);

    @POST("/kakaoLogin")
    Call<KakaoLoginResponse> userKakaoLogin(@Body KakaoLoginData data);

    @POST("/googleLogin")
    Call<GoogleLoginResponse> userGoogleLogin(@Body GoogleLoginData data);
}
