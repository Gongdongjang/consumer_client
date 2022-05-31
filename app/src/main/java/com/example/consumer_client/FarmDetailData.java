package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

public class FarmDetailData {
    @SerializedName("farm_id")
    private int farm_id;

    @SerializedName("md_count")
    private int md_count;

    public FarmDetailData(int farm_id, int md_count) {
        this.farm_id = farm_id;
        this.md_count = md_count;
//        this.auto_check = auto_check;
    }
}
