package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.Utility.Utilities;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;

import java.lang.ref.WeakReference;

public class MobileActivity extends AppCompatActivity {

    private EditText number;
    Intent intent;
    private Keystore store;//Holds our key pairs
    static boolean isExist;
    Button btn;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
//        getSupportActionBar().hide();

        store = Keystore.getInstance(MobileActivity.this);
        number = findViewById(R.id.editText);
        //Utilities.showToast(MobileActivity.this,store.get("role"),1);
        btn = findViewById(R.id.next);

        intent = new Intent(MobileActivity.this,Otp.class);

    }

    @Override
    protected void onStart(){
        super.onStart();
        btn.setOnClickListener(v -> {

            mobile = number.getText().toString().trim();

            if(mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 10){
                number.setError("Enter a valid mobile");
                number.requestFocus();
                return;
            }

            General.userNumber = mobile;

            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    isExist = RetrofitClass.postPhoneNumber("/signup-number");
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (isExist){
                        number.setError("Mobile number already exist");
                        number.requestFocus();
                        return;
                    }else{
                        store.putString("phone_number",mobile);
                        startActivity(intent);
                    }
                }
            }.execute();
        });

    }

}
