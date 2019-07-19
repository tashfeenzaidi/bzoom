package com.example.bzoom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChauffeurProfile extends AppCompatActivity {

    RatingBar ratingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffeur_profile);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        ratingbar.setRating(4); // set default rating


        ImageView back = (ImageView) findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
