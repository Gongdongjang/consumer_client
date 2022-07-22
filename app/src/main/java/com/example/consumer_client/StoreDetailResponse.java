package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreDetailResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("md_name")
    private List md_name;

    @SerializedName("pu_start")
    private List pu_start;

    @SerializedName("pu_end")
    private List pu_end;

    @SerializedName("farm_name")
    private List farm_name;

    @SerializedName("pay_schedule")
    private List pay_schedule;

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public List getMd_name(){
        return md_name;
    }

    public List getPu_start(){
        return pu_start;
    }
    public List getPu_end(){
        return pu_end;
    }
    public List getFarm_name(){
        return farm_name;
    }
    public List getPay_schedule(){
        return pay_schedule;
    }

}
