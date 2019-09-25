package com.example.bzoom.modules.map.driver;

import com.example.bzoom.modal.firebase.DriverFirebase;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.rider.Rider;

import org.json.JSONObject;

import java.util.List;

public class Driver {
    public static int id;
    public static int carType;

    public static double driverLon;
    public static double driverLat;
    public static double driverRating;
    public static String imgPath;
    public static String name;
    public static String number;
    public static long totalRides;
    public static long totalReview;
    public static long totalMonth;
    public static long totalEarned;


    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        Driver.number = number;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Driver.name = name;
    }

    public static String getImgPath() {
        return imgPath;
    }

    public static void setImgPath(String imgPath) {
        Driver.imgPath = imgPath;
    }



    public static double getDriverRating() {
        return driverRating;
    }

    public static void setDriverRating(double driverRating) {
        Driver.driverRating = driverRating;
    }

    public static JSONObject driver;
    DriverFirebase driverFirebase;

    public static double getDriverLon() {
        return driverLon;
    }

    public static void setDriverLon(double driverLon) {
        Driver.driverLon = driverLon;
    }

    public static double getDriverLat() {
        return driverLat;
    }

    public static void setDriverLat(double driverLat) {
        Driver.driverLat = driverLat;
    }



    public Driver(){

    }

    public Driver(int id, int carType) {
        this.id = id;
        this.carType = carType;
        driverFirebase =  new DriverFirebase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarType() {
        return carType;
    }

    public void setCarType(int carType) {
        this.carType = carType;
    }

    public void activateDriver(Driver driver){

         driverFirebase.addDriver(driver);


    }

    public List<Rider> getRides(){

        driverFirebase.getRiderByCarTypeOnNew();


       return driverFirebase.getRiderList();
    }





}
