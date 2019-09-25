package com.example.bzoom.modules.map;

import java.util.ArrayList;

public class Complaint {

    public static int availableId;
    public static boolean isOwner;
    public static String description;
    public static ArrayList<Integer> clauseList ;

    public static int getAvailableId() {
        return availableId;
    }

    public static void setAvailableId(int availableId) {
        Complaint.availableId = availableId;
    }

    public static boolean isIsOwner() {
        return isOwner;
    }

    public static void setIsOwner(boolean isOwner) {
        Complaint.isOwner = isOwner;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        Complaint.description = description;
    }
}
