package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MdGet {
    @SerializedName("st_name")
    public List st_name;

    @SerializedName("md_name")
    private List md_name;

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

    public List getSt_name(){
        return st_name;
    }
    public void setSt_name(){
        this.st_name = st_name;
    }

    public List getMd_name(){
        return md_name;
    }
    public void setMd_name(){
        this.md_name = md_name;
    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

}
