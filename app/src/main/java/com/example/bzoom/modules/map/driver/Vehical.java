package com.example.bzoom.modules.map.driver;

import org.json.JSONObject;

public class Vehical {

    public static JSONObject vehicalData;
    public static String color;
    public static String modal;
    public static String numbre;
    public static String image;
    public static String name;
    public String carRating;
    public String carName;
    public String carModal;
    public boolean status;
    public int carId;
    public int carTypeId;
    public String carImage;

    public Vehical(String carRating, String carName, String carModal, int carId, int carTypeId, String carImage) {
        this.carRating = carRating;
        this.carName = carName;
        this.carModal = carModal;
        this.carId = carId;
        this.carTypeId = carTypeId;
        this.carImage = carImage;
    }

    public String getCarRating() {
        return carRating;
    }

    public void setCarRating(String carRating) {
        this.carRating = carRating;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModal() {
        return carModal;
    }

    public void setCarModal(String carModal) {
        this.carModal = carModal;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
