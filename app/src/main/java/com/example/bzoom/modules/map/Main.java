package com.example.bzoom.modules.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bzoom.DirectionsJSONParser;
import com.example.bzoom.MainActivity;
import com.example.bzoom.R;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.owner.Owner;
import com.example.bzoom.modules.map.chauffeur.Chauffeur;
import com.example.bzoom.modules.map.driver.Driver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlaceDetectionClient;
//import com.google.android.gms.location.places.PlaceLikelihood;
//import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.GeoDataClient;
//import com.google.android.gms.location.places.PlaceDetectionClient;
//import com.google.android.gms.location.places.PlaceLikelihood;
//import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.bzoom.modal.firebase.Ride.jsonObject;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class Main extends Fragment
        implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = Main.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    MapView mapView;
    View mView;
    private String activityName ;
    // The entry points to the Places API.
//    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;


    // flag veriable in order to show current location and poly lines

    boolean flag;

    // veriables of poly lines
    LatLng start;
    LatLng end;

    Keystore keystore;
    ProgressDialog PG;
    private List<Marker> originMarkers = new ArrayList<>();
    protected GoogleApiClient mGoogleApiClient;
    double CurrentLatitude, CurrentLongitude;

    //53.747710, -2.470952
    MarkerOptions place1;
    MarkerOptions place2;
    String Long, Lati;


    private Polyline polyline;
    private String travellingDistance;
    private String travellingDuration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            mView = inflater.inflate(R.layout.mapview, container, false);

        } catch (InflateException e) {

        }

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityName = getActivity().getClass().getName();

        keystore = Keystore.getInstance(getActivity());
        flag = true;
//        PG = new ProgressDialog(getActivity());
//        PG.setMessage("Getting Direction");
//        PG.setCancelable(true);
//        PG.show();
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.


        // Construct a GeoDataClient.
        //mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        // Construct a PlaceDetectionClient.
        //mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


//        double asd =MainActivity.currentLocation.getLatitude() ;
//        double eer =MainActivity.currentLocation.getLongitude();
        // poly lines start

        String rideStatud = keystore.get("rideStatus");
        String role = keystore.get("role");
        try {
            if (rideStatud != null){
                if (rideStatud.equals(getString(R.string.start))){

                    end = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                    start = new LatLng(Double.valueOf(Ride.getLatDrop()), Double.valueOf(Ride.getLonDrop()));

                }else if (rideStatud.equals(getString(R.string.pending))){
                    if (keystore.get("role").equals("driver")){
                        start = new LatLng(Double.valueOf(Driver.getDriverLat()), Double.valueOf(Driver.getDriverLon()));
                        end = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                    }else if (keystore.get("role").equals("rider")) {
                        start = new LatLng(Double.valueOf(Driver.getDriverLat()), Double.valueOf(Driver.getDriverLon()));
                        end = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                    }else if (keystore.get("role").equals("owner")) {
                        getDeviceLocation();
                        end = new LatLng(Double.valueOf(Chauffeur.getLat()), Double.valueOf(Chauffeur.getLon()));
                    }
                }
            }else {
                if (keystore.get("role").equals("rider")){

                    end = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                    start = new LatLng(Double.valueOf(Ride.getLatDrop()), Double.valueOf(Ride.getLonDrop()));

                }else if (keystore.get("role").equals("driver")){

                    end = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));
                    start = new LatLng(Double.valueOf(Ride.getLatDrop()), Double.valueOf(Ride.getLonDrop()));

                }else if (keystore.get("role").equals("chauffeur")){
                    end = new LatLng(AvailableCar.activelat, AvailableCar.activelon);
                    start = new LatLng(MainActivity.currentLocation.getLatitude(), MainActivity.currentLocation.getLongitude());

                }else if (keystore.get("role").equals("owner")){
                    end = new LatLng(Chauffeur.getLat(),Chauffeur.getLon());
                    start = new LatLng(Double.valueOf(Ride.getLat()), Double.valueOf(Ride.getLon()));

                }else {
                    end = new LatLng(MainActivity.currentRide.getLat(), MainActivity.currentRide.getLon());
                    start = new LatLng(MainActivity.currentRide.getDrivelat(), MainActivity.currentRide.getDriverlon());
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), "We Are Unable to Get your Location Please check in Settings", Toast.LENGTH_SHORT).show();
        }
        // Build the map.

    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;


        getLocationPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
       // getDeviceLocation();
        mMap.setMyLocationEnabled(false);
        if (start == null){

            start = new LatLng(24.933885, 67.100148);
            end = new LatLng(24.923910, 67.102046);

        }
        direction(start, end);

