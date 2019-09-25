package com.example.bzoom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.driver.Driver;
import com.example.bzoom.modules.map.driver.Vehical;
import com.google.android.gms.maps.model.LatLng;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;

import org.json.JSONException;

public class DriverConnected extends AppCompatActivity {

    TextView counter;
    Button cancel;
    Button contact;
    TextView name;
    RatingBar ratingBar;
    ImageView profilePic;
    ImageView carImage;
    TextView carDetails;
    TextView price;
    TextView location;
    TextView rating;
    MapUtility mapUtility;
    String locationPickup;
    private double distence;
    private CountDownTimer countDownTimer;
    private boolean isExist;
    private int status;
    private String bodyTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_connected);

        rating =  findViewById(R.id.textView3);
        contact =  findViewById(R.id.contact);
        cancel =  findViewById(R.id.cancel);
        name =  findViewById(R.id.name);
        ratingBar =  findViewById(R.id.ratingBar);
        profilePic   =  findViewById(R.id.image_rv);
        price =  findViewById(R.id.price);
        carDetails =  findViewById(R.id.txtph);
        carImage   =  findViewById(R.id.car_img);
        location =  findViewById(R.id.location);
        counter =  findViewById(R.id.counter);
        mapUtility = new MapUtility(this);
        setCounter(30000);
        status = 40;
        bodyTitle = getString(R.string.rider_cancel_ride);
        loadFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+"+92"+Driver.getNumber()));
                startActivity(intent);
            }
        });

        double rate = Driver.getDriverRating();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {

                       isExist =  RetrofitClass.rideCancel("/ride-cancel",status, bodyTitle);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (isExist){
                            Intent intent = new Intent(DriverConnected.this,MainActivity.class);
                            startActivity(intent);
                            DriverConnected.this.finish();
                        }

                    }
                }.execute();

            }
        });
        name.setText(Driver.getName());
        ratingBar.setRating((float) rate);
        rating.setText(String.valueOf(rate));
        try {
            locationPickup = mapUtility.getCompleteAddressString(Ride.jsonObject.getDouble("pick_latitude"),Ride.jsonObject.getDouble("pick_longitude"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        location.setText(locationPickup);

        carDetails.setText(Vehical.modal+" "+Vehical.name+Vehical.color+" color "+Vehical.numbre);

//        new AsyncTask<Void,Void,Void>(){
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//            }
//        }.execute();
        Glide.with(this)
                .load(Vehical.image)
                .override(300, 200)
                .fitCenter()
                .into(carImage);
//
//        Glide.with(this)
//                .load("https://www.tutorialspoint.com/images/tp-logo-diamond.png")
//                .override(100, 200)
//                .fitCenter()
//                .into(profilePic);

        distence = MapUtility.distanceBetweenTwoPoint(Double.valueOf(Ride.getLat()),Double.valueOf(Ride.getLon()),Double.valueOf(Ride.getLatDrop()),Double.valueOf(Ride.getLatDrop()));
        price.setText(String.valueOf((int)Ride.getFair()));

    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Main()).commit();
    }

    private void setCounter(long time){
        countDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //counter.setText(millisUntilFinished / 1000+" s");
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                counter.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                counter.setText(" ");
                status = 41;
            }
        }.start();
    }
}
