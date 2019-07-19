package com.example.bzoom;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class ChaufferRideStart extends AppCompatActivity implements OnMapReadyCallback {

    TextView taskbar_heading;
    Button start_ride;
    ImageView imageView ;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_the_car);

        taskbar_heading = (TextView) findViewById(R.id.heading);
        taskbar_heading.setText("Now drive to Jamal");
        start_ride = (Button) findViewById(R.id.startride);
        start_ride.setText("Start ride");
        imageView = (ImageView) findViewById(R.id.textView2);
        imageView.setVisibility(View.GONE);
        RelativeLayout releativelayout = (RelativeLayout) findViewById(R.id.uppercard);
        textView = new TextView(this);
        final RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        int margin = getResources().getDimensionPixelSize(R.dimen._15sdp);
        params.setMargins(0,0,margin,0);
        textView.setTextSize(9);
        textView.setLayoutParams(params);
        String  start_time = "<font color='#FC8E11'>8pm</font>";
        String  end_time = "<font color='#FC8E11'>8pm</font>";
        Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto);
        textView.setTypeface(typeface);
        textView.setText(Html.fromHtml("Wanted from "+start_time+" to "+end_time));
        releativelayout.addView(textView);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ChaufferRideStart.this,ReturnCarToOwner.class);
                startActivity(intent);
            }
        },1000L);
    }
}
