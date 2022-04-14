package com.example.consumer_client;

import com.google.gson.annotations.SerializedName;

public class FarmResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("farm_name")
    private String farm_name;

    @SerializedName("farm_info")
    private String farm_info;

    @SerializedName("farm_mainItem")
    private String farm_mainItem;

//    private String farmProdImgView;
//    public String getFarmProdImgView() {
//        return farmProdImgView;
//    }
//    public void setFarmProdImgView(String farmProdImgView) {
//        this.farmProdImgView = farmProdImgView;
//    }
//
//    private int farmSituation;
//    public int getFarmSituation() {
//        return farmSituation;
//    }
//    public void setFarmSituation(int farmSituation) {
//        this.farmSituation = farmSituation;
//    }

    public int getCode(){
        return code;
    }
    public void setCode(){
        this.code = code;
    }

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
}
