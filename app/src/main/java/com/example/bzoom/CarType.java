package com.example.bzoom;


import org.json.JSONArray;

public class CarType {

    public int id;
    String imageUrl;
    int rate;
    String time;
    boolean status;
    String name;
    String topicname;
    public static String activeCarTopicName;
    public static JSONArray carTypes;
    public static int activeCarId;

    public CarType(int id,String image, int rate, String time,String name,String topicname) {
        this.imageUrl = image;
        this.rate = rate;
        this.time = time;
        this.id = id;
        this.name = name;
        this.status = false;
        this.topicname = topicname;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CarType() {
    }



    public String getImage() {
        return imageUrl;
    }

    public void setImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
