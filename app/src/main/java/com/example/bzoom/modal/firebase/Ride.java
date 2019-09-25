package com.example.bzoom.modal.firebase;

import org.json.JSONObject;

public class Ride {

    private static String lat;
    private static String lon;
    private static String latDrop;
    private static String lonDrop;
    private static String customerID;
    private static String rideID;
    private static boolean isCab;
    private static String vehicalId;
    public static JSONObject jsonObject;
    public static double distance;
    public static double fair;
    public static String status;
    public static String AmountCollected;
    public static String startDate;
    public static String endDate;
    public static String startTime;
    public static String endTime;
    public static String address;

    public static String getStartDate() {
        return startDate;
    }

    public static void setStartDate(String startDate) {
        Ride.startDate = startDate;
    }

    public static String getEndDate() {
        return endDate;
    }

    public static void setEndDate(String endDate) {
        Ride.endDate = endDate;
    }

    public static String getStartTime() {
        return startTime;
    }

    public static void setStartTime(String startTime) {
        Ride.startTime = startTime;
    }

    public static String getEndTime() {
        return endTime;
    }

    public static void setEndTime(String endTime) {
        Ride.endTime = endTime;
    }

    public static String getAmountCollected() {
        return AmountCollected;
    }

    public static void setAmountCollected(String amountCollected) {
        AmountCollected = amountCollected;
    }

    public static double getDistance() {
        return distance;
    }

    public static void setDistance(double distance) {
        Ride.distance = distance;
    }

    public static double getFair() {
        return fair;
    }

    public static void setFair(double fair) {
        Ride.fair = fair;
    }

    public static String getVehicalId() {
        return vehicalId;
    }

    public static void setVehicalId(String vehicalId) {
        Ride.vehicalId = vehicalId;
    }

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        Ride.lat = lat;
    }

    public static String getLon() {
        return lon;
    }

    public static void setLon(String lon) {
        Ride.lon = lon;
    }

    public static String getLatDrop() {
        return latDrop;
    }

    public static void setLatDrop(String latDrop) {
        Ride.latDrop = latDrop;
    }

    public static String getLonDrop() {
        return lonDrop;
    }

    public static void setLonDrop(String lonDrop) {
        Ride.lonDrop = lonDrop;
    }

    public static String getCustomerID() {
        return customerID;
    }

    public static void setCustomerID(String customerID) {
        Ride.customerID = customerID;
    }

    public static String getRideID() {
        return rideID;
    }

    public static void setRideID(String rideID) {
        Ride.rideID = rideID;
    }

    public static boolean isIsCab() {
        return isCab;
    }

    public static void setIsCab(boolean isCab) {
        Ride.isCab = isCab;
    }
}
