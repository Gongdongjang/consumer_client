package com.gdjang.consumer_client.cart;

public class CartListInfo {
    private String userId;
    private String mdImg;
    private String storeName;
    private String mdName;
    private int eachMdPrice;
    private String eachMdTotalPriceP;
    private int eachMdTotalPrice;
    private int eachStoreTotalPrice;
    private String mdSet;
    private String qty;
    private int totalPrice;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMdImg() {
        return mdImg;
    }

    public void setMdImg(String mdImg) {
        this.mdImg = mdImg;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
    }

    public String getEachMdPrice() {
        return String.valueOf(eachMdPrice);
    }

    public void setEachMdPrice(int eachMdPrice) {
        this.eachMdPrice = eachMdPrice;
    }

    public String getEachMdTotalPriceP() {
        return eachMdTotalPriceP;
    }

    public void setEachMdTotalPriceP(String eachMdTotalPriceP) {
        this.eachMdTotalPriceP = eachMdTotalPriceP;
    }

    public String getEachMdTotalPrice() {
        return String.valueOf(eachMdTotalPrice);
    }

    public void setEachMdTotalPrice(int eachMdTotalPrice) {
        this.eachMdTotalPrice = eachMdTotalPrice;
    }

    public String getEachStoreTotalPrice() {
        return String.valueOf(eachStoreTotalPrice);
    }

    public void setEachStoreTotalPrice(int eachStoreTotalPrice) {
        this.eachStoreTotalPrice = eachStoreTotalPrice;
    }

    public String getMdSet() {
        return mdSet;
    }

    public void setMdSet(String mdSet) {
        this.mdSet = mdSet;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalPrice() {return String.valueOf(totalPrice);}
    public void setTotalPrice(int totalPrice){this.totalPrice = totalPrice;}
}
