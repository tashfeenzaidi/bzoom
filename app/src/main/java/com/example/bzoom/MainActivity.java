package com.example.bzoom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.service.dreams.DreamService;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.DriverFirebase;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.driver.Driver;
import com.example.bzoom.modules.map.rider.Rider;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<Car> cars;
    TextView heading;
    Switch active;
    Keystore keystore;
    MapUtility mapUtility;
    public static Rider currentRide;
    boolean rideStatud;
    double distence;
     List<Rider> riderList;
    public static Location currentLocation;
    private final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private String catType = "carType";
    private String rideStatus = "rideStatus";
    private String status = "finding";
    String token;
    String uid ;
    String userId;
    ImageView profileImage;
    TextView name;
    TextView editprofile;
    private List<CarType> carTypes;
    boolean isExist;
    public static boolean cancel;
    TextView counter;
    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLocationPermission();
        carTypes = new ArrayList<>();
        riderList = new ArrayList<>();
        keystore = Keystore.getInstance(MainActivity.this);
        token = keystore.get("userToken");
        uid = keystore.get("UID");
        userId = keystore.get("userId");
        int isSignup = keystore.getInt("signup");
        Firebase.subscribeToTopic(uid);
        General.setUserUid(uid);
        General.setUserid(userId);
        mapUtility = new MapUtility(this);
        mapUtility.getLocationPermission();
        cars = new ArrayList<>();
        heading =  findViewById(R.id.heading);
        active =  findViewById(R.id.active);
        counter = findViewById(R.id.counter);
        roleNativeLyout();


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        View view =  navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.profileImg);
        name =  view.findViewById(R.id.name);
        editprofile =  view.findViewById(R.id.editprofile);

        if (keystore.get("role").equals("driver")){

            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {

                    isExist =  RetrofitClass.getDriver("/get-driver-data",keystore.get("userId"));

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    if (isExist){
                        for (CarType carType: carTypes) {

                            try {
                                if (carType.getId() == Driver.driver.getInt("vt_id")){
                                    CarType.activeCarId = carType.getId();
                                    Firebase.subscribeToTopic(carType.getName());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }
            }.execute();
        }

        Glide.with(MainActivity.this)
                .load(Uri.parse("file:///android_asset/"+"http://i.imgur.com/DvpvklR.png"))//"https://www.google.com/search?q=profile+picture&tbm=isch&source=iu&ictx=1&fir=weAN_07uzPRPBM%253A%252CYqmgipjgMd5qHM%252C_&vet=1&usg=AI4_-kQf7Dd8PycQWXlgosbfWs5qqSFbVw&sa=X&ved=2ahUKEwiIwPS7m_PjAhUyt3EKHf_3AbYQ9QEwAHoECAcQBA#imgrc=weAN_07uzPRPBM:")
                .into(profileImage);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            if (keystore.get("role").equals("owner")) {
                Role.role = "owner";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
            if (keystore.get("role").equals("driver")) {
                Role.role = "driver";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Rider_Home_Fragment()).commit();
            }
            if (keystore.get("role").equals("rider")) {
                Role.role = "rider";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RiderSetPicknDrop()).commit();
            }
            if (keystore.get("role").equals("chauffeur") && !Role.status) {
                Role.role = "chauffeur";
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChaufferMainFragment()).commit();
            }
        }

        getLastLocationtest();
    }

    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                            onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Driver.driverLat = location.getLatitude();
        Driver.driverLon = location.getLongitude();
        currentLocation = location;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocationtest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(65000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //
                        onLocationChanged(location);
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        name.setText(keystore.get("name"));
        editprofile.setText(keystore.get("email"));

        if(active.isChecked()){
            getLocationPermission();
            if (keystore.get("role").equals("driver")){
               Firebase.subscribeToTopic(String.valueOf(R.string.topic_driver));
                activateUser();
            }else if (keystore.get("role").equals("chauffeur")){
                Firebase.subscribeToTopic(String.valueOf(R.string.topic_chauffeur));
            }
            //getRiderByCarTypeOnNew();
        }

        active.setOnClickListener(v -> {
            if(active.isChecked()){
                getLocationPermission();
                activateUser();
                //getRiderByCarTypeOnNew();
            }else{
                dActiveUser();
            }
        });

//        if(keystore.get("role").equals("chauffeur") && Role.status == true){
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            },1000L);
//
//        }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Bzoom");
            alertDialog.setMessage("Are you sure.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            keystore.putInt("login_status",0);
                            clearAndExit(MainActivity.this);
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

//        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void clearAndExit(Context ctx) {
        if (!(ctx instanceof MainActivity)) {
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putBoolean("exit", true);
            intent.putExtras(bundle);
            ctx.startActivity(intent);
        } else {
            ((Activity) ctx).finish();
        }
    }

    public void roleNativeLyout(){
        if (Role.role != null){
            if(keystore.get("role").equals("driver")){
                heading.setText("Home");
                if(keystore.get(getString(R.string.driver_status)) != null && keystore.get("driverStatus").equals("active")){
                    active.setChecked(true);
                }
            }else if(keystore.get("role").equals("owner") && keystore.get("role") != null){
                active.setVisibility(View.GONE);
            }else if(keystore.get("role").equals("rider")){
                active.setVisibility(View.GONE);
                heading.setText("Bzoom");
            }else if(keystore.get("role").equals("chauffeur")){
                active.setVisibility(View.GONE);
                if(Role.status){
                    heading.setText("Start rides");
                    counter.setVisibility(View.VISIBLE);
                    setCounter();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new Rider_Home_Fragment()).commit();
                }else {
                    heading.setText("Bzoom");
                }
            }
        }
    }


    private void setCounter(){

       long difference = Utilities.differenceFromCurrentDate(AvailableCar.activeEndDate,AvailableCar.activeEndTime);
        countDownTimer = new CountDownTimer(difference,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
//                int seconds = (int) (millisUntilFinished / 1000);
//                int minutes = seconds / 60;
//                int hours = minutes/60;
//                seconds = seconds % 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
                counter.setText(String.format("%02d", hours)
                        + ":" +String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                counter.setText("Time end");
                Role.status = false;
                if (AvailableCar.status.equals("rideEnd")){
                    Intent intent = new Intent(MainActivity.this,CollectTheCarActivity.class);
                    startActivity(intent);
                }

            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void dActiveUser(){
        Firebase.unsubscribeFromTopic(uid);

        if(keystore.get(getString(R.string.role)).equals(getString(R.string.user_driver))){
            keystore.putString(getString(R.string.driver_status),getString(R.string.dactive_user));
            Firebase.unsubscribeFromTopic(getString(R.string.topic_driver));
            Firebase.unsubscribeFromTopic(CarType.activeCarTopicName);
        }else if (keystore.get(getString(R.string.role)).equals(getString(R.string.user_chauffeur))){
            keystore.putString(getString(R.string.user_chauffeur),getString(R.string.dactive_user));
            Firebase.unsubscribeFromTopic(getString(R.string.topic_chauffeur));
        }
    }

    public void activateUser(){

        Firebase.subscribeToTopic(uid);

        if(keystore.get(getString(R.string.role)).equals(getString(R.string.user_driver))){
            keystore.putString(getString(R.string.driver_status),getString(R.string.active_user));
//         Get vehical types
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {

                    carTypes = RetrofitClass.getRest(getString(R.string.getvahicaltype)+1);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (carTypes.size() != 0){
                        for (CarType carType: carTypes) {

                            try {
                                if (Driver.driver != null){
                                    if (carType.getId() == Driver.driver.getInt("vt_id")){
                                        Firebase.subscribeToTopic(carType.getName());
                                        CarType.activeCarTopicName = carType.getName();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }.execute();

            Firebase.subscribeToTopic("driver");


        }else if (keystore.get(getString(R.string.role)).equals(getString(R.string.user_chauffeur))){
            keystore.putString(getString(R.string.user_chauffeur),getString(R.string.active_user));
            Firebase.subscribeToTopic(getString(R.string.topic_chauffeur));
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

}
