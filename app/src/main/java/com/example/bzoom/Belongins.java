package com.example.bzoom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Belongins extends AppCompatActivity {

    String role;
    Button agree;
    ArrayList<OwnerBelongings> belongingsArrayList;
    private RecyclerView recyclerView;
    TextView warning;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.belongins);

        agree = (Button) findViewById(R.id.agree);
        warning = (TextView) findViewById(R.id.warning);
        belongingsArrayList = new ArrayList<>();

        Intent intent = getIntent();
        role= intent.getStringExtra("role");
        if (Role.role.equals("chauffeur")){
            warning.setText("Please verify carefully");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        recyclerView = findViewById(R.id.belongings_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        BelongingsRecyclerViewAddapter belongingsRecyclerViewAddapter= new BelongingsRecyclerViewAddapter(fillList(),this);
        recyclerView.setAdapter(belongingsRecyclerViewAddapter);

        if (Role.role.equals("chauffeur")){
            agree.setText("Agreed");
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
                    builder.setMessage("Did you receive the keys?")
                    ;
                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            Intent intent = new Intent(Belongins.this,ChaufferSuccessActivity.class);
                            startActivity(intent);

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
        }else {
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
                    builder.setMessage("Chauffer has arrived to return your belongings, did you recive it?")
                    ;
                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button

                            AlertDialog.Builder builder = new AlertDialog.Builder(Belongins.this);
                            LayoutInflater li = LayoutInflater.from(Belongins.this);
                            View promptsView = li.inflate(R.layout.submit_rating, null);
                            builder.setView(promptsView);

                            // create alert dialog
                            AlertDialog alertDialog = builder.create();

                            // show it
                            alertDialog.show();

//                        Intent intent = new Intent(Belongins.this,MainActivity.class);
//                        startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            Intent intent = new Intent(Belongins.this,MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            });
        }


//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//
////              showDialog(RideStartActivity.this,R.string.dialog_message,"");
//
//            }
//
//        }, 2000L);

    }

    public ArrayList<OwnerBelongings> fillList(){


        OwnerBelongings ownerBelongings = new OwnerBelongings(true,"AC");
        OwnerBelongings ownerBelongings1 = new OwnerBelongings(false,"AC");
        OwnerBelongings ownerBelongings2 = new OwnerBelongings(true,"AC");
        OwnerBelongings ownerBelongings3 = new OwnerBelongings(false,"AC");
        OwnerBelongings ownerBelongings4 = new OwnerBelongings(true,"AC");
        OwnerBelongings ownerBelongings5 = new OwnerBelongings(false,"AC");
        belongingsArrayList.add(ownerBelongings);
        belongingsArrayList.add(ownerBelongings1);
        belongingsArrayList.add(ownerBelongings2);
        belongingsArrayList.add(ownerBelongings3);
        belongingsArrayList.add(ownerBelongings4);
        belongingsArrayList.add(ownerBelongings5);

        return belongingsArrayList;
    }
}
