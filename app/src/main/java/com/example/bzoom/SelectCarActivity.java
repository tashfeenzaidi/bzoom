package com.example.bzoom;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SelectCarActivity extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper viewFlipper;
    ImageView pre;
    ImageView next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_car_activity);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        pre = (ImageView) findViewById(R.id.pre);
        next = (ImageView) findViewById(R.id.next);
        pre.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == pre){
            viewFlipper.showPrevious();
        }
        else {
            viewFlipper.showNext();

        }
    }
}
