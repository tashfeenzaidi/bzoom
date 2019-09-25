package com.example.bzoom.Utility;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Belongins;
import com.example.bzoom.ChaufferSuccessActivity;
import com.example.bzoom.FindingActivity;
import com.example.bzoom.MainActivity;
import com.example.bzoom.R;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.Rating;

public class InAppNotification extends AppCompatActivity {


    TextView message;
    Button ok;
    AlertDialog alertDialog ;

    private boolean isExist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        setContentView(R.layout.notification_layout);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        this.setFinishOnTouchOutside(true);
        message= findViewById(R.id.message);
        ok = findViewById(R.id.ok);
        String getMessage = getIntent().getStringExtra("message");
        message.setText(getMessage);

        if (getMessage.equals(getString(R.string.chauffuer_accept_agreement)) || getMessage.equals(getString(R.string.received_keys))
                ||  getMessage.equals("Return key to owner") || getMessage.equals("Did you received key")){
            ok.setVisibility(View.VISIBLE);
            this.setFinishOnTouchOutside(false);
        }
        if(getMessage.equals("Driver cancel the ride! other will come soon.")){
            Intent intent = new Intent(InAppNotification.this, FindingActivity.class);
            intent.putExtra("role","owner");
            startActivity(intent);
            finish();
        }else if(getMessage.equals("Rider cancel the ride!")){
            Intent intent = new Intent(InAppNotification.this, MainActivity.class);
            intent.putExtra("role","owner");
            startActivity(intent);
            finish();
        }

        if(getMessage.equals("Chauffeur is waiting outside")){
            Intent intent = new Intent(InAppNotification.this, Belongins.class);
            intent.putExtra("role","owner");
            startActivity(intent);
            finish();
        }

        if(getMessage.equals("chauffeur not accept on your agreement") || getMessage.equals("owner not accept on your agreement")){
            Toast.makeText(this,getMessage,Toast.LENGTH_LONG);
            Intent intent = new Intent(InAppNotification.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(getMessage.equals("chauffeur cancel the booking")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(InAppNotification.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },2000L);

        }

        if(getMessage.equals("owner received key")){
            AlertDialog.Builder builder = new AlertDialog.Builder(InAppNotification.this);
            LayoutInflater li = LayoutInflater.from(InAppNotification.this);
            View promptsView = li.inflate(R.layout.submit_rating, null);
            builder.setView(promptsView);

            // create alert dialog
            AlertDialog alertDialog = builder.create();

            // show it
            alertDialog.show();
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getMessage.equals(getString(R.string.chauffuer_accept_agreement))){
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            RetrofitClass.notification("/change_booking_status_to_notify",getString(R.string.key_received),19);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finish();
                        }
                    }.execute();

                }else if(getMessage.equals(getString(R.string.received_keys))){
                    Intent intent = new Intent(InAppNotification.this, ChaufferSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }else if(getMessage.equals("Return key to owner")){
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            RetrofitClass.notification("/change_booking_status_to_notify","Did you received key",25);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ratingAlertDialog();
                        }
                    }.execute();
                }else if(getMessage.equals("Did you received key")){
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            RetrofitClass.notification("/change_booking_status_to_notify","owner received key",25);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            ratingAlertDialog();
                        }
                    }.execute();
                }
            }
        });
    }

    public void ratingAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(InAppNotification.this);
        LayoutInflater li = LayoutInflater.from(InAppNotification.this);
        View promptsView = li.inflate(R.layout.submit_rating, null);
        builder.setView(promptsView);
        RatingBar ratingBar = promptsView.findViewById(R.id.ratingBar);
        EditText comment = promptsView.findViewById(R.id.editText);

        Button next = promptsView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rating.setRating(ratingBar.getRating());
                if (!comment.getText().equals("")){
                    Rating.setComment(comment.getText().toString());
                }else {
                    Rating.setComment("null");
                }
                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {

                        isExist = RetrofitClass.rideRating("/book-rating");
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);

                        if (isExist){
                            Intent intent = new Intent(InAppNotification.this, MainActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                            finish();
                        }
                    }
                }.execute();

            }
        });
        // create alert dialog
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        // show it
        alertDialog.show();
    }
}
