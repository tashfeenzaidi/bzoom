package com.example.bzoom;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
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
        store.putInt("signup",0);
        driver =  findViewById(R.id.driver);

        btn =  findViewById(R.id.owner);
        btn.setOnClickListener(v -> {
            Role.role = "owner";
            store.putString("role","owner");
            General.setRole("owner");
            startActivity();


        });
        chauffer =  findViewById(R.id.chauffeur);
        chauffer.setOnClickListener(v -> {
            Role.role = "chauffeur";
            store.putString("role","chauffeur");
            General.setRole("chauffeur");

            startActivity();


        });

        rider =  findViewById(R.id.rider);
        rider.setOnClickListener(v -> {
            Role.role = "rider";
            store.putString("role","rider");
            General.setRole("rider");
            store.putInt("signup",1);
            startActivity();
        });

        driver.setOnClickListener(v -> {
            Role.role = "driver";
            store.putString("role","driver");
            General.setRole("driver");
            startActivity();

        });




    }

    public void startActivity(){
        intent = new Intent(Login.this,MobileActivity.class);
        startActivity(intent);
    }

}
