package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.rider.Rider;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TripEnd extends AppCompatActivity {

    Button ratenow;
    TextView erned;
    TextView startDate;
    TextView endDate;
    TextView source;
    private TextView destinaton;
    private TextView totaltime;
    private TextView endRide;
    private TextView startRide;
    private boolean isEist;
    private MapUtility mapUtility;
    Keystore keystore;
    String role;
    String startAtTime;
    String startAtDate;
    String endAtTime;
    String endAtDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_ended);

        ratenow =  findViewById(R.id.next);
        erned = findViewById(R.id.erned);
        startDate = findViewById(R.id.startdate);
        endDate = findViewById(R.id.enddate);
        source = findViewById(R.id.source);
        destinaton = findViewById(R.id.destinaton);
        totaltime = findViewById(R.id.totaltime);
        endRide = findViewById(R.id.number1);
        startRide = findViewById(R.id.number);
        keystore = Keystore.getInstance(this);
        role = keystore.get("role");
        if (role.equals("chauffeur")){
            AvailableCar.status = "rideEnd";
        }
        mapUtility = new MapUtility(this);
        populateTripEnd();

    }

    public void populateTripEnd(){

        if(getIntent() != null){
            if (getIntent().getStringExtra("activity").equals("message")){
                fillData(Ride.getStartDate(),Ride.getEndDate(),Ride.getLat(),Ride.getLon(),Ride.getLatDrop(),Ride.getLonDrop());
                return;
            }
        }

        try {
            totaltime.setText(Utilities.stringToDate(Ride.jsonObject.getString("ride_end_ttime")).compareTo(Utilities.stringToDate(Ride.jsonObject.getString("ride_start_time")))+" min");
            startRide.setText(dateTimeFormat(Ride.jsonObject.getString("ride_start_time"),"hh:mm"));
            startDate.setText(dateTimeFormat(Ride.jsonObject.getString("ride_start_time"),"MMM dd"));
            endRide.setText(dateTimeFormat(Ride.jsonObject.getString("ride_end_ttime"),"hh:mm"));
            endDate.setText(dateTimeFormat(Ride.jsonObject.getString("ride_end_ttime"),"MMM dd"));
            source.setText(mapUtility.getCompleteAddress(Ride.jsonObject.getDouble("pick_latitude"), Ride.jsonObject.getDouble("pick_longitude")));
            destinaton.setText(mapUtility.getCompleteAddress(Ride.jsonObject.getDouble("drop_latitude"), Ride.jsonObject.getDouble("drop_longitude")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        erned.setText(Ride.getAmountCollected());

        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TripEnd.this);
                LayoutInflater li = LayoutInflater.from(TripEnd.this);
                View promptsView = li.inflate(R.layout.submit_rating, null);
                builder.setView(promptsView);
                TextView money = promptsView.findViewById(R.id.money);
                money.setVisibility(View.GONE);
                ImageView wallet =  promptsView.findViewById(R.id.wallet);
                wallet.setVisibility(View.GONE);
                TextView number = promptsView.findViewById(R.id.number);
                number.setVisibility(View.GONE);
                ImageView distence =  promptsView.findViewById(R.id.distence);
                distence.setVisibility(View.GONE);
                RatingBar ratingBar = promptsView.findViewById(R.id.ratingBar);
                ImageView profilePic = promptsView.findViewById(R.id.image_rv);
                TextView name= promptsView.findViewById(R.id.name);
                TextView line= promptsView.findViewById(R.id.textView);
                EditText comment = profilePic.findViewById(R.id.editText);
                Rider.setRatingBar(ratingBar.getRating());
                name.setText(Rider.getName());
                line.setText("How was your ride?");
                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();

                Button submit =  promptsView.findViewById(R.id.next);
                submit.setOnClickListener(v1 -> {

                    if (comment != null){
                        Rider.setComment(String.valueOf(comment.getText()));
                    }else {
                        Rider.setComment("null");
                    }

                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            isEist =  RetrofitClass.normalRideRating("/ride-rating");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            if (isEist){

                                if (AvailableCar.status.equals("timeEnd") && role.equals("chauffeur")){
                                    Intent intent = new Intent(TripEnd.this,CollectTheCarActivity.class);
                                    startActivity(intent);
                                    TripEnd.this.finish();
                                }else {

                                    Intent intent = new Intent(TripEnd.this,MainActivity.class);
                                    startActivity(intent);
                                    TripEnd.this.finish();
                                }
                            }
                        }
                    }.execute();
                });
            }
        });
    }

    public String dateTimeFormat(String date,String format){

        String finalDateString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(date);
            SimpleDateFormat sdfnewformat = new SimpleDateFormat(format);
            finalDateString = sdfnewformat.format(convertedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateString;
    }

    public void fillData(String startAtDate,String endAtDate,String pickLat,String pickLon,String dropLat,String dropLon){
        startRide.setText(dateTimeFormat(startAtDate,"hh:mm"));
        startDate.setText(dateTimeFormat(startAtDate,"MMM dd"));
        endRide.setText(dateTimeFormat(endAtDate,"hh:mm"));
        endDate.setText(dateTimeFormat(endAtDate,"MMM dd"));
        source.setText(mapUtility.getCompleteAddressString(Double.valueOf(pickLat),Double.valueOf(pickLon)));
        totaltime.setText(Utilities.stringToDate(startAtDate).compareTo(Utilities.stringToDate(endAtDate))+" min");
        destinaton.setText(mapUtility.getCompleteAddressString(Double.valueOf(dropLat),Double.valueOf(dropLon)));
    }
}
