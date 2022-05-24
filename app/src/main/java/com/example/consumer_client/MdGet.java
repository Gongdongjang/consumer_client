package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MdGet {
    @SerializedName("farm_name")
    public List farm_name;

    @SerializedName("md_name")
    private List md_name;

    @SerializedName("st_name")
    private List st_name;

    @SerializedName("pay_schedule")
    private List pay_schedule;

    @SerializedName("pu_start")
    private List pu_start;

    @SerializedName("pu_end")
    private List pu_end;

    @SerializedName("buying_count")
    private List buying_count;

    @SerializedName("goal_people")
    private List goal_people;

    @SerializedName("farmer_name")
    private List farmer_name;

    @SerializedName("farm_desc")
    private List farm_desc;

    @SerializedName("st_desc")
    private List st_desc;

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

    public List getFarm_name(){ return farm_name; }
    public void setFarm_name(){
        this.farm_name = farm_name;
    }

    public List getPay_schedule(){
        return pay_schedule;
    }
    public void setPay_schedule(){
        this.pay_schedule = pay_schedule;
    }

    public List getPu_start(){
        return pu_start;
    }
    public void setPu_start(){
        this.pu_start = pu_start; }

    public List getPu_end(){
        return pu_end;
    }
    public void setPu_end(){
        this.pu_end = pu_end; }

    public List getBuying_count(){
        return buying_count;
    }
    public void setBuying_count(){ this.buying_count = buying_count; }

    public List getGoal_people() { return goal_people; }
    public void setGoal_people() { this.goal_people = goal_people; }

    public List getFarmer_name() { return farmer_name; }
    public void setFarmer_name() { this.farmer_name = farmer_name; }

    public List getFarm_desc() { return farm_desc; }
    public void setFarm_desc() { this.farm_desc = farm_desc; }

    public List getSt_desc() { return st_desc; }
    public void setSt_desc() { this.st_desc = st_desc; }

    @SerializedName("count")
    private String count;
    public String getCount(){
        return count;
    }
    public void setCount(){
        this.count = count;
    }

}
