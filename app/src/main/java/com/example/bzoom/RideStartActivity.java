package com.example.bzoom;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.chauffeur.Chauffeur;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RideStartActivity extends AppCompatActivity {
    private GoogleMap mMap;
    Context context;
    TextView nav_headng;
    TextView sub_headng;
    TextView address;
    Button view_profile;
    private boolean isEisxt;
    Keystore keystore;
    MapUtility mapUtility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_start);

        context = this;

        keystore = Keystore.getInstance(this);
        view_profile = findViewById( R.id.view_peofile );


        ImageView back =  findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mapUtility = new MapUtility(this);

        loadFragment();



        address = findViewById(R.id.textView4);
        nav_headng =  findViewById(R.id.textView7);
        nav_headng.setText("Chauffer on the way");

        sub_headng =  findViewById(R.id.textView8);
        sub_headng.setText("Required to reach here");

    }

    @Override
    protected void onStart() {
        super.onStart();


        double asd = Double.valueOf(Ride.getLat());
        double eer =Double.valueOf(Ride.getLon());
        double ded = Chauffeur.getLat();
        double wwe = Chauffeur.getLon();

        double time =  MapUtility.durationBetweenTwoPoint(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()),Chauffeur.getLat(),Chauffeur.getLon());
        address.setText(mapUtility.getCompleteAddress(Chauffeur.getLat(),Chauffeur.getLon()));

        nav_headng.setText((long) time +" mins");
        view_profile.setOnClickListener(v -> {
            General.setRole(keystore.get("role"));
            initializeUI();
        });
    }


    public void initializeUI(){

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                isEisxt = RetrofitClass.getProfile("/profile/", Chauffeur.getChauffeurId(),"chauffeur");
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (isEisxt){
                    Intent intent = new Intent(RideStartActivity.this,ChauffeurProfile.class);
                    startActivity(intent);
                }
            }
        }.execute();


    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Main()).commit();
    }

}
