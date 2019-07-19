package com.example.bzoom;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Keystore;

public class Login extends AppCompatActivity {
    Button chauffer;
    Button btn;
    Button rider;
    Button driver;
    Intent intent;
    private Keystore store;//Holds our key pairs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        store = Keystore.getInstance(Login.this);

        driver = (Button) findViewById(R.id.driver);

        btn = (Button) findViewById(R.id.owner);
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Role.role = "owner";
                startActivity();

            }
        });
        chauffer = (Button) findViewById(R.id.chauffeur);
        chauffer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Role.role = "chauffeur";
                startActivity();

            }
        });

        rider = (Button) findViewById(R.id.rider);
        rider.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Role.role = "rider";
                startActivity();

            }
        });

        driver.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Role.role = "driver";
                store.putString("role","rider");
                startActivity();
            }
        });


    }

    public void startActivity(){
        intent = new Intent(Login.this,MobileActivity.class);
        startActivity(intent);
    }
}
