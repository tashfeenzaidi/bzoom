package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Firebase;
import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.RiderFirebase;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.rider.Rider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ConfirmDetail extends AppCompatActivity {

    Button bzoom;
    private String activity;
    TableRow tableRow;
    TextView rate;
    TextView destination;
    TextView type;
    static String dest;
    String name;
    String carId;
    String lat;
    String lon;
    Keystore keystore;
    private String success;
    private String distence;
    TextView startDate;
    TextView endDate;
    TextView startDay;
    TextView endDay;
    TextView arrivalTime;
    TextView ampm;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_confirm_detail);


        MapUtility mapUtility = new MapUtility(this);
        keystore =  Keystore.getInstance(this);
        type = findViewById(R.id.type);
        rate  = findViewById(R.id.rate);
        destination = findViewById(R.id.destination);
        tableRow =  findViewById(R.id.row1);
        tableRow.setVisibility(View.GONE);

        bzoom =  findViewById(R.id.next);


        String carName = keystore.get("topicName");
        String address = mapUtility.getCompleteAddress(Double.valueOf(Ride.getLat()),Double.valueOf(Ride.getLon()));
        type.setText(carName);
        destination.setText(address);
        rate.setText(String.valueOf((int)Ride.getFair()));


        if (getIntent().getExtras() != null){
            activity =getIntent().getExtras().getString("from");
            if (activity != null){
                if (activity.equals("rent")){
                    tableRow.setVisibility(View.VISIBLE);
                    startDate = findViewById(R.id.number);
                    endDate = findViewById(R.id.number1);
                    startDay = findViewById(R.id.on_day);
                    endDay  = findViewById(R.id.till_day);
                    arrivalTime = findViewById(R.id.totaltime);
                    ampm = findViewById(R.id.am_pm);

                    arrivalTime.setText(SelectTimeActivity.arrivalTime);
                    startDay.setText(SelectTimeActivity.onDay);
                    endDay.setText(SelectTimeActivity.tillDay);

                    startDate.setText(SelectTimeActivity.onMonth);
                    endDate.setText(SelectTimeActivity.tillMonth);

                    ampm.setText(SelectTimeActivity.arrivalAmPM);

                }else if(activity.equals("cab")){
                    Intent intent = getIntent();
                    dest  = intent.getStringExtra("destination");
                    distence  = intent.getStringExtra("distence");
                    carId = intent.getStringExtra("carid");
                    lat   = intent.getStringExtra("lat");
                    lon   = intent.getStringExtra("lon");
                }
            }
        }

        bzoom.setOnClickListener(v -> {

            Ride.setCustomerID(keystore.get("userId"));

            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    success = RetrofitClass.postRide("/customer-ride-request");
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    if (success != null){
                        keystore.putString("rideId",success);
                        keystore.putString(getString(R.string.rid_status),getString(R.string.pending));
                        Intent intent1 = new Intent(ConfirmDetail.this,FindingActivity.class);
                        startActivity(intent1);
                    }


                }
            }.execute();


            Firebase.subscribeToTopic(keystore.get("UID"));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



    }
}
