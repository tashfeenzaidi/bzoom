package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.Firebase;

import java.security.Key;

public class Name extends AppCompatActivity {

    Keystore keystore;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_fullname);
        EditText name =  findViewById(R.id.editText);
        Button btn =  findViewById(R.id.next);
        keystore = Keystore.getInstance(this);
        btn.setOnClickListener(v -> {

            String Name = name.getText().toString().trim();
            if (Name.equals("") || Name.isEmpty()){
                name.setError("enter name");
            }else {
                keystore.putString("name",Name);
                General.userName = Name;
                Intent intent = new Intent(Name.this,Email.class);
                startActivity(intent);
            }

        });
    }
}
