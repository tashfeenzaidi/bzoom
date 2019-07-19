package com.example.bzoom;

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

public class TripEnd extends AppCompatActivity {

    Button ratenow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_ended);

        ratenow = (Button) findViewById(R.id.next);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ratenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TripEnd.this);
                LayoutInflater li = LayoutInflater.from(TripEnd.this);
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
                        Intent intent = new Intent(TripEnd.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
