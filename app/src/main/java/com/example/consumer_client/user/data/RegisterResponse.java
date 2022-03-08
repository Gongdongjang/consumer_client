package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

//    @SerializedName("user_id")
//    String user_id;
//
//    @SerializedName("password")
//    String password;
//
//    @SerializedName("user_name")
//    String user_name;
//
//    @SerializedName("mobile_no")
//    String mobile_no;
//
//    @SerializedName("nickname")
//    String nickname;
//
//    @SerializedName("message")
//    String message;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private String user_id;

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public String getUser_id(){
        return user_id;
    }
}
