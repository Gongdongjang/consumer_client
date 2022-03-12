package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;
import com.kakao.sdk.user.model.Gender;

public class KakaoLoginData {
    @SerializedName("user_id")
    String user_id;

    @SerializedName("user_name")
    String user_name;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("sns_type")
    String sns_type;

    @SerializedName("refresh_token")
    String refresh_token;

    @SerializedName("gender")
    Gender gender;

    public KakaoLoginData(String user_id, String user_name, String nickname, String sns_type,String refresh_token, Gender gender) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.nickname = nickname;
        this.sns_type=sns_type;
        this.refresh_token=refresh_token;
        this.gender = gender;
    }
}
