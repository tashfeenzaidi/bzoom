package com.example.bzoom;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
public class ChaufferEndTime extends AppCompatActivity {

    Calendar date;
    TextView time;
    Context context = this;
    String TAG;
    TextView ampm;
    Button select_end_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        time = (TextView) findViewById(R.id.time);
        ampm = (TextView) findViewById(R.id.ampm);
        ampm.setVisibility(View.GONE);
        select_end_time = (Button) findViewById(R.id.next);
        select_end_time.setText("Confirm end time");
        showDateTimePicker();
    }

    @Override
    protected void onStart() {
        super.onStart();
        select_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChaufferEndTime.this,SelectOwnerListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
//        new TimePickerDialog(context,R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                date.set(Calendar.MINUTE, minute);
//                Log.v(TAG, "The choosen one " + date.getTime());
//                time.setText(Date(date));
//
//            }
//        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

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
    public String Date (Calendar date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formatted = dateFormat.format(date.getTime());
        return formatted;
    }
}
