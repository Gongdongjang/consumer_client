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
    public void setFarm_loc(){

        this.farm_loc = farm_loc;
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