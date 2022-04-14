package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

public class FarmData {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("farm_name")
    private String farm_name;

    @SerializedName("farm_info")
    private String farm_info;

    @SerializedName("farm_mainItem")
    private String farm_mainItem;

//    public FarmData(int code, String message, String farm_name, String farm_info, String farm_mainItem) {
//        this.code = code;
//        this.message = message;
//        this.farm_name = farm_name;
//        this.farm_info = farm_info;
//        this.farm_mainItem = farm_mainItem;
//    }
}
