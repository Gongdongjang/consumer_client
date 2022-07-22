package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreGet {

    @SerializedName("st_arr")
    private List st_arr;

    @SerializedName("md_arr")
    private List md_arr;

    @SerializedName("pu_start")
    private List pu_start;

    @SerializedName("pu_end")
    private List pu_end;

    @SerializedName("md_name")
    private List md_name;

    @SerializedName("md_count")
    private List md_count;

    //세부페이지 store정보
    @SerializedName("store_info")
    private List store_info;
    @SerializedName("store_hours")
    private List store_hours;
    @SerializedName("store_restDays")
    private List store_restDays;
    @SerializedName("store_loc")
    private List store_loc;
    @SerializedName("store_lat")
    private List store_lat;
    @SerializedName("store_long")
    private List store_long;
    @SerializedName("store_id")
    private List store_id;
    private int code;

//    public void setSt_arr(List st_arr) {
//        this.st_arr = st_arr;
//    }

    public List getStore_info() {
        return store_info;
    }

    public void setStore_info() {
        this.store_info = store_info;
    }

    public List getStore_hours() {
        return store_hours;
    }

    public void setStore_hours() {
        this.store_hours = store_hours;
    }

    public List getStore_restDays() {
        return store_restDays;
    }

    public void setStore_restDays() {
        this.store_restDays = store_restDays;
    }

    public List getStore_loc() {
        return store_loc;
    }

    public void setStore_loc() {
        this.store_loc = store_loc;
    }

    public List getStore_lat() {
        return store_lat;
    }

    public void setStore_lat() {
        this.store_lat = store_lat;
    }

    public List getStore_long() {
        return store_long;
    }

    public void setStore_long() {
        this.store_long = store_long;
    }

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

    //스토어 아이디
    public List getStore_id(){ return store_id; }
    public void setStore_id(){
        this.store_id = store_id;
    }


    //Store 리사이클러뷰 정보
//    public List getMd_arr(){
//        return md_arr;
//    }
//    public void setMd_arr(){
//        this.md_arr = md_arr;
//    }
//
    public List getSt_arr(){
        return st_arr;
    }
    public void setSt_arr(){
        this.st_arr = st_arr;
    }
//
//
//    public List getPu_start(){
//        return pu_start;
//    }
//    public void setPu_start(){
//        this.pu_start = pu_start;
//    }
//
//    public List getPu_end(){
//        return pu_end;
//    }
//    public void setPu_end(){
//        this.pu_end = pu_end;
//    }

//    //StoreView Detail 세부페이지 Store테이블에서 받아오는
//    @SerializedName("store_count")
//    private String store_count;
//    public String getStore_Count(){
//        return store_count;
//    }public void setStore_Count(){
//        this.store_count = store_count;
//    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

    //제품명
    public List getMd_name() { return md_name; }
    public void setMd_name() { this.md_name = md_name; }
    //제품 개수
    public List getMd_count() { return md_count; }
    public void setMd_count() { this.md_count = md_count; }
}
