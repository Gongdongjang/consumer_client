package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FarmGet {
    @SerializedName("farm")
    public List farm;

    @SerializedName("farm_name")
    private String farm_name;

    @SerializedName("farm_info")
    private String farm_info;

    @SerializedName("farm_mainItem")
    private String farm_mainItem;

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

    public String getFarm_name(){
        return farm_name;
    }
    public void setFarm_name(){
        this.farm_name = farm_name;
    }

    public String getFarm_info(){
        return farm_info;
    }
    public void setFarm_info(){
        this.farm_info = farm_info;
    }

    public String getFarm_mainItem(){
        return farm_mainItem;
    }
    public void setFarm_mainItem(){
        this.farm_mainItem = farm_mainItem;
    }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

    public List getFarm(){
        return farm;
    }
    public void setFarm(){
        this.farm = farm;
    }

}
