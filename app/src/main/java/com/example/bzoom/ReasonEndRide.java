package com.example.bzoom;

import org.json.JSONArray;

import java.util.ArrayList;

public class ReasonEndRide  {

   public static int id;
   public static String reason;
   public static JSONArray reasonEndRides;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ReasonEndRide.id = id;
    }

    public static String getReason() {
        return reason;
    }

    public static void setReason(String reason) {
        ReasonEndRide.reason = reason;
    }
}
