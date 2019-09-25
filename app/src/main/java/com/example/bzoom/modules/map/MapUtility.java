package com.example.bzoom.modules.map;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.ContentValues.TAG;

public class MapUtility {

    Context context;
    private static int speed = 30;
    LatLng latLng;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private static FusedLocationProviderClient fusedLocationProviderClient;

    public MapUtility(Context context) {
        this.context = context;
    }

    public MapUtility() {
    }

    public boolean  getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
                 return true;
        } else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            mLocationPermissionGranted = false;
        }

        return false;
    }

    public LatLng getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        try {
            if (mLocationPermissionGranted) {
                // Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        latLng =  new LatLng(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "Current location: lat:"+location.getLatitude()+"long" +location.getLongitude());

                    }
                });
            }
        }catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
        return latLng;
    }


    public static double distanceBetweenTwoPoint(double srcLat, double srcLng, double desLat, double desLng) {
//        LatLng latLngA = new LatLng(12.3456789,98.7654321);
//        LatLng latLngB = new LatLng(98.7654321,12.3456789);
        LatLng latLngA = new LatLng(srcLat,srcLng);
        LatLng latLngB = new LatLng(desLat,desLng);
        Location locationA = new Location("point A");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);
        Location locationB = new Location("point B");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        double distance = locationA.distanceTo(locationB);
        return (distance);
    }

    public static double durationBetweenTwoPoint(double srcLat, double srcLng, double desLat, double desLng) {

        double distance = distanceBetweenTwoPoint( srcLat,  srcLng,  desLat,  desLng);
        int speed=5;
        double time = distance/speed;
        time = time/60;
        return time;
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    public static long calRate(double km){

        float baserate = (float) 1.5;
        double time =  km/speed;
        long rate=(long) ((time * baserate)*60);
        return (rate);
    }

    public static double getTime(double km){

        float baserate = (float) 1.5;
        double time =  km/speed;
        return time;
    }

    public void calRate(double srcLat, double srcLng, double desLat, double desLng){
        Location loc1 = new Location("");
        loc1.setLatitude(srcLat);
        loc1.setLongitude(srcLng);

        Location loc2 = new Location("");
        loc2.setLatitude(desLat);
        loc2.setLongitude(desLng);

        float distance = loc1.distanceTo(loc2);
        float time = distance/speed;

    }

    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        String strAdd = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);

            if (addresses != null) {

                strAdd = addresses.get(0).getFeatureName();
                Log.w("My Current loction",
                        "" + strAdd);
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }
    public String getCompleteAddress(double LATITUDE, double LONGITUDE) {

        String strAdd = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,
                    LONGITUDE, 1);

            if (addresses != null) {
                strAdd = addresses.get(0).getFeatureName();
                strAdd = strAdd+" "+addresses.get(0).getSubLocality();
                //strAdd = addresses.get(0).getAddressLine(0);
                Log.w("My Current loction",
                        "" + strAdd);
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }

    public void getRideInRadius(double lat,double lon){

        Location location = new Location("");

    }


}
