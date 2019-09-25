package com.example.bzoom;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.modules.map.chauffeur.Chauffeur;

public class ChauffeurProfile extends AppCompatActivity {

    RatingBar ratingbar;
    TextView name;
    TextView number;
    TextView rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffeur_profile);
        ratingbar= findViewById(R.id.ratingBar);
        ratingbar.setRating(4); // set default rating
        name = findViewById(R.id.name);
        number = findViewById(R.id.txtph);
        rating = findViewById(R.id.textView3);
        ImageView back =  findViewById(R.id.imageView3);
        initializeUI();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initializeUI(){
        name.setText(Chauffeur.getName());
        number.setText(Chauffeur.getNumber());
        rating.setText(Chauffeur.getRating());
        if (Chauffeur.getRating() != null){
            ratingbar.setRating(Float.parseFloat(Chauffeur.getRating()));

        }
    }
}
