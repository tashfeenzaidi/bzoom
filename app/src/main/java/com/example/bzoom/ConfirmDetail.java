package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmDetail extends AppCompatActivity {

    Button bzoom;
    private String activity;
    TableRow tableRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_confirm_detail);


        tableRow = (TableRow) findViewById(R.id.row1);
        if (getIntent().getExtras() != null){
            activity =getIntent().getExtras().getString("from");
            if (activity != null){
                if (activity.equals("rent")){

                    tableRow.setVisibility(View.VISIBLE);
                }
            }
        }


        bzoom = (Button) findViewById(R.id.next);
        bzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmDetail.this,FindingActivity.class);
                startActivity(intent);
            }
        });
    }
}
