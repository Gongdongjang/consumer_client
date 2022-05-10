package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreGet {
//    @SerializedName("store")
//    public List store;
//
//    public List getStore(){
//        return store;
//    }
//
//    public void setStore(){
//        this.store = store;
//    }

    @SerializedName("st_arr")
    private List st_arr;

    @SerializedName("md_arr")
    private List md_arr;

    @SerializedName("pu_start")
    private List pu_start;

    @SerializedName("pu_end")
    private List pu_end;

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

    public List getMd_arr(){
        return md_arr;
    }
    public void setMd_arr(){
        this.md_arr = md_arr;
    }

    public List getSt_arr(){
        return st_arr;
    }
    public void setSt_arr(){
        this.st_arr = st_arr;
    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

    public List getPu_start(){
        return pu_start;
    }
    public void setPu_start(){
        this.pu_start = pu_start;
    }

    public List getPu_end(){
        return pu_end;
    }

    public void setPu_end(){
        this.pu_end = pu_end;
    }

}
