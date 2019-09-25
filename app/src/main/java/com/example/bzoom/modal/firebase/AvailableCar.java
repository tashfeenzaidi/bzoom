package com.example.bzoom.modal.firebase;

public class AvailableCar {

    String name;
    String number;
    String plateNumber;
    String modal;
    double rating;
    double lat;
    double lon;
    int ownerId;
    String color;
    int availableId;
    String startDate;
    String startTime;
    String endDate;
    String endTime;
    int vehicalId;
    public static int activeAvailableId;
    public static int activeVehicalId;
    public static int activeOwnerId;
    public static double activelat;
    public static double activelon;
    public static String activeEndDate;
    public static String activeEndTime;
    public static String status = "";



    public int getVehicalId() {
        return vehicalId;
    }

    public void setVehicalId(int vehicalId) {
        this.vehicalId = vehicalId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAvailableId() {
        return availableId;
    }

    public void setAvailableId(int availableId) {
        this.availableId = availableId;
    }
}
