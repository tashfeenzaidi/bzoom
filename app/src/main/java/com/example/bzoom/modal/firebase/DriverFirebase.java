package com.example.bzoom.modal.firebase;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bzoom.R;
import com.example.bzoom.modules.map.driver.Driver;
import com.example.bzoom.modules.map.rider.Rider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.List;

public class DriverFirebase {

    private Firebase database;
    private DatabaseReference driverRef;
    private String catType = "carType";
    private String rideStatus = "rideStatus";
    private String status = "pending";
    private int carTypeId = 1;

    public List<Rider> getRiderList() {
        return riderList;
    }

    List<Rider> riderList;
    public DriverFirebase() {
        database = new Firebase();
        driverRef = database.getmDriver();
        riderList = new ArrayList<>();
    }

    public void addDriver(Driver driver){
        DatabaseReference databaseReference = database.getmDriver();
        databaseReference.child(databaseReference.push().getKey()).setValue(driver);
    }


    public void getRiderByCarTypeOnNew() {
        DatabaseReference riderRef = database.getmRider();
        Query query = riderRef.orderByChild(rideStatus).equalTo(status);
        query.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()) {
                    datasnapshot(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getRiderByCarType(){
        DatabaseReference riderRef= database.getmRider();
        Query query = riderRef.orderByChild(rideStatus).equalTo(rideStatus);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    datasnapshot(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void datasnapshot(DataSnapshot dataSnapshot){

        Rider rider =  dataSnapshot.getValue(Rider.class);
        if(rider.getCarType() == carTypeId){
            riderList.add(rider);
        }
    }
}
