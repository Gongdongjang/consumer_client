package com.example.consumer_client.address;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("latlonglist")
    private List latlonglist;

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public List getLatlonglist(){
        return latlonglist;
    }

}
