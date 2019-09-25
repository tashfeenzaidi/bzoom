package com.example.bzoom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    //24.812957, 67.109558
    // LatLng start=new LatLng(57.142999, -2.101721);

    LatLng start;
    LatLng end;

    ProgressDialog PG;
    private List<Marker> originMarkers = new ArrayList<>();
    protected GoogleApiClient mGoogleApiClient;
    double CurrentLatitude,CurrentLongitude;

    //53.747710, -2.470952
    MarkerOptions place1;
    MarkerOptions place2;
    String Long,Lati;


    private Polyline polyline;






    /*  private MarkerOptions place1, place2;*/

    //24.816165, 67.109492

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        PG=new ProgressDialog(this);
        PG.setMessage("Getting Direction");
        PG.setCancelable(false);
        PG.show();


        //    place1 = new MarkerOptions().position(start).title("Location 1");
        //   place2 = new MarkerOptions().position(end).title("Locatio 2");


//        try {
//
//            SharedPreferences CurrentLocationPrefs = getSharedPreferences("LatLang", Context.MODE_PRIVATE);
//            CurrentLatitude = Double.parseDouble(CurrentLocationPrefs.getString("Lat", "0"));
//            CurrentLongitude = Double.parseDouble(CurrentLocationPrefs.getString("Lag", "0"));
//
//        }catch (Exception ex){
//
//
//            Toast.makeText(this,"We Are Unable to Get your Location Please check in Settings",Toast.LENGTH_SHORT).show();
//
//
//        }


        SharedPreferences prefs = getSharedPreferences("Location", MODE_PRIVATE);
        Long = prefs.getString("Longitude", null);
        Lati = prefs.getString("Latitude", null);


        try {
            end = new LatLng(24.934193, 67.100274);
            start=new LatLng(53.747710, -2.470952);
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this,"We Are Unable to Get your Location Please check in Settings",Toast.LENGTH_SHORT).show();

        }





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


 /*       LatLng hcmus = new LatLng(24.812957, 67.109558);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("pokas")
                .position(hcmus)));*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        direction(start, end);


        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(start)
                .anchor(0.5f, 0.5f)
                .title("Current Place"));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(start).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);


        PG.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                PG.dismiss();
            }
        }, 8000);



        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(end)
                .anchor(0.5f, 0.5f)
                .title("Destination Place"));

    }


    private void direction(LatLng latlng, LatLng latlng1) {
        String url = getDirectionsUrl(latlng, latlng1);

        MapsActivity.DownloadTask downloadTask = new MapsActivity.DownloadTask();

// Start downloading json data from Google Directions API
        downloadTask.execute(url);






        Log.d("Downloaded", "url " + url);
    }



    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false&alternatives=true&units=metric&mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String MY_API_KEY =getResources().getString(R.string.google_maps_key) ;
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + MY_API_KEY;

        return url;
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

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

            MapsActivity.ParserTask parserTask = new MapsActivity.ParserTask();

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
    /**
     * A class to parse the Google Places in JSON format
     */
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
            ;
            PolylineOptions lineOptions = new PolylineOptions();
            ;
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
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                // Adding all the points in the route to LineOptions

                lineOptions.addAll(points);

            }
            if (polyline != null) {
                polyline.remove();
            }
            // Drawing polyline in the Google Map for the i-th route
            polyline = mMap.addPolyline(lineOptions);


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(start);
            builder.include(end);
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);

            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                public void onCancel() {
                }

                public void onFinish() {


                    CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -0.1);
                    mMap.animateCamera(zout);
                }
            });


        }



    }






}
