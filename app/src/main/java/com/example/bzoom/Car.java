package com.example.bzoom;

import java.util.ArrayList;

public class Car {
    String name;
    float rating;
    String number;
    String color;
    int status;
    public String carModal;
    public int carId;
    public int carTypeId;
    public String carImage;
    public static int activeCarId;
    public static ArrayList<Car> cars;
        public Car(String name, float rating, String number, String color, int status, String carModal, int carId, int carTypeId, String carImage) {
        this.name = name;
        this.rating = rating;
        this.number = number;
        this.color = color;
        this.status = status;
        this.carModal = carModal;
        this.carId = carId;
        this.carTypeId = carTypeId;
        this.carImage = carImage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCarModal() {
        return carModal;
    }

    public void setCarModal(String carModal) {
        this.carModal = carModal;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCarTypeId() {
        return carTypeId;
    }

    public void setCarTypeId(int carTypeId) {
        this.carTypeId = carTypeId;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
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



    /*  labels and input type of vahicals in database
        "id": 6,
        "company_name": "toyota",
        "model": "carolla",
        "year": "2016",
        "created_at": "2019-08-17 13:53:58",
        "updated_at": "2019-08-17 20:53:58",
        "plate_number": "4314",
        "engine_number": "342343424234",
        "vt_id": "37",
        "is_approve": "1",
        "user_id": "5",
        "engine_cc": null,
        "insurance_paper_name": "DSC_0040.JPG",
        "vehicle_letter_name": "DSC_0046.JPG",
        "vehicle_document_name": "DSC_0045.JPG",
        "police_record_name": null,
        "insurance_paper_path": "uploads/insurance_file/Kgzq5-083410-Ykn.JPG",
        "vehicle_letter_path": "uploads/vehicle_letter/sEwmL-083409-iG3.JPG",
        "vehicle_document_path": "uploads/vehicle_document/7WqB9-083408-D2O.JPG",
        "police_record_path": null,
        "color": "white"
*/
}
