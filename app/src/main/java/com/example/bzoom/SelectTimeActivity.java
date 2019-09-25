package com.example.bzoom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.GoogleDirectionApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectTimeActivity extends AppCompatActivity {


    Calendar calendar;
    int currentHour;
    int currentMinute;
    TextView ampm;
    TextView time;
    Context context = this;
    String TAG;
    Calendar date;
    String activity;
    TextView heading;
    ImageView imageView;
    Button select_time;
    boolean is_intent_exist;
    Calendar currentDate;
     String startDate;
    String endDate;
    String startTime;
    String endTime;
    boolean start;
    public static String onMonth;
    public static String onDay;
    public static String tillDay;
    public static String tillMonth;
    public static String arrivalTime;
    public static String arrivalAmPM;
    boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        start = false;

        heading =  findViewById(R.id.enter_number);
        time =  findViewById(R.id.time);
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        imageView =  findViewById(R.id.imageView2);
        select_time =  findViewById(R.id.next);
        ampm =  findViewById(R.id.ampm);

        activity = getIntent().getStringExtra("activity");

        ampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ampm.getText().equals("AM")){
                    ampm.setText("PM");
                }else {
                    ampm.setText("AM");
                }
            }
        });
        ampm.setVisibility(View.GONE);

        //create a date string.
        String date_n = new SimpleDateFormat("MMM dd, hh:mm aaa", Locale.getDefault()).format(new Date());
        time.setText(date_n);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateTimePicker();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        // if intent is coming from rider pickndrop it change the heading text



            if (activity != null){
                if (activity.equals("rent")){
                    showDateTimePicker();
                    heading.setText("For how long?");
                    imageView.setImageResource(R.drawable.calender);
                }else if (activity.equals(getString(R.string.chauffeur))){
                    showDateTimePicker();
                    heading.setText("Confirm end time");
                }else if (activity.equals("owner")){
                    showDateTimePicker();
                    heading.setText("Confirm end time");
                }
            }else {
            showDateTimePicker();
            }

        select_time.setOnClickListener(v -> {



            if (activity != null){

                if (activity.equals("rent") || activity.equals("owner")){

                    if (endDate == null || endTime == null){
                        return;
                    }
                    Ride.setStartDate(startDate);
                    Ride.setEndDate(endDate);
                    Ride.setStartTime(startTime);
                    Ride.setEndTime(endTime);

                    if (activity.equals("rent")){
                        long min = Utilities.dateDifferenceInMin(Utilities.stringToDate(startDate,startTime),Utilities.stringToDate(endDate,endTime));
                        new AsyncTask<Void,Void,Void>(){
                            @Override
                            protected Void doInBackground(Void... voids) {

                                isExist = RetrofitClass.getDirection(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()), Double.valueOf(Ride.getLatDrop()), Double.valueOf(Ride.getLonDrop()));

                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);

                                if (isExist){
                                    new AsyncTask<Void,Void,Void>(){
                                        @Override
                                        protected Void doInBackground(Void... voids) {

                                            isExist = RetrofitClass.getEstimation("/ride_estimation", GoogleDirectionApi.getDistance(),String.valueOf(min),Ride.getVehicalId(),Ride.isIsCab());

                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);

                                            if (isExist){
                                                Intent intent = new Intent(SelectTimeActivity.this,ConfirmDetail.class);
                                                intent.putExtra("from","rent");
                                                startActivity(intent);
                                            }

                                        }
                                    }.execute();
                                }

                            }
                        }.execute();


                    }else if (activity.equals("owner")){
                        Intent intent = new Intent(SelectTimeActivity.this,RequestRide.class);
                        startActivity(intent);
                    }

                }else if (activity.equals(getString(R.string.chauffeur))){
                    Ride.setEndDate(endDate);
                    Ride.setEndTime(endTime);
                    Intent intent = new Intent(SelectTimeActivity.this,SelectOwnerListActivity.class);
                    startActivity(intent);
                }

            }else {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        start = false;
    }

    public String Date (Calendar date, String format){
        //long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        String formatted = dateFormat.format(date.getTime());

        return formatted;
    }

    public Calendar showDateTimePicker() {
        currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,R.style.TimePickerTheme,datePickerListener,
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        /*new DatePickerDialog(context,R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(context,R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.v(TAG, "The choosen one " + date.getTime());
                        time.setText(Date(date));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();*/

        return date;
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
            date.set(selectedYear, selectedMonth, selectedDay);
            if (activity.equals("rent") || activity.equals("owner")){
                if(start){
                    endDate = selectedYear +"-"+ (selectedMonth+1) +"-"+ selectedDay;
                }else {
                    startDate = selectedYear +"-"+ (selectedMonth+1) +"-"+ selectedDay;
                }
            }else if (activity.equals(getString(R.string.chauffeur))){
                endDate = selectedYear +"-"+ (selectedMonth+1) +"-"+ selectedDay;
            }

            new TimePickerDialog(context,R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (activity.equals("rent") || activity.equals("owner")){
                        if(start){
                            endTime = hourOfDay+":"+minute+":00";
                        }else {

                            startTime = hourOfDay+":"+minute+":00";
                            arrivalTime = hourOfDay+":"+minute;
                        }
                    }else if (activity.equals(getString(R.string.chauffeur))){
                        endTime = hourOfDay+":"+minute+":00";
                    }

                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    date.set(Calendar.MINUTE, minute);
                    Log.v(TAG, "The choosen one " + date.getTime());
                    time.setText(Date(date,"MMM dd EEE, hh:mm aaa"));
                    if (activity.equals("rent")|| activity.equals("owner")){
                        if(start){
                            tillMonth = Date(date,"MMM dd");
                            tillDay   = Date(date,"EEE");
                        }else {
                            onMonth = Date(date,"MMM dd");
                            onDay   = Date(date,"EEE");
                            arrivalAmPM = Date(date,"aaa");
                        }
                        start = true;
                    }
                }
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }
    };

}

