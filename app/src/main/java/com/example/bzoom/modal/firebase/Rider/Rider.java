package com.example.bzoom.modal.firebase.Rider;

public class Rider {

    private static String  vehicalId;
    private static String  driverId;
    private static String  rideId;

    public static String getVehicalId() {
        return vehicalId;
    }

    public static void setVehicalId(String vehicalId) {
        Rider.vehicalId = vehicalId;
    }

    public static String getDriverId() {
        return driverId;
    }

    public static void setDriverId(String driverId) {
        Rider.driverId = driverId;
    }

    public static String getRideId() {
        return rideId;
    }

    public static void setRideId(String rideId) {
        Rider.rideId = rideId;
    }
}
