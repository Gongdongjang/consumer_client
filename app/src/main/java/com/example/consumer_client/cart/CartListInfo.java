package com.example.consumer_client.cart;

public class CartListInfo {
    private String userId;
    private String storeProdImgView;
    private String storeName;
    private String mdName;
    private String mdPrice;
    private String puDate;
    private String totalPrice;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreProdImgView() {
        return storeProdImgView;
    }

    public void setStoreProdImgView(String storeProdImgView) {
        this.storeProdImgView = storeProdImgView;
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

    public String getMdPrice() {
        return mdPrice;
    }

    public void setMdPrice(String mdPrice) {
        this.mdPrice = mdPrice;
    }

    public String getPuDate() {
        return puDate;
    }

    public void setPuDate(String puDate) {
        this.puDate = puDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
