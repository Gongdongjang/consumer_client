package com.example.consumer_client.user.network;
import com.example.consumer_client.address.AddressData;
import com.example.consumer_client.FarmDetailData;
import com.example.consumer_client.FarmDetailResponse;
import com.example.consumer_client.FarmGet;
import com.example.consumer_client.MdGet;
import com.example.consumer_client.StoreGet;
import com.example.consumer_client.address.AddressResponse;
import com.example.consumer_client.user.data.KakaoLoginData;
import com.example.consumer_client.user.data.KakaoLoginResponse;
import com.example.consumer_client.user.data.GoogleLoginData;
import com.example.consumer_client.user.data.GoogleLoginResponse;
import com.example.consumer_client.user.data.LoginData;
import com.example.consumer_client.user.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceApi {

//    @POST("/googleLogin")
//    Call<GoogleLoginResponse> userGoogleLogin(@Body GoogleLoginData data);

//    @GET("/farmView")
//    Call<FarmGet> getFarmData();

    @GET("/storeView")
    Call<StoreGet> getStoreData();

    @GET("/mdView_main")
    Call<MdGet> getMdMainData();

    @POST("/farmDetail")
    Call<FarmDetailResponse> farmDetail(@Body FarmDetailData data);

    @POST("/register_address")
    Call<AddressResponse> addressRegister(@Body AddressData data);
}
