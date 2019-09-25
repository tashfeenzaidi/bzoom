package com.example.bzoom.modal.firebase.owner;

public class Owner {

   public static  String name;
   public static  String rating;
   public static  String number;
   public static  String profilePic;
   public static  String carImage;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Owner.name = name;
    }

    public static String getRating() {
        return rating;
    }

    public static void setRating(String rating) {
        Owner.rating = rating;
    }

    public static String getNumber() {
        return number;
    }

    public static void setNumber(String number) {
        Owner.number = number;
    }

    public static String getProfilePic() {
        return profilePic;
    }

    public static void setProfilePic(String profilePic) {
        Owner.profilePic = profilePic;
    }

    public static String getCarImage() {
        return carImage;
    }

    public static void setCarImage(String carImage) {
        Owner.carImage = carImage;
    }
}
