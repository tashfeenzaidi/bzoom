package com.example.bzoom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RideStartActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Context context;
    TextView nav_headng;
    TextView sub_headng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_start);

        context = this;
         MapView mapView = findViewById(R.id.map);
        mapView.getMapAsync(this);


        //View myLayout = findViewById( R.id.buttom_ride_start ); // root View id from that link
        Button view_profile = findViewById( R.id.view_peofile );
        view_profile.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideStartActivity.this,ChauffeurProfile.class);
                startActivity(intent);
            }
        });

        ImageView back = (ImageView) findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nav_headng = (TextView) findViewById(R.id.heading);
        nav_headng.setText("Chauffer on the way");

        sub_headng = (TextView) findViewById(R.id.textView8);
        sub_headng.setText("required to reach here");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(24.800348, 67.045104);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

//                showDialog(RideStartActivity.this,R.string.dialog_message,"");
                AlertDialog.Builder builder = new AlertDialog.Builder(RideStartActivity.this);
                builder.setMessage(R.string.dialog_message)
                        ;
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        sub_headng.setText("remaining to complete a ride");
                        nav_headng.setText("Chauffer on the ride");

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(RideStartActivity.this,Belongins.class);
                                startActivity(intent);
                            }
                        },2000L);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }, 2000L);
    }

}
