package com.example.bzoom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DriverConnected extends AppCompatActivity {

    TextView counter;
    Button cancel;
    Button contact;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_connected);


        contact = (Button) findViewById(R.id.contact);
        cancel = (Button) findViewById(R.id.cancel);

        counter = (TextView) findViewById(R.id.counter);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                counter.setText(millisUntilFinished / 1000+" s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                counter.setText(" ");
                Intent intent = new Intent(DriverConnected.this,NavigationActivity.class);
                intent.putExtra("activity","driverconnected");
                startActivity(intent);
            }

        }.start();



    }

    @Override
    protected void onStart() {
        super.onStart();

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+923072384734"));
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverConnected.this.finish();
            }
        });
    }
}
