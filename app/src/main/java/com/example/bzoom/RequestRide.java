package com.example.bzoom;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.MapUtility;

public class RequestRide extends AppCompatActivity {
    private boolean isExist;

    TextView startTime;
    TextView endTime;
    TextView address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_ride);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView textView = findViewById(R.id.textView8);

        startTime = findViewById(R.id.source);
        endTime = findViewById(R.id.destination);
        address = findViewById(R.id.address);

        fillLayout();

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
              isExist =   RetrofitClass.postVehical("/available-car");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isExist){
//                    Intent intent = new Intent(RequestRide.this,RideStartActivity.class);
//                    startActivity(intent);
                    textView.setText("Searching....");
                }else {

                }
            }
        }.execute();

        Button disable_car=  findViewById(R.id.next);
        disable_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void fillLayout(){
        startTime.setText(Ride.getStartDate()+" "+Ride.getStartTime());
        endTime.setText(Ride.getEndDate()+" "+Ride.getEndTime());
        address.setText(Ride.address);
    }


}
