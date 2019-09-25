package com.example.bzoom.modules.map.rider;


public class Rider {

    public static int id;
    public int carType;
    public double lat;
    public double lon;
    public boolean cab;
    public String rideStatus;
    public int driverId;
    public double drivelat;
    public double driverlon;
    public static String name;
    public static String number;
    public static float ratingBar;
    public static String comment;

    public static String getComment() {
        return comment;
    }

    public static void setComment(String comment) {
        Rider.comment = comment;
    }

    public static float getRatingBar() {
        return ratingBar;
    }

    public static void setRatingBar(float ratingBar) {
        Rider.ratingBar = ratingBar;
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        Rider.number = number;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Rider.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Rider.name = name;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public double getDrivelat() {
        return drivelat;
    }

    public void setDrivelat(double drivelat) {
        this.drivelat = drivelat;
    }

    public double getDriverlon() {
        return driverlon;
    }

    public void setDriverlon(double driverlon) {
        this.driverlon = driverlon;
    }


    public Rider() {
    }

    public Rider(int id, int carType, double lat, double lon, boolean cab) {
        this.id = id;
        this.carType = carType;
        this.lat = lat;
        this.lon = lon;
        this.cab = cab;
        this.rideStatus = "finding";
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public void setRideStatus(String rideStatus) {
        this.rideStatus = rideStatus;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
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

    public boolean isCab() {
        return cab;
    }

    public void setCab(boolean cab) {
        this.cab = cab;
    }
}
