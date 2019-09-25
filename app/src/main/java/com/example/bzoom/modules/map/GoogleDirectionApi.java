package com.example.bzoom.modules.map;

import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleDirectionApi {

    public static String duration;
    public static String distance;
    public static String durationText;
    public static String distanceText;
    public static JSONObject directionResponse;

    public static String getDuration() {
        return duration;
    }

    public static void setDuration(String duration) {
        GoogleDirectionApi.duration = duration;
    }

    public static String getDistance() {
        return distance;
    }

    public static void setDistance(String distance) {
        GoogleDirectionApi.distance = distance;
    }

    public static String getDurationText() {
        return durationText;
    }

    public static void setDurationText(String durationText) {
        GoogleDirectionApi.durationText = durationText;
    }

    public static String getDistanceText() {
        return distanceText;
    }

    public static void setDistanceText(String distanceText) {
        GoogleDirectionApi.distanceText = distanceText;
    }
}
