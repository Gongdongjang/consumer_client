package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreGet {
    @SerializedName("store")
    public List store;

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("store_info")
    private String store_info;

//    @SerializedName("farm_mainItem")
//    private String farm_mainItem;

    private int code;
    public int getCode(){
        return code;
    }
    public void setCode(){
        this.code = code;
    }

    private String message;
    public String getMessage(){
        return message;
    }
    public void setMessage(){
        this.message = message;
    }

    public String getStore_name(){
        return store_name;
    }
    public void setStore_name(){
        this.store_name = store_name;
    }

    public String getStore_info(){
        return store_info;
    }
    public void setStore_info(){
        this.store_info = store_info;
    }

//    public String getFarm_mainItem(){
//        return farm_mainItem;
//    }
//    public void setFarm_mainItem(){
//        this.farm_mainItem = farm_mainItem;
//    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

    public List getStore(){
        return store;
    }
    public void setStore(){
        this.store = store;
    }

}
