package com.example.bzoom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;
import com.example.bzoom.modules.map.DisagreeAgreement;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Belongins extends AppCompatActivity {

    String role;
    Button agree;
    public static ArrayList<OwnerBelongings> belongingsArrayList;
    private RecyclerView recyclerView;
    TextView warning;
    Keystore keystore;
    Button disagree;
    private boolean isExist;
    private boolean isEisxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.belongins);

        agree =  findViewById(R.id.agree);
        disagree =  findViewById(R.id.disagree);
        warning =  findViewById(R.id.warning);
        belongingsArrayList = new ArrayList<>();
        keystore = Keystore.getInstance(this);

        Intent intent = getIntent();
        role= intent.getStringExtra("role");
        if (Role.role.equals("chauffeur")){
            warning.setText("Please verify carefully");
        }
        agree.setText("Agreed");
    }

    @Override
    protected void onStart() {
        super.onStart();


        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                if (keystore.get("role").equals("owner")){
                    belongingsArrayList =  RetrofitClass.getAgreementByVehical("/get-agreemnt-by-vehicals/", AvailableCar.activeVehicalId,1);
                }else if (keystore.get("role").equals("chauffeur")){
                    belongingsArrayList =  RetrofitClass.getAgreementByVehical("/get-agreemnt-by-vehicals/", AvailableCar.activeVehicalId,0);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerView = findViewById(R.id.belongings_recycler);
                recyclerView.setLayoutManager(new GridLayoutManager(Belongins.this,1));
                BelongingsRecyclerViewAddapter belongingsRecyclerViewAddapter= new BelongingsRecyclerViewAddapter(belongingsArrayList,Belongins.this,true);
                recyclerView.setAdapter(belongingsRecyclerViewAddapter);
            }
        }.execute();

//        if (Role.role.equals("chauffeur")){
//            agree.setText("Agreed");
//
//            agree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//
//                    new AsyncTask<Void,Void,Void>(){
//                        @Override
//                        protected Void doInBackground(Void... voids) {
//
//                            isExist = RetrofitClass.notification("/change_booking_status_to_notify",getString(R.string.agreement_accepted),17);
//                            return null;
//                        }
//
//                        @Override
//                        protected void onPostExecute(Void aVoid) {
//                            super.onPostExecute(aVoid);
//                            if (isExist){
//
//                            }
//
//                        }
//                    }.execute();
//
////                    AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
////                    builder.setMessage("Did you receive the keys?")
////                    ;
////                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int id) {
////                            // User clicked OK button
////
////                            Intent intent = new Intent(Belongins.this,ChaufferSuccessActivity.class);
////                            startActivity(intent);
////
//////                        Intent intent = new Intent(Belongins.this,MainActivity.class);
//////                        startActivity(intent);
////                        }
////                    });
////                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
////                        public void onClick(DialogInterface dialog, int id) {
////                            // User clicked OK button
////
////                        }
////                    });
////                    AlertDialog dialog = builder.create();
////                    dialog.setCanceledOnTouchOutside(false);
////                    dialog.show();
//                }
//            });
//        }else {
//            agree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
//                    builder.setMessage("Chauffer has arrived to return your belongings, did you recive it?")
//                    ;
//                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//
//                            AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
//                            LayoutInflater li = LayoutInflater.from(Belongins.this);
//                            View promptsView = li.inflate(R.layout.submit_rating, null);
//                            builder.setView(promptsView);
//
//                            // create alert dialog
//                            AlertDialog alertDialog = builder.create();
//
//                            // show it
//                            alertDialog.show();
//
////                        Intent intent = new Intent(Belongins.this,MainActivity.class);
////                        startActivity(intent);
//                        }
//                    });
//                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User clicked OK button
//                            Intent intent = new Intent(Belongins.this,MainActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.setCanceledOnTouchOutside(false);
//                    dialog.show();
//                }
//            });
//        }

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (keystore.get("role").equals("chauffeur")){

                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {

                            isExist = RetrofitClass.notification("/change_booking_status_to_notify",getString(R.string.agreement_accepted),17);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (isExist){

                            }

                        }
                    }.execute();
                }
                else if (keystore.get("role").equals("owner")){
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected Void doInBackground(Void... voids) {
                            isEisxt = RetrofitClass.notification("/change_booking_status_to_notify","Return key to owner",24);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (isEisxt){
                            }
                        }
                    }.execute();

                }
            }
        });

        disagree.setOnClickListener(v -> {
            Intent intent = new Intent(Belongins.this, DisagreeAgreement.class);
            startActivity(intent);

        });
    }

}