//        CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(0.5f).build();
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        mMap.moveCamera(cameraUpdate);


        //PG.show();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                PG.dismiss();
//            }
//        }, 8000);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
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
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                    new LatLng(location.getLatitude(),
//                                            location.getLongitude()), DEFAULT_ZOOM));
//                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(new LatLng(location.getLatitude(),location.getLongitude()))
//                                    .draggable(true)
//                                    .alpha(1.0f));

                            start=new LatLng(location.getLatitude(),location.getLongitude());
                            Log.d(TAG, "Current location: lat:"+location.getLatitude()+"long" +location.getLongitude());

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


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
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
        //updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//
//        if (mLocationPermissionGranted) {
//            // Get the likely places - that is, the businesses and other points of interest that
//            // are the best match for the device's current location.
//            @SuppressWarnings("MissingPermission") final
//            Task<PlaceLikelihoodBufferResponse> placeResult =
//                    mPlaceDetectionClient.getCurrentPlace(null);
//            placeResult.addOnCompleteListener
//                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
//                        @Override
//                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
//
//                                // Set the count, handling cases where less than 5 entries are returned.
//                                int count;
//                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
//                                    count = likelyPlaces.getCount();
//                                } else {
//                                    count = M_MAX_ENTRIES;
//                                }
//
//                                int i = 0;
//                                mLikelyPlaceNames = new String[count];
//                                mLikelyPlaceAddresses = new String[count];
//                                mLikelyPlaceAttributions = new String[count];
//                                mLikelyPlaceLatLngs = new LatLng[count];
//
//                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
//                                    // Build a list of likely places to show the user.
//                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
//                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
//                                            .getAddress();
//                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
//                                            .getAttributions();
//                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
//
//                                    i++;
//                                    if (i > (count - 1)) {
//                                        break;
//                                    }
//                                }
//
//                                // Release the place likelihood buffer, to avoid memory leaks.
//                                likelyPlaces.release();
//
//                                // Show a dialog offering the user the list of likely places, and add a
//                                // marker at the selected place.
//                                openPlacesDialog();
//
//                            } else {
//                                Log.e(TAG, "Exception: %s", task.getException());
//                            }
//                        }
//                    });
//        } else {
//            // The user has not granted permission.
//            Log.i(TAG, "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title("my location")
//                    .position(mDefaultLocation)
//                    .snippet("dialog"));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
//    private void openPlacesDialog() {
//        // Ask the user to choose the place where they are now.
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // The "which" argument contains the position of the selected item.
//                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
//                String markerSnippet = mLikelyPlaceAddresses[which];
//                if (mLikelyPlaceAttributions[which] != null) {
//                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
//                }
//
//                // Add a marker for the selected place, with an info window
//                // showing information about that place.
//                mMap.addMarker(new MarkerOptions()
//                        .title(mLikelyPlaceNames[which])
//                        .position(markerLatLng)
//                        .snippet(markerSnippet));
//
//                // Position the map's camera at the location of the marker.
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
//                        DEFAULT_ZOOM));
//            }
//        };
//
//        // Display the dialog.
//        AlertDialog dialog = new AlertDialog.Builder(getContext())
//                .setTitle("pick place")
//                .setItems(mLikelyPlaceNames, listener)
//                .show();
//    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void direction(LatLng latlng, LatLng latlng1) {
        String url = getDirectionsUrl(latlng, latlng1);

        Main.DownloadTask downloadTask = new Main.DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        Log.d("Downloaded", "url " + url);
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Main.ParserTask parserTask = new Main.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false&alternatives=false&units=metric&mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String MY_API_KEY =getResources().getString(R.string.google_maps_key) ;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        return url;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<LatLng>();
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.width(2);
            lineOptions.color(Color.RED);
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng position = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    position = new LatLng(lat, lng);

                    points.add(position);
                }
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                // Adding all the points in the route to LineOptions

                lineOptions.addAll(points);

            }
            if (polyline != null) {
                polyline.remove();
            }
            // Drawing polyline in the Google Map for the i-th route
            polyline = mMap.addPolyline(lineOptions);
            polyline.setWidth(10);
            polyline.setColor(R.color.colorPrimary);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(start);
            builder.include(end);
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);

            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                public void onCancel() {
                }

                public void onFinish() {

                    CameraUpdate zout = CameraUpdateFactory.zoomBy((float) 0.1);
                    mMap.animateCamera(zout);
                    mMap.addMarker(new MarkerOptions()
                            .position(start)
                            .anchor(0.5f, 0.5f)
                            .title("My location"));
                    mMap.addMarker(new MarkerOptions()
                            .position(end)
                            .anchor(0.5f, 0.5f)
                            .title("His location"));
                }
            });


        }

    }
}
/*{"geocoded_waypoints":[{"geocoder_status":"OK","place_id":"ChIJUZsYALQ4sz4Rf-oH7X4gEv0",
"types":["street_address"]},{"geocoder_status":"OK","place_id":"ChIJCXsXZso4sz4RosrXN99DPOc",
"types":["street_address"]}],"routes":[{"bounds":{"northeast":{"lat":24.9352828,"lng":67.1000023},
"southwest":{"lat":24.9268139,"lng":67.0944463}},"copyrights":"Map data ©2019",
"legs":[{"distance":{"text":"1.6 km","value":1607},"duration":{"text":"8 mins","value":506}
,"end_address":"Plot B 277, Block 6 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan",
"end_location":{"lat":24.9268139,"lng":67.0978756},
"start_address":"Plot D 59\/1, Block 4 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan",
"start_location":{"lat":24.933965,"lng":67.1000023},"steps":[{"distance":{"text":"50 m","value":50}
,"duration":{"text":"1 min","value":11},"end_location":{"lat":24.9336725,"lng":67.09962829999999},
"html_instructions":"Head <b>southwest<\/b>","polyline":{"points":"g|dwC_npxKv@fA@@"},
"start_location":{"lat":24.933965,"lng":67.1000023},"travel_mode":"DRIVING"},
{"distance":{"text":"0.2 km","value":235},"duration":{"text":"1 min","value":58},
"end_location":{"lat":24.9352828,"lng":67.0981234},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Pass by Neusol Private Limited (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"mzdwCukpxKw@t@{DnDmAfA"},"start_location":{"lat":24.9336725,"lng":67.09962829999999},"travel_mode":"DRIVING"},{"distance":{"text":"0.5 km","value":487},"duration":{"text":"3 mins","value":168},"end_location":{"lat":24.9324394,"lng":67.0944463},"html_instructions":"Turn <b>left<\/b> at Winbury Grammar School onto <b>Naqash Kazmi Rd<\/b><div style=\"font-size:0.9em\">Pass by New Dhoraji Colony Apartments (on the left)<\/div>","maneuver":"turn-left","polyline":{"points":"odewCgbpxKl@x@~@vAJP`@d@Tb@Z`@Z`@r@dAZb@^h@h@l@rDhF"},"start_location":{"lat":24.9352828,"lng":67.0981234},"travel_mode":"DRIVING"},{"distance":{"text":"0.6 km","value":566},"duration":{"text":"3 mins","value":165},"end_location":{"lat":24.9285474,"lng":67.0980624},"html_instructions":"Turn <b>left<\/b> at Cold spot depo onto <b>Olympian Islahuddin Rd<\/b><div style=\"font-size:0.9em\">Pass by Experts' Zone Gulshan Campus (on the right)<\/div>","maneuver":"turn-left","polyline":{"points":"wrdwCikoxKj@e@h@e@~AuAlCeCjAgAbDwCNOrAoATQzAwA"},"start_location":{"lat":24.9324394,"lng":67.0944463},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":137},"duration":{"text":"1 min","value":43},"end_location":{"lat":24.9277318,"lng":67.09704239999999},"html_instructions":"Turn <b>right<\/b> at Mac Caterers<div style=\"font-size:0.9em\">Pass by Masjid- e- Akbar (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"mzcwC{apxK\\b@`@j@\\`@dAxA"},"start_location":{"lat":24.9285474,"lng":67.0980624},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":132},"duration":{"text":"1 min","value":61},"end_location":{"lat":24.9268139,"lng":67.0978756},"html_instructions":"Turn <b>left<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-left","polyline":{"points":"iucwCo{oxKvDgD"},"start_location":{"lat":24.9277318,"lng":67.09704239999999},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}],"overview_polyline":{"points":"g|dwC_npxKx@hAsFdFmAfAl@x@jAhBnBlCnAhBhAvArDhFj@e@hC{BlKuJhBaBzAwA\\b@~@lAdAxAvDgD"},"summary":"Naqash Kazmi Rd and Olympian Islahuddin Rd","warnings":[],"waypoint_order":[]},{"bounds":{"northeast":{"lat":24.9351572,"lng":67.105387},"southwest":{"lat":24.9268139,"lng":67.09704239999999}},"copyrights":"Map data ©2019","legs":[{"distance":{"text":"2.2 km","value":2228},"duration":{"text":"8 mins","value":468},"end_address":"Plot B 277, Block 6 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan","end_location":{"lat":24.9268139,"lng":67.0978756},"start_address":"Plot D 59\/1, Block 4 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan","start_location":{"lat":24.933965,"lng":67.1000023},"steps":[{"distance":{"text":"50 m","value":50},"duration":{"text":"1 min","value":11},"end_location":{"lat":24.9336725,"lng":67.09962829999999},"html_instructions":"Head <b>southwest<\/b>","polyline":{"points":"g|dwC_npxKv@fA@@"},"start_location":{"lat":24.933965,"lng":67.1000023},"travel_mode":"DRIVING"},{"distance":{"text":"0.2 km","value":246},"duration":{"text":"1 min","value":67},"end_location":{"lat":24.9319636,"lng":67.10117199999999},"html_instructions":"Turn <b>left<\/b><div style=\"font-size:0.9em\">Pass by Mishu School (on the left)<\/div>","maneuver":"turn-left","polyline":{"points":"mzdwCukpxKzAuAxCiC@C|AoA"},"start_location":{"lat":24.9336725,"lng":67.09962829999999},"travel_mode":"DRIVING"},{"distance":{"text":"0.5 km","value":532},"duration":{"text":"1 min","value":85},"end_location":{"lat":24.9350792,"lng":67.10517949999999},"html_instructions":"Turn <b>left<\/b> at The Canvas Beauty Bar onto <b>Allama Shabbir Ahmed Usmani Rd<\/b><div style=\"font-size:0.9em\">Pass by Lush Crush (on the left)<\/div>","maneuver":"turn-left","polyline":{"points":"wodwCiupxKyF{HuJeN"},"start_location":{"lat":24.9319636,"lng":67.10117199999999},"travel_mode":"DRIVING"},{"distance":{"text":"1.1 km","value":1063},"duration":{"text":"3 mins","value":181},"end_location":{"lat":24.9290118,"lng":67.0976189},"html_instructions":"Make a <b>U-turn<\/b> at Iqbalz Collections<div style=\"font-size:0.9em\">Pass by Al Quresh Dairy &amp; Foods (on the left)<\/div>","maneuver":"uturn-right","polyline":{"points":"gcewCknqxKOS\\ULVHNx@zA~AzBHL|DpFnFzHBBfEjGjAzAbDvEXb@d@j@^\\h@b@"},"start_location":{"lat":24.9350792,"lng":67.10517949999999},"travel_mode":"DRIVING"},{"distance":{"text":"68 m","value":68},"duration":{"text":"1 min","value":20},"end_location":{"lat":24.9285474,"lng":67.0980624},"html_instructions":"Turn <b>left<\/b> at <b>Kaptaan Chowk<\/b> onto <b>Olympian Islahuddin Rd<\/b><div style=\"font-size:0.9em\">Pass by AXE HAIR SALOON (on the left)<\/div>","maneuver":"turn-left","polyline":{"points":"i}cwCc_pxKzAwA"},"start_location":{"lat":24.9290118,"lng":67.0976189},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":137},"duration":{"text":"1 min","value":43},"end_location":{"lat":24.9277318,"lng":67.09704239999999},"html_instructions":"Turn <b>right<\/b> at Mac Caterers<div style=\"font-size:0.9em\">Pass by Masjid- e- Akbar (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"mzcwC{apxK\\b@`@j@\\`@dAxA"},"start_location":{"lat":24.9285474,"lng":67.0980624},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":132},"duration":{"text":"1 min","value":61},"end_location":{"lat":24.9268139,"lng":67.0978756},"html_instructions":"Turn <b>left<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-left","polyline":{"points":"iucwCo{oxKvDgD"},"start_location":{"lat":24.9277318,"lng":67.09704239999999},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}],"overview_polyline":{"points":"g|dwC_npxKx@hAtF_F~AsAoRaXOS\\UVf@x@zA~AzBfE~FrF~HfEjGjAzA|DzFdAhAh@b@zAwA~@nAbBzBvDgD"},"summary":"Allama Shabbir Ahmed Usmani Rd","warnings":[],"waypoint_order":[]},{"bounds":{"northeast":{"lat":24.9387677,"lng":67.105387},"southwest":{"lat":24.9268139,"lng":67.09704239999999}},"copyrights":"Map data ©2019","legs":[{"distance":{"text":"2.8 km","value":2808},"duration":{"text":"10 mins","value":615},"end_address":"Plot B 277, Block 6 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan","end_location":{"lat":24.9268139,"lng":67.0978756},"start_address":"Plot D 59\/1, Block 4 Gulshan-e-Iqbal, Karachi, Karachi City, Sindh, Pakistan","start_location":{"lat":24.933965,"lng":67.1000023},"steps":[{"distance":{"text":"50 m","value":50},"duration":{"text":"1 min","value":11},"end_location":{"lat":24.9336725,"lng":67.09962829999999},"html_instructions":"Head <b>southwest<\/b>","polyline":{"points":"g|dwC_npxKv@fA@@"},"start_location":{"lat":24.933965,"lng":67.1000023},"travel_mode":"DRIVING"},{"distance":{"text":"0.3 km","value":303},"duration":{"text":"1 min","value":79},"end_location":{"lat":24.9357571,"lng":67.0976925},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Pass by Neusol Private Limited (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"mzdwCukpxKw@t@{DnDmAfA_BtA"},"start_location":{"lat":24.9336725,"lng":67.09962829999999},"travel_mode":"DRIVING"},{"distance":{"text":"0.5 km","value":494},"duration":{"text":"2 mins","value":119},"end_location":{"lat":24.9386326,"lng":67.1014233},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Pass by Islamic Grooming School (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"ogewCq_pxKeBeCyAuBGGu@eAUa@m@y@eA}Aq@}@}@uAeAqA"},"start_location":{"lat":24.9357571,"lng":67.0976925},"travel_mode":"DRIVING"},{"distance":{"text":"0.6 km","value":574},"duration":{"text":"2 mins","value":103},"end_location":{"lat":24.9351572,"lng":67.1052789},"html_instructions":"Turn <b>right<\/b> at Global Desi Foundation onto <b>Abul Hasan Isphahani Rd<\/b><div style=\"font-size:0.9em\">Pass by Plastic and plastic (on the left)<\/div>","maneuver":"turn-right","polyline":{"points":"myewC{vpxK[APQn@o@v@}@x@aADGvCcD`AgAn@w@`AkAhBmBz@w@d@a@"},"start_location":{"lat":24.9386326,"lng":67.1014233},"travel_mode":"DRIVING"},{"distance":{"text":"1.0 km","value":1050},"duration":{"text":"3 mins","value":179},"end_location":{"lat":24.9290118,"lng":67.0976189},"html_instructions":"Turn <b>right<\/b> at Al Noor Aluminum &amp; Glass Maskan onto <b>Allama Shabbir Ahmed Usmani Rd<\/b><div style=\"font-size:0.9em\">Pass by Al Quresh Dairy &amp; Foods (on the left)<\/div>","maneuver":"turn-right","polyline":{"points":"wcewC_oqxK\\ULVHNx@zA~AzBHL|DpFnFzHBBfEjGjAzAbDvEXb@d@j@^\\h@b@"},"start_location":{"lat":24.9351572,"lng":67.1052789},"travel_mode":"DRIVING"},{"distance":{"text":"68 m","value":68},"duration":{"text":"1 min","value":20},"end_location":{"lat":24.9285474,"lng":67.0980624},"html_instructions":"Turn <b>left<\/b> at <b>Kaptaan Chowk<\/b> onto <b>Olympian Islahuddin Rd<\/b><div style=\"font-size:0.9em\">Pass by AXE HAIR SALOON (on the left)<\/div>","maneuver":"turn-left","polyline":{"points":"i}cwCc_pxKzAwA"},"start_location":{"lat":24.9290118,"lng":67.0976189},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":137},"duration":{"text":"1 min","value":43},"end_location":{"lat":24.9277318,"lng":67.09704239999999},"html_instructions":"Turn <b>right<\/b> at Mac Caterers<div style=\"font-size:0.9em\">Pass by Masjid- e- Akbar (on the right)<\/div>","maneuver":"turn-right","polyline":{"points":"mzcwC{apxK\\b@`@j@\\`@dAxA"},"start_location":{"lat":24.9285474,"lng":67.0980624},"travel_mode":"DRIVING"},{"distance":{"text":"0.1 km","value":132},"duration":{"text":"1 min","value":61},"end_location":{"lat":24.9268139,"lng":67.0978756},"html_instructions":"Turn <b>left<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-left","polyline":{"points":"iucwCo{oxKvDgD"},"start_location":{"lat":24.9277318,"lng":67.09704239999999},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}],"overview_polyline":{"points":"g|dwC_npxKx@hAsFdFmD|C_E{FaCiDwB{C}@uAeAqA[APQfBmBxGuHpBcCdDeDbAw@Vf@x@zA~AzBfE~FrF~HfEjGjAzA|DzFdAhAh@b@zAwA~@nAbBzBvDgD"},"summary":"Abul Hasan Isphahani Rd and Allama Shabbir Ahmed Usmani Rd","warnings":[],"waypoint_order":[]}],"status":"OK"}*/