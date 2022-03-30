package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("password")
    private String password;

//    @SerializedName("auto_check")
//    private Boolean auto_check;

    public LoginData(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
//        this.auto_check = auto_check;
    }
}
