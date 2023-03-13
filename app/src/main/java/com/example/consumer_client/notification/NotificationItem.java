package com.example.consumer_client.notification;

public class NotificationItem {

    private String notiTitle;
    private String notiContent;
    private String target;

    public String getNotiTitle() {
        return notiTitle;
    }

    public void setNotiTitle(String notiTitle) {
        this.notiTitle = notiTitle;
    }

    public String getNotiContent() {
        return notiContent;
    }

    public void setNotiContent(String notiContent) {this.notiContent = notiContent;
    }
    public String getTarget() {return target;}

    public void setTarget(String target) {this.target = target;}
}
