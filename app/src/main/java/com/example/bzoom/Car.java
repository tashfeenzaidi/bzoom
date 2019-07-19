package com.example.bzoom;

public class Car {
    String name;
    float rating;
    String number;
    String color;
    int status;

    Car(String name, float rating, String number, String color,int status){
        this.name = name;
        this.color= color;
        this.number = number;
        this.rating = rating;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
