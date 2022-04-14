package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MdGet {
    @SerializedName("md")
    public List md;

    @SerializedName("farm_name")
    public List farm_name;

    @SerializedName("md_name")
    private String md_name;

//    @SerializedName("farm_info")
//    private String farm_info;

//    @SerializedName("farm_name")
//    private String farm_name;

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

    public List getFarm_name(){
        return farm_name;
    }
    public void setFarm_name(){
        this.farm_name = farm_name;
    }

    public String getMd_name(){
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

    public List getMd(){
        return md;
    }
    public void setMd(){
        this.md = md;
    }
}
