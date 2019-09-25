package com.example.bzoom;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.owner.Owner;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.Main;
import com.example.bzoom.modules.map.MapUtility;
import com.example.bzoom.modules.map.chauffeur.ChauffeurTimeFinish;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Date;

public class CollectTheCarActivity extends AppCompatActivity  {


    TextView counter;
    Button cancel;
    TextView contact;
    TextView name;
    TextView time;
    RatingBar ratingBar;
    ImageView profilePic;
    ImageView carImage;
    TextView carDetails;
    TextView price;
    TextView location;
    TextView rating;
    TextView heading;

    MapUtility mapUtility;
    String locationPickup;
    Button returned;
    private CountDownTimer countDownTimer;
    Keystore keystore;
    private boolean isExist;
    private int status;
    private String bodyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_the_car);

        status = 30;
        bodyTitle = getString(R.string.chauffeur_cancel_owner);
        loadFragment();
        keystore = Keystore.getInstance(this);
        rating =  findViewById(R.id.textView3);
        contact =  findViewById(R.id.txtph);
        cancel =  findViewById(R.id.cancel);
        name =  findViewById(R.id.name);
        ratingBar =  findViewById(R.id.ratingBar);
        profilePic   =  findViewById(R.id.image_rv);
        carImage   =  findViewById(R.id.textView2);
        location =  findViewById(R.id.location);
        counter =  findViewById(R.id.counter);
        time = findViewById(R.id.time);
        heading = findViewById(R.id.heading);
         returned =  findViewById(R.id.startride);
         mapUtility = new MapUtility(this);
         returned.setText("On my way");
        if (Role.status){
             returned.setText(getString(R.string.returned_to_owner));
             heading.setText("Return to owner");
         }
        setCounter(30000);
        initializeUI();
        Date date = Utilities.stringToDate(AvailableCar.activeEndDate,AvailableCar.activeEndTime    );
        startAlert(date.getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();
        returned.setOnClickListener(v -> {

            String status = returned.getText().toString();
            if (status.equals("On my way")){
                countDownTimer.cancel();
                counter.setText("");
                this.status = 32;
                pushNotification("/change_booking_status_to_notify",getString(R.string.chauffeur_on_the_way),14,"I have arrived");
            }else if (status.equals("I have arrived")){
                //setCounter(900000);
                if (Role.status){
                    pushNotification("/change_booking_status_to_notify",getString(R.string.reached_owner_place),22,"Im at owner's place");
                }else {
                    setCounter((15*60)*1000);
                    this.status = 33;
                    pushNotification("/change_booking_status_to_notify",getString(R.string.chauffeur_arrived),15,"Met owner");
                }

            }else if (status.equals("Met owner")){

                Intent intent = new Intent(CollectTheCarActivity.this,Belongins.class);
                intent.putExtra("role","chauffer");
                startActivity(intent);

            }else if (status.equals(getString(R.string.returned_to_owner))){
                Role.status = false;
                pushNotification("/change_booking_status_to_notify","Chauffeur is coming",21,"Im at owner's place");

            }else if (status.equals("Im at owner's place")){
                pushNotification("/change_booking_status_to_notify","Chauffeur is waiting outside",22,"Im at owner's place");
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {

                        isExist = RetrofitClass.chauffeurCancelBooking("/booking_cancel",status,bodyTitle);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (isExist){
                            Intent intent = new Intent(CollectTheCarActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }.execute();
            }
        });
    }

    private void loadFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Main()).commit();
    }

    private void setCounter(long time){
        countDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //counter.setText(millisUntilFinished / 1000+" s");
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                counter.setText(String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {

                counter.setText(" ");
                if (status == 30){
                    status = 31;
                }else if (status==33){
                    status = 34;
                }

            }
        }.start();
    }

    public void pushNotification(String endPoint,String bodyTitle, int status,String buttonText){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                isExist = RetrofitClass.notification(endPoint,bodyTitle,status);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isExist){
                    returned.setText(buttonText);
                }
            }
        }.execute();
    }

    public void initializeUI(){

     new AsyncTask<Void,Void,Void>(){
         @Override
         protected Void doInBackground(Void... voids) {

             isExist = RetrofitClass.getProfile("/profile/",AvailableCar.activeOwnerId,"owner");
             return null;

         }

         @Override
         protected void onPostExecute(Void aVoid) {
             super.onPostExecute(aVoid);

             if (isExist){

                 name.setText(Owner.getName());
                 contact.setText(Owner.getNumber());
                 rating.setText(Owner.getRating());
                 if (!Owner.getRating().equals("0")){
                     ratingBar.setRating(Float.valueOf(Owner.getRating()));
                 }else {
                     ratingBar.setRating(0);
                 }
             }
         }
     }.execute();


     location.setText(mapUtility.getCompleteAddress(AvailableCar.activelat,AvailableCar.activelon));
     double time =  MapUtility.durationBetweenTwoPoint(MainActivity.currentLocation.getLatitude(),MainActivity.currentLocation.getLongitude(),AvailableCar.activelat,AvailableCar.activelon);
     this.time.setText((long) time +" mins from here");
    }

    public  void startAlert(long time){
        long i = time;
        Intent intent = new Intent(this, ChauffeurTimeFinish.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + i, pendingIntent);
    }

}