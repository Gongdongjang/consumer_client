package com.gdjang.consumer_client.review;

public class ReviewListInfo {
    private String userId;
    private String orderId;
    private String reviewImg1;
    private String reviewImg2;
    private String reviewImg3;
    private String storeName;
    private String mdName;
    private String reviewContent;
    private String rvw_rating;
    private String storeProdImgView;
    private String mdQty;
    private String mdPrice;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReviewImg1() {
        return reviewImg1;
    }

    public void setReviewImg1(String reviewImg1) {
        this.reviewImg1 = reviewImg1;
    }

    public String getReviewImg2() {
        return reviewImg2;
    }

    public void setReviewImg2(String reviewImg2) {
        this.reviewImg2 = reviewImg2;
    }

    public String getReviewImg3() {
        return reviewImg3;
    }

    public void setReviewImg3(String reviewImg3) {
        this.reviewImg3 = reviewImg3;
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

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getRvw_rating() {
        return rvw_rating;
    }

    public void setRvw_rating(String rvw_rating) {
        this.rvw_rating = rvw_rating;
    }

    public String getStoreProdImgView() {
        return storeProdImgView;
    }

    public void setStoreProdImgView(String storeProdImgView) {
        this.storeProdImgView = storeProdImgView;
    }

    public String getMdQty() {
        return mdQty;
    }

    public void setMdQty(String mdQty) {
        this.mdQty = mdQty;
    }

    public String getMdPrice() {
        return mdPrice;
    }

    public void setMdPrice(String mdPrice) {
        this.mdPrice = mdPrice;
    }
}
