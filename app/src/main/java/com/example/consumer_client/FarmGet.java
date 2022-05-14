package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FarmGet {

    @SerializedName("farm_arr")
    private List farm_arr;

    @SerializedName("farm_mainItem")
    private List farm_mainItem;

    @SerializedName("farm_info")
    private List farm_info;

    @SerializedName("farm_loc")
    private List farm_loc;

    @SerializedName("farm_lat")
    private List farm_lat;

    @SerializedName("farm_long")
    private List farm_long;

    @SerializedName("farm_hours")
    private List farm_hours;


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

    //농가이름
    public List getFarm_arr(){ return farm_arr; }
    public void setFarm_arr(){
        this.farm_arr = farm_arr;
    }
    //농가 판매 물품
    public List getFarm_mainItem(){
        return farm_mainItem;
    }
    public void setFarm_mainItem(){
        this.farm_mainItem = farm_mainItem;
    }
    //농가info
    public List getFarm_info(){
        return farm_info;
    }
    public void setFarm_info(){
        this.farm_info = farm_info;
    }
    //농가위치
    public List getFarm_loc(){
        return farm_loc;
    }
    public void setFarm_loc(){ this.farm_loc = farm_loc; }
    //농가위도
    public List getFarm_lat() { return farm_lat; }
    public void setFarm_lat() { this.farm_lat = farm_lat; }
    //농가경도
    public List getFarm_long() { return farm_long; }
    public void setFarm_long() { this.farm_long = farm_long; }
    //농가 영업시간
    public List getFarm_hours() { return farm_hours; }
    public void setFarm_hours() { this.farm_hours = farm_hours; }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }
}