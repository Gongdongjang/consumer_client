package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

public class StoreDetailData {
    @SerializedName("store_id")
    private int store_id;

    @SerializedName("md_count")
    private int md_count;

    public StoreDetailData(int store_id, int md_count) {
        this.store_id = store_id;
        this.md_count = md_count;
    }
}
