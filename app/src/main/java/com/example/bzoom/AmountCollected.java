package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.modal.firebase.Ride;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;

public class AmountCollected extends AppCompatActivity {

    Button next;
    TextView amount_collected;
    TextView amount;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_collected);

        String amountTaken = String.valueOf(Ride.getFair());
        amount_collected = findViewById(R.id.amount_collected);
        amount = findViewById(R.id.amount);
        amount.setText(amountTaken);
        next =  findViewById(R.id.next);
        amount_collected.setHint(amountTaken);
        next.setOnClickListener(v -> {
            if (amount_collected.getText().equals(" ")){
                return;
            }
            Ride.setAmountCollected(amount_collected.getText().toString());
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    RetrofitClass.collectedAmount("/ride-collect-amount");
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Intent intent = new Intent(AmountCollected.this,TripEnd.class);
                    intent.putExtra("activity","amountCollected");
                    startActivity(intent);
                }
            }.execute();
        });
    }
}
