package com.example.bzoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ReturnCarToOwner extends AppCompatActivity implements OnMapReadyCallback {

    TextView heading;
    Button returned;
    Button cancel;
    TextView returnkey;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_the_car);


        returned = (Button) findViewById(R.id.startride);
        returned.setText("i have returned");
        heading = (TextView) findViewById(R.id.heading);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        heading.setText("Returning to owner");
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        returned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ReturnCarToOwner.this);
                builder.setMessage("Did you returned the keys back to the owner?")
                ;
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                        AlertDialog.Builder builder = new AlertDialog.Builder(ReturnCarToOwner.this);
                        LayoutInflater li = LayoutInflater.from(ReturnCarToOwner.this);
                        View promptsView = li.inflate(R.layout.submit_rating, null);
                        builder.setView(promptsView);
                        TextView money =(TextView) promptsView.findViewById(R.id.money);
                        money.setVisibility(View.GONE);
                        ImageView wallet = (ImageView) promptsView.findViewById(R.id.wallet);
                        wallet.setVisibility(View.GONE);
                        TextView number =(TextView) promptsView.findViewById(R.id.number);
                        number.setVisibility(View.GONE);
                        ImageView distence = (ImageView) promptsView.findViewById(R.id.distence);
                        distence.setVisibility(View.GONE);
                        // create alert dialog
                        AlertDialog alertDialog = builder.create();

                        // show it
                        alertDialog.show();

                        Button submit = (Button) promptsView.findViewById(R.id.next);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ReturnCarToOwner.this,SelectOwnerListActivity.class);
                                startActivity(intent);
                            }
                        });

//                        Intent intent = new Intent(Belongins.this,MainActivity.class);
//                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
}
