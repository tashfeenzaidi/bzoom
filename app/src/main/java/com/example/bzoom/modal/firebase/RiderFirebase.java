package com.example.bzoom.modal.firebase;

import com.example.bzoom.modules.map.rider.Rider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RiderFirebase  {

    public final DatabaseReference mDatabase  = FirebaseDatabase.getInstance().getReference();
    public final DatabaseReference mrider  = mDatabase.child("rider");


    public void activateRider(Rider rider){

        mrider.child(mrider.push().getKey()).setValue(rider);

    }





}
