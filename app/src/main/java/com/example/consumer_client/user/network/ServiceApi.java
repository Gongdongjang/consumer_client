package com.example.consumer_client.user.network;
import com.example.consumer_client.address.AddressData;
import com.example.consumer_client.FarmDetailData;
import com.example.consumer_client.FarmDetailResponse;
import com.example.consumer_client.FarmGet;
import com.example.consumer_client.StoreDetailData;
import com.example.consumer_client.StoreDetailResponse;
import com.example.consumer_client.address.AddressResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceApi {
    @GET("/farmView")
    Call<FarmGet> getFarmData();

    @POST("/farmDetail")
    Call<FarmDetailResponse> farmDetail(@Body FarmDetailData data);


    @POST("/storeDetail")
    Call<StoreDetailResponse> storeDetail(@Body StoreDetailData data);

    @POST("/register_address")
    Call<AddressResponse> addressRegister(@Body AddressData data);

}
