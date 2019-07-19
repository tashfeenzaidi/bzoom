package com.example.bzoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import pl.tajchert.waitingdots.DotsTextView;

public class ChaufferSuccessActivity extends Activity {

    TextView text;
    DotsTextView dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chauffer_success);

        text = (TextView) findViewById(R.id.text);
        dots = (DotsTextView) findViewById(R.id.dots);
        final Handler handler = new Handler();
        dots.start();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Role.status = true;
                Intent intent = new Intent(ChaufferSuccessActivity.this,MainActivity.class);
                startActivity(intent);

            }

        }, 2000L);

    }

//    public void setvisibilityGone(){
//        text.setVisibility(View.GONE);
//
//    }
}
