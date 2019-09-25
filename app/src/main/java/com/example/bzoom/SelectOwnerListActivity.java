package com.example.bzoom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.chauffeur.ChauffeurTimeFinish;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectOwnerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<AvailableCar> ownerArrayList;
    DatabaseReference databaseReference;
    Keystore keystore;
    private int range=5000;
    private int endLimit=15000;
    private double lat;
    private double lon;
    private RelativeLayout relativelayout;
    private ProgressBar progressbar;
    private RelativeLayout.LayoutParams layoutparams;
    private boolean isExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_owner_list);
        ownerArrayList = new ArrayList<>();
        keystore =Keystore.getInstance(this);
        keystore.putString("rideStatus",null);
        Firebase firebase = new Firebase();
        databaseReference = firebase.getmAvailable();
        lat= MainActivity.currentLocation.getLatitude();
        lon= MainActivity.currentLocation.getLongitude();

        relativelayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        // Creating progress bar via coding method.
        progressbar = new ProgressBar(SelectOwnerListActivity.this, null, android.R.attr.progressBarStyle);

        layoutparams = new RelativeLayout.LayoutParams(450, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutparams.addRule(RelativeLayout.CENTER_IN_PARENT);

        progressbar.setLayoutParams(layoutparams);

        relativelayout.addView(progressbar);

        progressbar.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        String type = Ride.getVehicalId();
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Query query = databaseReference.orderByChild("vehical_type").equalTo(Integer.valueOf(type));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ownerArrayList.clear();
                        ownerList(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return null;
            }
        }.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ownerArrayList.isEmpty()){
            ownerArrayList.clear();
        }
    }

    public void ownerList(DataSnapshot dataSnapshot){

        ArrayList<AvailableCar> availableCars = new ArrayList<>();
        for (DataSnapshot owner:dataSnapshot.getChildren()) {

            AvailableCar car = new AvailableCar();
            car.setName(owner.child("owner_name").getValue(String.class));
            car.setColor(owner.child("color").getValue(String.class));
            car.setLat(Double.valueOf(owner.child("lat").getValue(String.class)) );
            car.setLon(Double.valueOf(owner.child("long").getValue(String.class)));
            car.setModal(owner.child("model").getValue(String.class));
            car.setOwnerId(owner.child("owner_id").getValue(Integer.class));
            car.setAvailableId(owner.child("available_id").getValue(Integer.class));
            car.setNumber(owner.child("number").getValue(String.class));
            car.setRating(Double.valueOf(owner.child("rating").getValue(String.class)));
            car.setEndDate(owner.child("end_date").getValue(String.class));
            car.setEndTime(owner.child("end_time").getValue(String.class));
            car.setStartTime(owner.child("start_time").getValue(String.class));
            car.setStartDate(owner.child("start_date").getValue(String.class));
            car.setPlateNumber(owner.child("plate_number").getValue(String.class));
            car.setVehicalId(Integer.valueOf(owner.child("vehical_id").getValue(String.class)));

             availableCars.add(car);
        }
        populateCarList(availableCars);
    }

    // Recursive function
    public void populateCarList(ArrayList<AvailableCar> car){


        if (!ownerArrayList.isEmpty()){
            recyclerView = findViewById(R.id.owner_recycler);
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
            OwnerRecyclerViewAddapter ownerrecyclerviewaddapter= new OwnerRecyclerViewAddapter(ownerArrayList,this);
            recyclerView.setAdapter(ownerrecyclerviewaddapter);
            progressbar.setVisibility(View.INVISIBLE);
            return;
        }

        for (AvailableCar car1:car) {
            double distance = MapUtility.distanceBetweenTwoPoint(lat,lon,car1.getLat(),car1.getLon());
            //  0 comes when two date are same,
            //  1 comes when date1 is higher then date2
            // -1 comes when date1 is lower then date2
            String date1=Ride.getEndDate();
            String time1=Ride.getEndTime();
            int endTimeCheck=Utilities.stringToDate(car1.getEndDate(),car1.getEndTime()).compareTo(Utilities.stringToDate(Ride.getEndDate(),Ride.getEndTime()));
            int endtime =  Utilities.stringToDate(Ride.getEndDate(),Ride.getEndTime()).compareTo(Utilities.stringToDate(car1.getEndDate(),car1.getEndTime()));
            boolean startTimeCheck = Utilities.compareDateWithCurrentTime(car1.getStartDate(),car1.getStartTime());
            if (startTimeCheck && (endTimeCheck == -1 || endtime == 0) && distance<range)
            {
                ownerArrayList.add(car1);
            }

        }

        if (!car.isEmpty() && range < endLimit){
            range = range+1000;
            populateCarList(car);
        }
    }


}
