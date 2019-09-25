package com.example.bzoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;


public class RequestAlertDialogActivity extends AppCompatActivity {

    Keystore keystore;
    boolean rideAccepted;
    TextView tripType;
    TextView time;
    TextView trip;
    private boolean isExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ride_req_popup);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        this.setFinishOnTouchOutside(false);


        keystore =  Keystore.getInstance(this);
        time = findViewById(R.id.time);
        trip = findViewById(R.id.trip);
        tripType = findViewById(R.id.trip_time);

        int distance =  (int)MapUtility.durationBetweenTwoPoint(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()),Double.valueOf(Ride.getLatDrop()), Double.valueOf(Ride.getLonDrop()));
        time.setText(String.valueOf(distance));

        keystore.putString(getString(R.string.rid_status),getString(R.string.start));
        Button accept = findViewById(R.id.next);

        if(distance < 20){
            trip.setText("small ride");
            tripType.setText("< 20 min");
        }else if(distance < 50){
            trip.setText("medium ride");
            tripType.setText("< 40 min");

        }else {
            trip.setText("long ride");
            tripType.setText("45+ min");

        }

        if (keystore.get("role").equals("chauffeur")){
            AvailableCar.status = "rideStart";
        }

        ProgressBar progressBar = findViewById(R.id.progress);
        ValueAnimator animator = ValueAnimator.ofInt(0, progressBar.getMax());
        animator.setDuration(30000);
        animator.addUpdateListener(animation ->
                progressBar.setProgress((Integer)animation.getAnimatedValue()));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // start your activity here

               RequestAlertDialogActivity.this.finish();
            }
        });
        animator.start();


        accept.setOnClickListener(this::onClick);
        loadFragment();
    }

    private void loadFragment() {

        getSupportFragmentManager().beginTransaction().replace(R.id.map,
                new Main()).commit();

    }

    private void onClick(View v) {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                isExist = RetrofitClass.getDirection(MainActivity.currentLocation.getLatitude(),MainActivity.currentLocation.getLongitude(),Double.valueOf(Ride.getLat()),Double.valueOf(Ride.getLon()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (isExist){
                    General.setRole(keystore.get("role"));

                    new AsyncTask<Void,Void,Void>(){

                        @Override
                        protected Void doInBackground(Void... voids) {

                            rideAccepted = RetrofitClass.driverRideAccept("/driver-ride-accept");
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            if (rideAccepted){
                                keystore.putString(getString(R.string.rid_status),getString(R.string.pending));
                                Firebase.subscribeToTopic(keystore.get("UID"));
                                finish();
                            }else {
                                Toast.makeText(RequestAlertDialogActivity.this,"Sorry!",Toast.LENGTH_LONG);
                                finish();
                            }

                        }
                    }.execute();
                }
            }
        }.execute();




//        Firebase.subscribeToTopic(keystore.get("UID"));
//        Intent intent = new Intent(RequestAlertDialogActivity.this, PickupRide.class);
//        startActivity(intent);
    }
}
