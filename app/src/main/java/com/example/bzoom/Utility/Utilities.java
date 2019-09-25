package com.example.bzoom.Utility;

import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {





    public static void showToast(Context context, String message, int length) {
        Toast.makeText(context,message, length).show();
    }


    public static Date stringToDate(String date,String time){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = df.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static Date stringToDate(String date){

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = null;
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    public static boolean compareDateWithCurrentTime(String date,String time){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date strDate = new Date();
        try {
            strDate = sdf.parse(date+" "+time);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeCheck = new Date().getTime();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        if (timeCheck >= strDate.getTime()) {
            return true;
        }
        return false;
    }

    public static long differenceFromCurrentDate(String date,String time){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date oldDate = new Date();
        try {
            oldDate = sdf.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date currentDate = new Date();

        long diff = oldDate.getTime()-currentDate.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;


        return diff;
    }

    public static long dateDifferenceInMin(Date date, Date date1){
        long diffmin = 0;
        try {


            SimpleDateFormat crunchifyFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object
            long diff = date1.getTime()-date.getTime();

            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

            int diffhours = (int) (diff / (60 * 60 * 1000));

            diffmin = (int) (diff / (60 * 1000));

            int diffsec = (int) (diff / (1000));


        } catch (Exception e) {
            e.printStackTrace();
        }


        return diffmin;
    }

}
