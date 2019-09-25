package com.example.bzoom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.GoogleDistanceMatrix;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    Button endRide;
    boolean isExist;
    TextView addressView;
    Keystore keystore;
    String roleNumber;
    TextView time;
    TextView subHeading;
    String role;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        endRide =  findViewById(R.id.view_peofile);
        keystore = Keystore.getInstance(this);
        if(Role.role.equals("rider")){
            ImageView imageView =  findViewById(R.id.imageView3);
            removeView(imageView);
        }else {
            ImageView imageView =  findViewById(R.id.navigate);
            removeView(imageView);
        }
        addressView = findViewById(R.id.textView4);
        time = findViewById(R.id.textView7);
        subHeading = findViewById(R.id.textView8);
        endRide.setBackgroundResource(R.drawable.btn);
        endRide.setTextColor(Color.parseColor("#ffffff"));
        endRide.setText("End Ride");
        keystore.putString(getString(R.string.rid_status),getString(R.string.start));
        role = keystore.get("role");

        if (role.equals("rider")|| role.equals("driver") || keystore.get("role").equals("chauffeur")){
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {

                    isExist = RetrofitClass.getDistanceMatrix(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()),Double.valueOf(Ride.getLatDrop()),  Double.valueOf(Ride.getLonDrop()));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    if (isExist){
                        time.setText(GoogleDistanceMatrix.duration);

                    }
                }
            }.execute();
        }

        statusCheck();
        loadFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();




        if (keystore.get("role").equals("driver") || keystore.get("role").equals("chauffeur")){
            roleNumber = "6";
            subHeading.setText("Travelling time");
        }else if (keystore.get("role").equals("rider")){
            roleNumber = "3";
            subHeading.setText("Time to reach destination");
        }

            MapUtility mapUtility = new MapUtility(this);
        String address =    mapUtility.getCompleteAddress(Double.valueOf(Ride.getLatDrop()),Double.valueOf(Ride.getLonDrop()));
        addressView.setText(address);
        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    reason();
//                AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
//                builder.setMessage("Are you sure?");
//                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
//                        LayoutInflater li = LayoutInflater.from(NavigationActivity.this);
//                        View promptsView = li.inflate(R.layout.cancel_reason, null);
//                        final List<String> stringList=new ArrayList<>();  // here is list
//                        stringList.add("Customer misbehaving");
//                        stringList.add("Tire puncher");
//                        stringList.add("Having personal issues, I need to go home");
//                        stringList.add("Having an accident ");
//                        stringList.add("I feel not well");
//                        RadioGroup rg = (RadioGroup) promptsView.findViewById(R.id.radio);
//
//                        for(int i=0;i<stringList.size();i++){
//                            RadioButton rb=new RadioButton(NavigationActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
//                            rb.setText(stringList.get(i));
//                            rg.addView(rb);
//                        }
//                        Button ok =  promptsView.findViewById(R.id.ok);
//                        Button cancel =  promptsView.findViewById(R.id.cancel);
//
//                        ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(NavigationActivity.this,AmountCollected.class);
//                                startActivity(intent);
//                            }
//                        });
//
//                        builder.setView(promptsView);
//                        final AlertDialog alertDialog = builder.create();
//                        alertDialog.setCanceledOnTouchOutside(false);
//                        alertDialog.show();
//                        cancel.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                alertDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//
//
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
        });

    }

    public void removeView(View view){

        view.setVisibility(View.GONE);
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

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Main()).commit();
    }

    public void reason(){

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                isExist =  RetrofitClass.getReasons("/get-reasons","1",roleNumber);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isExist){

                    AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                            LayoutInflater li = LayoutInflater.from(NavigationActivity.this);
                            View promptsView = li.inflate(R.layout.cancel_reason, null);
                            RadioGroup rg =  promptsView.findViewById(R.id.radio);

                            for(int i=0;i<ReasonEndRide.reasonEndRides.length();i++){
                                try {
                                    JSONObject actor = ReasonEndRide.reasonEndRides.getJSONObject(i);
                                    RadioButton rb=new RadioButton(NavigationActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
                                    rb.setText(actor.getString("discription"));
                                    rb.setId(actor.getInt("id"));
                                    rg.addView(rb);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            Button ok =  promptsView.findViewById(R.id.ok);
                            Button cancel =  promptsView.findViewById(R.id.cancel);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    int reasonId = rg.getCheckedRadioButtonId();
                                    if (reasonId == -1 ){
                                        return;
                                    }
                                    ReasonEndRide.setId(reasonId);
                                    new AsyncTask<Void,Void,Void>() {
                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            isExist = RetrofitClass.getDirection( Double.valueOf(Ride.getLatDrop()),  Double.valueOf(Ride.getLonDrop()), Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            super.onPostExecute(aVoid);

                                            if (isExist){
                                                new AsyncTask<Void,Void,Void>(){
                                                    @Override
                                                    protected Void doInBackground(Void... voids) {

                                                        isExist = RetrofitClass.rideEnd("/ride-end");
                                                        return null;
                                                    }

                                                    @Override
                                                    protected void onPostExecute(Void aVoid) {
                                                        super.onPostExecute(aVoid);

                                                        if (isExist){
                                                            if (keystore.get("role").equals("rider")){
                                                                Intent intent = new Intent(NavigationActivity.this,TripEnd.class);
                                                                intent.putExtra("activity","navigation");
                                                                startActivity(intent);
                                                            }else {
                                                                new AsyncTask<Void,Void,Void>(){
                                                                    @Override
                                                                    protected Void doInBackground(Void... voids) {

                                                                        isExist = RetrofitClass.getAmount("/ride_amount",Ride.getRideID());
                                                                        return null;
                                                                    }

                                                                    @Override
                                                                    protected void onPostExecute(Void aVoid) {
                                                                        super.onPostExecute(aVoid);
                                                                        if (isExist){
                                                                            Intent intent = new Intent(NavigationActivity.this,AmountCollected.class);
                                                                            intent.putExtra("activity","navigation");
                                                                            startActivity(intent);
                                                                        }
                                                                    }
                                                                }.execute();

                                                            }

                                                        }else{

                                                        }
                                                    }
                                                }.execute();



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


            }
        }.execute();

    }
}
