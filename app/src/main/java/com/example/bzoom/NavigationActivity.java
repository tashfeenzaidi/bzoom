package com.example.bzoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    Button endRide;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        endRide = (Button) findViewById(R.id.view_peofile);

        if(Role.role.equals("rider")){
            ImageView imageView = (ImageView) findViewById(R.id.imageView3);
            removeView(imageView);
        }else {
            ImageView imageView = (ImageView) findViewById(R.id.navigate);
            removeView(imageView);
        }

        endRide.setBackgroundResource(R.drawable.btn);
        endRide.setTextColor(Color.parseColor("#ffffff"));
        endRide.setText("End Ride");
    }

    @Override
    protected void onStart() {
        super.onStart();

        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationActivity.this);
                        LayoutInflater li = LayoutInflater.from(NavigationActivity.this);
                        View promptsView = li.inflate(R.layout.cancel_reason, null);
                        final List<String> stringList=new ArrayList<>();  // here is list
                        for(int i=0;i<5;i++) {
                            stringList.add("RadioButton " + (i + 1));
                        }
                        RadioGroup rg = (RadioGroup) promptsView.findViewById(R.id.radio);

                        for(int i=0;i<stringList.size();i++){
                            RadioButton rb=new RadioButton(NavigationActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
                            rb.setText(stringList.get(i));
                            rg.addView(rb);
                        }
                        Button ok = (Button) promptsView.findViewById(R.id.ok);
                        Button cancel = (Button) promptsView.findViewById(R.id.cancel);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(NavigationActivity.this,AmountCollected.class);
                                startActivity(intent);
                            }
                        });

                        builder.setView(promptsView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public void removeView(View view){

        view.setVisibility(View.GONE);
    }
}
