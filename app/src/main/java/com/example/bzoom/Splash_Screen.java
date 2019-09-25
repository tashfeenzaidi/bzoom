package com.example.bzoom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.UserVerification;

public class Splash_Screen extends AppCompatActivity {


    Intent mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        Keystore keystore = Keystore.getInstance(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            mainIntent = new Intent(Splash_Screen.this,Login.class);
            keystore.putInt("login_status",3);
            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }else {
            int login_status = keystore.getInt("login_status");

            if(login_status != 3){
                if(login_status == 0) {
                    mainIntent = new Intent(Splash_Screen.this,Email.class);
                }else {
                    mainIntent = new Intent(Splash_Screen.this,MainActivity.class);
                }
            }else {
                mainIntent = new Intent(Splash_Screen.this,Login.class);
            }

        }

        new Handler().postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            startActivity(mainIntent);
            Splash_Screen.this.finish();
        }, 3000);

    }

}
