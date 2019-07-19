package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;

public class MobileActivity extends AppCompatActivity {

    private EditText number;
    Intent intent;
    private Keystore store;//Holds our key pairs
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
//        getSupportActionBar().hide();

        store = Keystore.getInstance(MobileActivity.this);
        number = findViewById(R.id.editText);
        Utilities.showToast(MobileActivity.this,store.get("role"),1);
        Button btn = (Button) findViewById(R.id.next);

        if(Role.role.equals("driver")){
            intent = new Intent(MobileActivity.this,Otp.class);
        }else{
            intent = new Intent(MobileActivity.this,PasswordActivity.class);
        }
        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String mobile = number.getText().toString().trim();

                if(mobile.isEmpty() || mobile.length() < 10){
                    number.setError("Enter a valid mobile");
                    number.requestFocus();
                    return;
                }
                store.putString("phone_number",mobile);
                startActivity(intent);
            }
        });

    }
}
