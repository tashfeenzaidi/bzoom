package com.example.bzoom;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.GoogleDirectionApi;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.rider.Rider;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PickupRide extends AppCompatActivity {

    TextView heading;
    Button arrived;
    Button cancel;
    TextView counter;
    TextView name;
    RatingBar ratingBar;
    TextView number;
    TextView time;

    TextView location;
    ImageView back;
    ImageView car;
    ImageView profilePic;
    MapUtility utility;
    private MapFragment mMapFragment;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private double distance;
    private double distence;
    CountDownTimer countDownTimer;
    private TextView rating;
    boolean isExist;
    Keystore keystore;
    private int status;
    private String bodyTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_the_car);
        status = 35;
        bodyTitle = getString(R.string.driver_cancel_ride);
        keystore = Keystore.getInstance(this);
        statusCheck();
        loadFragment();
        utility = new MapUtility(this);
        String address = null;
            address = utility.getCompleteAddressString(
                    Double.valueOf(Ride.getLat()),
                    Double.valueOf(Ride.getLon()));

        //Distance b/w two points and rate approx
        distence = MapUtility.distanceBetweenTwoPoint(Double.valueOf(Ride.getLat()),Double.valueOf(Ride.getLon()),Double.valueOf(Ride.getLatDrop()),Double.valueOf(Ride.getLatDrop()));
        Ride.setFair(MapUtility.calRate(distence));

        rating = findViewById(R.id.textView3);
        location = findViewById(R.id.location);
        location.setText(address);
        counter =  findViewById(R.id.counter);
        ratingBar = findViewById(R.id.ratingBar);
        back =  findViewById(R.id.imageView3);
        back.setVisibility(View.GONE);
        car =  findViewById(R.id.textView2);
        car.setVisibility(View.GONE);
        heading =  findViewById(R.id.heading);
        heading.setText("Pickup Ride");
        arrived =  findViewById(R.id.startride);
        cancel =  findViewById(R.id.cancel);
        arrived.setText("On the way");
        time =  findViewById(R.id.time);
        name=  findViewById(R.id.name);
        number=  findViewById(R.id.txtph);

        profilePic =  findViewById(R.id.image_rv);

        setCounter(30000);
        General.setUserid(keystore.get("userId"));


    }

    @Override
    protected void onStart() {
        super.onStart();
        float rate = Rider.getRatingBar();
        ratingBar.setRating(rate);
        rating.setText(String.valueOf(rate));
        name.setText(Rider.getName());
        number.setText("0"+Rider.getNumber());

//        try {
//             distance = MapUtility.distanceBetweenTwoPoint(
//                    Ride.jsonObject.getDouble("pick_latitude"),
//                    Ride.jsonObject.getDouble("pick_longitude"),
//                    Ride.jsonObject.getDouble("drop_latitude"),
//                    Ride.jsonObject.getDouble("drop_latitude"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
        time.setText(GoogleDirectionApi.getDurationText()+"from here");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PickupRide.this);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PickupRide.this);
                        LayoutInflater li = LayoutInflater.from(PickupRide.this);
                        View promptsView = li.inflate(R.layout.cancel_reason, null);
                        final List<String> stringList=new ArrayList<>();  // here is list
//                        for(int i=0;i<5;i++) {
//                            stringList.add("RadioButton " + (i + 1));
//                        }
                        stringList.add("Customer misbehaving");
                        stringList.add("Tire puncher");
                        stringList.add("Having personal issues, I need to go home");
                        stringList.add("Having an accident ");
                        stringList.add("I feel not well");


                        RadioGroup rg = (RadioGroup) promptsView.findViewById(R.id.radio);

                        for(int i=0;i<stringList.size();i++){
                            RadioButton rb=new RadioButton(PickupRide.this); // dynamically creating RadioButton and adding to RadioGroup.
                            rb.setText(stringList.get(i));
                            rg.addView(rb);
                        }
                        Button ok =  promptsView.findViewById(R.id.ok);
                        Button cancel =  promptsView.findViewById(R.id.cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {

                                        isExist =  RetrofitClass.rideCancel("/ride-cancel", status, bodyTitle);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        if (isExist){
                                            Intent intent = new Intent(PickupRide.this,MainActivity.class);
                                            startActivity(intent);
                                            PickupRide.this.finish();
                                        }

                                    }
                                }.execute();
                            }
                        });

                        builder.setView(promptsView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(arrived.getText().equals("On the way")) {
                    countDownTimer.cancel();
                    status = 37;
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            RetrofitClass.driverOnTheWay("/driver-ride-on_my_way");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            arrived.setText("I have arrived");

                        }
                    }.execute();
                }else if(arrived.getText().equals("I have arrived")){
                    arrived.setText("Start ride");
                    setCounter(900000);
                    status = 38;

                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            RetrofitClass.driverArrived("/driver-ride-arrived ");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            arrived.setText("Start ride");


                        }
                    }.execute();
                }else {
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            RetrofitClass.driverRideStart("/driver-ride-start");

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            arrived.setText("Start ride");
                            Intent intent = new Intent(PickupRide.this,NavigationActivity.class);
                            startActivity(intent);
                        }
                    }.execute();

                }
//                if(arrived.getText().equals("Start ride")){
//
//                }

            }
        });

    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Main()).commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
                if (status == 35){
                    status = 36;
                }else if (status == 38){
                    status = 39;
                }
            }
        }.start();
    }
}
