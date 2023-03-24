package com.example.consumer_client.store;

public class StoreReviewInfo {
    private String userProfileImg;
    private String userID;
    private String prodName;
    private String starCount;
    private String reviewMessage;

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {this.prodName = prodName; }

    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    public String getReview() {
        return reviewMessage;
    }

    public void setReview(String reviewMessage) {this.reviewMessage = reviewMessage;}
}
