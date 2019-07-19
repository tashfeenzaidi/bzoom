package com.example.bzoom;

public class Owner {

    String name;
    String number;
    String address;
    String car;
    double rating;

    public Owner(String name, String number, String address, String car, double rating) {
        this.name = name;
        this.number = number;
        this.address = address;
        this.car = car;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
