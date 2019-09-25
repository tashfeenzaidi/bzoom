package com.example.bzoom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.GoogleDirectionApi;
import com.example.bzoom.modules.map.MapUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class RiderSetPicknDrop extends Fragment implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener {

    private HomeFragment.OnFragmentInteractionListener mListener;
    View view_rent;
    View view_ride;
    Button ride;
    Button rent;
    public boolean check;
    EditText destination;
    EditText source;
    View div;
    Button bzoom;
    ImageView arrow;
    ImageView curr_location;
    ImageView sourceanddestination;
    RelativeLayout cardView;
    ImageButton micro;
    ImageButton bzoomplus;
    ImageButton xclass;
    CardView cab_card;
    CardView rent_card;
    private MapView mapView;
    private GoogleMap mMap;
    // device current location veriable
    LatLng latLng;
    View view;
    // update latlong after change the map position
    double updateLat;
    double updateLong;
    double sourceLat;
    double sourceLong;
    double destLat;
    double destLong;
    // Recycler view for car types
    List<CarType> carTypes;
    private RecyclerView recyclerView;
    // flag for return ride if true its a return ride or vise versa
    private boolean ride_type;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Location mLastKnownLocation;
    Location sourceLocation;
    Location destinationLocation;
    private static final int DEFAULT_ZOOM = 15;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MapUtility mapUtility;
    private int btn_flag;
    private Thread thread;
    String success;
    static Keystore store ;
    int carId;
    private Location currentLocation;
    private LocationRequest mLocationRequest;
    private boolean isExist;
    int type;
    boolean firstTimeLatLon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.cancel){
            androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Driver cancel the ride!");
        }
        type = 1;
        firstTimeLatLon = true;
        store = Keystore.getInstance(getActivity());
        carTypes = new ArrayList<>();
        btn_flag = 0;
        mapUtility = new MapUtility(getActivity());
        mLocationPermissionGranted = mapUtility.getLocationPermission();
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        sourceLat = -1;
        carId = -1;
        destLat = -1;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.rider_set_pickndrop, container, false);
        bzoom = view.findViewById(R.id.bzoom);
        check = true;
        cardView = view.findViewById(R.id.releative);
        arrow = view.findViewById(R.id.end);
        curr_location = view.findViewById(R.id.currentlocation);
        sourceanddestination = view.findViewById(R.id.img);
        div = view.findViewById(R.id.div);
        destination = view.findViewById(R.id.destinaton);
        source = view.findViewById(R.id.source);
        view_ride = view.findViewById(R.id.cab_border);
        view_rent = view.findViewById(R.id.rent_border);
        view_rent.setVisibility(View.INVISIBLE);
        ride = view.findViewById(R.id.ride);
        rent = view.findViewById(R.id.rent);
        micro = view.findViewById(R.id.micro);
        bzoomplus = view.findViewById(R.id.bzoomplus);
        xclass = view.findViewById(R.id.xclass);
        cab_card = view.findViewById(R.id.topcard);
        rent_card = view.findViewById(R.id.topcardrent);

        ride.setOnClickListener(v -> {
            if (check != true) {
                check = true;
                type = 1;
                populateTypes();

                //cab_card.setVisibility(View.VISIBLE);
                //rent_card.setVisibility(View.INVISIBLE);
                view_ride.setVisibility(View.VISIBLE);
                view_rent.setVisibility(View.INVISIBLE);
                bzoom.setText("Bzoom");
            }
        });
        rent.setOnClickListener(v -> {
            if (check != false) {

                check = false;
                type = 0;
                populateTypes();
                //cab_card.setVisibility(View.INVISIBLE);
                //rent_card.setVisibility(View.VISIBLE);
                view_ride.setVisibility(View.INVISIBLE);
                view_rent.setVisibility(View.VISIBLE);
                bzoom.setText("Next");
            }

        });


        bzoom.setOnClickListener(v -> {
            for (CarType type: carTypes) {
                if(type.isStatus()){
                    carId = type.getId();
                    Firebase.subscribeToTopic(type.name);
                    store.putString("topicName",type.name);
                }
            }
            if (sourceLat != -1 && carId != -1 && destLat != -1){
                double distence = mapUtility.distanceBetweenTwoPoint(sourceLat,sourceLong,destLat,destLong);
                long rate = mapUtility.calRate(distence);
                String destinationtext = destination.getText().toString();

                Ride.setVehicalId(String.valueOf(carId));
                String s = store.get("UID");
                Ride.setCustomerID(s);
                Ride.setIsCab(check);
                Ride.setLat(String.valueOf(sourceLat));
                Ride.setLon(String.valueOf(sourceLong));
                Ride.setLatDrop(String.valueOf(destLat));
                Ride.setLonDrop(String.valueOf(destLong));
                Ride.setDistance(rate);

                if (check) {

                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            isExist = RetrofitClass.getDirection(sourceLat, sourceLong, destLat, destLong);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);

                            if (isExist){
                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {

                                        isExist = RetrofitClass.getEstimation("/ride_estimation", GoogleDirectionApi.getDistance(),GoogleDirectionApi.getDuration(),Ride.getVehicalId(),Ride.isIsCab());

                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

                                        if (isExist){
                                            String sourcetext = source.getText().toString();
                                            Intent intent = new Intent(getActivity(), ConfirmDetail.class);
                                            intent.putExtra("from","cab");
                                            intent.putExtra("destination",destinationtext);
                                            intent.putExtra("distence",String.valueOf(rate) );
                                            intent.putExtra("lat",String.valueOf(sourceLat));
                                            intent.putExtra("lon",String.valueOf(sourceLong));
                                            intent.putExtra("carid",String.valueOf(carId) );
                                            startActivity(intent);
                                        }

                                    }
                                }.execute();
                            }

                        }
                    }.execute();


                }
                else {
                    Intent intent = new Intent(getActivity(), SelectTimeActivity.class);
                    intent.putExtra("activity", "rent");
                    startActivity(intent);
                }
            }else{
                Toast.makeText(getContext(),"You'er missing something!",Toast.LENGTH_LONG);
            }




        });
        return view;
    }


    public double calDistence(){
       return sourceLocation.distanceTo(destinationLocation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map1);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        populateTypes();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        //getDeviceLocation();
        getLastLocation();

        mMap.setOnCameraIdleListener(() -> {
           updateLat = mMap.getCameraPosition().target.latitude;
           updateLong = mMap.getCameraPosition().target.longitude;
           String location = mapUtility.getCompleteAddressString(updateLat,updateLong);
           if(btn_flag == 1){
               sourceLat = updateLat;
               sourceLong = updateLong;
               source.setText(location);
           }else if(btn_flag == 2){
               destination.setText(location);
               destLat = updateLat;
               destLong = updateLong;
           }
           Timber.d("Current location::" + updateLat + "long" + updateLong);
       });

        mMap.setOnCameraMoveStartedListener(i -> {
           if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

              if(btn_flag == 1){
                  source.setText("...");
              }else if(btn_flag == 2){
                  destination.setText("...");
              }
           }
       });
    }

    @Override
    public void onStart() {
        super.onStart();
        source.setOnTouchListener((v, event) -> {

            btn_flag = 1;
            return true;
        });
        destination.setOnTouchListener((v, event) -> {

            btn_flag = 2;
            return true;
        });

        curr_location.setOnClickListener(v -> {
            if(currentLocation !=null){
                source.setText(mapUtility.getCompleteAddressString(latLng.latitude, latLng.longitude));
                sourceLat = latLng.latitude;
                destLat = latLng.longitude;
            }
        });

                //source.setText(mapUtility.getCompleteAddressString(latLng.latitude, latLng.longitude)));

        arrow.setOnClickListener(v -> {
            if (ride_type){
                ride_type = false;
                arrow.setImageResource(R.drawable.icon_disable);

            }else {
                arrow.setImageResource(R.drawable.gonback);
                ride_type = true;

            }

        });


//        source.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_flag = 1;
//            }
//        });
//
//        destination.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btn_flag = 2;
//            }
//        });

    }

    public void populateTypes(){
        carId = -1;
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                carTypes = RetrofitClass.getRest(getString(R.string.getvahicaltype)+type);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerView = view.findViewById(R.id.cartyperecyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                CarTypeRecyclerViewAdapter carTypeRecyclerViewAdapter= new CarTypeRecyclerViewAdapter(carTypes,getActivity());
                recyclerView.setAdapter(carTypeRecyclerViewAdapter);

            }
        }.execute();
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        currentLocation = location;
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        if (firstTimeLatLon){
            updateLat = location.getLatitude();
            updateLong = location.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    latLng, DEFAULT_ZOOM));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            firstTimeLatLon = false;
        }
    }

    private long UPDATE_INTERVAL = 5 * 1000;  /* 5 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    @SuppressLint("MissingPermission")
    public void getLastLocation() {
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
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    @SuppressLint("MissingPermission")
    public void getLastLocationtest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext())).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                // Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, DEFAULT_ZOOM));
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            Log.d(TAG, "Current location: lat:"+location.getLatitude()+"long" +location.getLongitude());

                        }else {
                            if (MainActivity.currentLocation != null){
                                latLng = new LatLng(MainActivity.currentLocation.getLatitude(),MainActivity.currentLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        latLng, DEFAULT_ZOOM));
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            }else{
                                Toast.makeText(getContext(),"There is some issue with location",Toast.LENGTH_LONG);
                            }

                        }
                    }
                });
//                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(mLastKnownLocation.getLatitude(),
//                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory
//                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                mLocationPermissionGranted =  mapUtility.getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateTypes();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        carTypes.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        carTypes.clear();

    }

//    public void buttonSwitchLayout(){
//        if(check == false){
//            div.setVisibility(View.GONE);
//            destination.setVisibility(View.GONE);
//            bzoom.setText("Next");
//            arrow.setVisibility(View.GONE);
//            sourceanddestination.setImageResource(R.drawable.destination);
//            RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,
//                    350);
//
//            cardView.setLayoutParams(layout_description);
//
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(curr_location.getLayoutParams());
//            lp.addRule(RelativeLayout.ALIGN_PARENT_END);
//            lp.setMargins(0, 70, 20, 0);
//            curr_location.setLayoutParams(lp);
//        }
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
