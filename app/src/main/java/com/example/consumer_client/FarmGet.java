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

    @SerializedName("md_name")
    private List md_name;

    @SerializedName("md_count")
    private List md_count;

//    @SerializedName("store_name")
//    private List store_name;
//
//    @SerializedName("pu_start")
//    private List pu_start;
//
//    @SerializedName("pu_end")
//    private List pu_end;

    @SerializedName("farm_id")
    private List farm_id;

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

    //농가 아이디
    public List getFarm_id(){ return farm_id; }
    public void setFarm_id(){
        this.farm_id = farm_id;
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


    //제품명
    public List getMd_name() { return md_name; }
    public void setMd_name() { this.md_name = md_name; }
    //제품 개수
    public List getMd_count() { return md_count; }
    public void setMd_count() { this.md_count = md_count; }
//    //스토어명
//    public List getStore_name() { return store_name; }
//    public void setStore_name() { this.store_name = store_name; }
//    //픽업기간-시작
//    public List getPu_start(){
//        return pu_start;
//    }
//    public void setPu_start(){
//        this.pu_start = pu_start;
//    }
//    //픽업기간-끝
//    public List getPu_end(){
//        return pu_end;
//    }
//    public void setPu_end(){
//        this.pu_end = pu_end;
//    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }
}