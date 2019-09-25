package com.example.bzoom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modules.map.MapUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import timber.log.Timber;

public class PickupLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener  {

    private Location mLastKnownLocation;
    double updateLat;
    double updateLong;
    private MapView mapView;
    private GoogleMap mMap;
    private MapUtility mapUtility;
    private boolean mLocationPermissionGranted;
    private EditText source;
    private Location currentLocation;
    private LatLng latLng;
    private static final int DEFAULT_ZOOM = 15;
    private ImageView curr_location;
    Button button;
    boolean firstTimeLatLon;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_location);

        button = findViewById(R.id.view_peofile);
        button.setText("Confirm Pickup");
        TextView textView = findViewById(R.id.textView6);
        textView.setVisibility(View.GONE);
        source = findViewById(R.id.source);
        curr_location = findViewById(R.id.currentlocation);
        mapUtility = new MapUtility(this);
        firstTimeLatLon = true;
        address = findViewById(R.id.textView4);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(PickupLocationActivity.this,SelectTimeActivity.class);
            intent.putExtra("activity","owner");
            Ride.setLon(String.valueOf(updateLong));
            Ride.setLat(String.valueOf(updateLat));
            startActivity(intent);
        });

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        mapUtility.getLocationPermission();
        getLastLocationtest();
        mMap.setOnCameraIdleListener(() -> {
            updateLat = mMap.getCameraPosition().target.latitude;
            updateLong = mMap.getCameraPosition().target.longitude;

            String location = mapUtility.getCompleteAddressString(updateLat,updateLong);
            Ride.address = location;
            source.setText(location);
            address.setText(mapUtility.getCompleteAddress(updateLat,updateLong));
            Timber.d("Current location::" + updateLat + "long" + updateLong);
        });

        mMap.setOnCameraMoveStartedListener(i -> {
            if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                source.setText("...");
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
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
                        currentLocation = location;
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
        curr_location.setOnClickListener(v -> {
            if(currentLocation ==null){
            }else {
                source.setText(mapUtility.getCompleteAddressString(latLng.latitude, latLng.longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        latLng, DEFAULT_ZOOM));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                updateLat = latLng.latitude;
                updateLong = latLng.longitude;
            }
        });
    }
}
