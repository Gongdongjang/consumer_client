package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginData {
    @SerializedName("user_id")
    private String user_id;

    @SerializedName("name")
    private String name;

    @SerializedName("nickname")
    private String nickname;

//    @SerializedName("auto_check")
//    private Boolean auto_check;

    public GoogleLoginData(String user_id, String name, String nickname) {
        this.user_id = user_id;
        this.name = name;
        this.nickname = nickname;
//        this.auto_check = auto_check;
    }
}
