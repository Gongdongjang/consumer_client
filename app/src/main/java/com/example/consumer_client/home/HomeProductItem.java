package com.example.consumer_client.home;

//필요 없어 보임

public class HomeProductItem {
    private String homeMdId;
    private String homeProdImg;
    private String homeProdName;
    private String homeProdEx;
    private String homeDistance;

    public String getHomeMdId() {
        return homeMdId;
    }

    public void setHomeMdId(String homeMdId) {
        this.homeMdId = homeMdId;
    }


    public String getHomeProdImg() {
        return homeProdImg;
    }

    public void setHomeProdImg(String homeProdImg) {
        this.homeProdImg = homeProdImg;
    }

    public String getHomeProdName() {
        return homeProdName;
    }

    public void setHomeProdName(String homeProdName) {
        this.homeProdName = homeProdName;
    }

    public String getHomeProdEx() {
        return homeProdEx;
    }

    public void setHomeProdEx(String homeProdEx) {
        this.homeProdEx = homeProdEx;
    }

    public void setHomeDistance(String homeDistance) {this.homeDistance = homeDistance;}

    public String getHomeDistance() { return homeDistance;}
}
