package com.example.bzoom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);




        heading = (TextView) findViewById(R.id.enter_number);
        time = (TextView) findViewById(R.id.time);
        calendar = Calendar.getInstance();
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        select_time = (Button) findViewById(R.id.next);


        ampm = (TextView) findViewById(R.id.ampm);
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
        String date_n = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
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

         activity = getIntent().getStringExtra("activity");

            if (activity != null){
                if (activity.equals("rent")){
                    showDateTimePicker();
                    heading.setText("For how long?");
                    imageView.setImageResource(R.drawable.calender);
                }
            }
        else {
            showDateTimePicker();
        }

        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if (activity != null){

                        if (activity.equals("rent")){
                            Intent intent = new Intent(SelectTimeActivity.this,ConfirmDetail.class);
                            intent.putExtra("from","rent");
                            startActivity(intent);
                        }


//                        if(heading.getText().equals("For how long?")){
//                            Intent intent = new Intent(SelectTimeActivity.this,ConfirmDetail.class);
//                            intent.putExtra("from","rent");
//                            startActivity(intent);
//                        }else {
//                            heading.setText("For how long?");
//                        }
                    }else {
                    Intent intent = new Intent(SelectTimeActivity.this,RequestRide.class);
                    startActivity(intent);
                }


            }
        });


    }

    public String Date (Calendar date){
        //long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formatted = dateFormat.format(date.getTime());
        return formatted;
    }


    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(context,R.style.TimePickerTheme, new DatePickerDialog.OnDateSetListener() {
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
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}

