package com.example.bzoom;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bzoom.modules.map.Main;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PickupRide extends AppCompatActivity {

    TextView heading;
    Button arrived;
    Button cancel;
    TextView counter;
    ImageView back;
    ImageView car;
    private MapFragment mMapFragment;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_the_car);


        statusCheck();
        loadFragment();


        counter = (TextView) findViewById(R.id.counter);
        new CountDownTimer(900000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(millisUntilFinished / 1000+" s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                counter.setText(" ");
//                Intent intent = new Intent(PickupRide.this,NavigationActivity.class);
//                intent.putExtra("activity","driverconnected");
//                startActivity(intent);
            }

        }.start();
        back = (ImageView) findViewById(R.id.imageView3);
        back.setVisibility(View.GONE);
        car = (ImageView) findViewById(R.id.textView2);
        car.setVisibility(View.GONE);
        heading = (TextView) findViewById(R.id.heading);
        heading.setText("Pickup Ride");
        arrived = (Button) findViewById(R.id.startride);
        cancel = (Button) findViewById(R.id.cancel);
        arrived.setText("On the way");

    }

    @Override
    protected void onStart() {
        super.onStart();



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
                        for(int i=0;i<5;i++) {
                            stringList.add("RadioButton " + (i + 1));
                        }
                        RadioGroup rg = (RadioGroup) promptsView.findViewById(R.id.radio);

                        for(int i=0;i<stringList.size();i++){
                            RadioButton rb=new RadioButton(PickupRide.this); // dynamically creating RadioButton and adding to RadioGroup.
                            rb.setText(stringList.get(i));
                            rg.addView(rb);
                        }
                        Button ok = (Button) promptsView.findViewById(R.id.ok);
                        Button cancel = (Button) promptsView.findViewById(R.id.cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PickupRide.this,MainActivity.class);
                                startActivity(intent);
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
                    arrived.setText("I have arrived");
                }else if(arrived.getText().equals("I have arrived")){
                    arrived.setText("Start ride");
                }else {
                    arrived.setText("Start ride");
                    Intent intent = new Intent(PickupRide.this,NavigationActivity.class);
                    startActivity(intent);
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

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
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

}
