package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmDetailResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("md_name")
    private List md_name;

    @SerializedName("md_start")
    private List md_start;

    @SerializedName("md_end")
    private List md_end;

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public List getMd_name(){
        return md_name;
    }
    public List getMd_start(){
        return md_start;
    }
    public List getMd_end(){
        return md_end;
    }
}
