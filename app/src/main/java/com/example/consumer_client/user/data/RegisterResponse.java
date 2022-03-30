package com.example.consumer_client.user.data;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

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
