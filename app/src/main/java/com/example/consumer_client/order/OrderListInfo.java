package com.example.consumer_client.order;

public class OrderListInfo {
    private String storeId;
    private String storeProdImgView;
    private String storeName;
    private String mdName;
    private String storeLocationFromMe;
    private String storeLoc;
    private String mdQty;
    private String orderId;
    private String mdPrice;
    private String mdStatus;
    private String puDate;
    private String storeLong;
    private String storeLat;

    public String getStoreid() {
        return storeId;
    }

    public void setStoreid(String storeid) {
        this.storeId = storeid;
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

    public String getStoreLocationFromMe() {
        return storeLocationFromMe;
    }

    public void setStoreLocationFromMe(String storeLocationFromMe) {this.storeLocationFromMe = storeLocationFromMe;}

    public String getStoreLoc() {
        return storeLoc;
    }

    public void setStoreLoc(String storeLoc) {this.storeLoc = storeLoc;}

    public String getStoreLat() {
        return storeLat;
    }

    public void setStoreLat(String storeLat) {this.storeLat = storeLat;}

    public String getStoreLong() {
        return storeLong;
    }

    public void setStoreLong(String storeLong) {this.storeLong = storeLong;}

    public String getMdQty() { return mdQty; }

    public void setMdQty(String mdQty) {
        this.mdQty = mdQty;
    }

    public String getMdStatus() {
        return mdStatus;
    }

    public void setMdStatus(String mdStatus) {
        this.mdStatus = mdStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

}
