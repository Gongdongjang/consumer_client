package com.example.consumer_client.address;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressData {
    @SerializedName("latlonglist")
    private final List latlonglist;

    public AddressData(List latlonglist){
        this.latlonglist = latlonglist;
    }

}
