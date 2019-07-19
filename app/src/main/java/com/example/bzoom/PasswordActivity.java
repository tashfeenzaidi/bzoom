package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);


        Button btn = (Button) findViewById(R.id.next);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(Role.role.equals("owner")){
                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                if(Role.role.equals("chauffeur")){
                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                if(Role.role.equals("rider")){
                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
}
