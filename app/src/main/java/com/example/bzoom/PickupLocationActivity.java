package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PickupLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_location);

        Button button = (Button) findViewById(R.id.view_peofile);
        button.setText("Confirm Pickup");
        TextView textView = (TextView) findViewById(R.id.textView6);
        textView.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PickupLocationActivity.this,SelectTimeActivity.class);
                startActivity(intent);
            }
        });

    }
}
