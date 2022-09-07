package com.example.consumer_client.farm;

import android.widget.ImageView;
import android.widget.TextView;

public class FarmDetailInfo {
    private String prodImg;
    private String farmName;
    private String prodName;
    private String storeName;
    private String puTerm;

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {this.prodName = prodName; }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getPuTerm() {
        return puTerm;
    }

    public void setPuTerm(String puTerm) {
        this.puTerm = puTerm;
    }

    public String getProdImg() {
        return prodImg;
    }

    public void setProdImg(String prodImg) {
        this.prodImg = prodImg;
    }

}
