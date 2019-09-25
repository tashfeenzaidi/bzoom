package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;

import javax.crypto.Cipher;

public class PasswordActivity extends AppCompatActivity {

    private Keystore store;//Holds our key pairs

    EditText password;
    String check;
    String getpass;
    int login_status;
    Button btn;
    int isSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        store = Keystore.getInstance(PasswordActivity.this);
        isSignup = store.getInt("signup");
        password = findViewById(R.id.password);
        btn =  findViewById(R.id.next);
        password.setOnFocusChangeListener((v, hasFocus) ->
                getpass = password.getText().toString());
        getpass = password.getText().toString();
        login_status = store.getInt("login_status");
        if (  isSignup != 1){
            btn.setText("Login");
        }else {
            btn.setText("Signup");
        }

        General.setUserUid(store.get("UID"));
        General.setRole(store.get("role"));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn.setOnClickListener(v -> {
            getpass = password.getText().toString();
            if(password.getText().toString().length() < 8)
            {
                password.setError("password must be 8 characters");
                return;
            }
            General.setUserPassword(getpass);
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    if(isSignup == 1){

                        RetrofitClass.postSignUp("/signup");

                    }else {

                        check =  RetrofitClass.postLogin("/login");

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (isSignup != 1){
                        if (check.equals("not fount") || check == null){
                            password.setError("wrong password");
                            return;
                        }else {
                            new AsyncTask<Void,Void,Void>(){
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    RetrofitClass.updateUid("/signup", General.getUserid());

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }.execute();
                            store.putString("userId",General.getUserid());
                            store.putInt("login_status",1);

                        }
                    }else {
                        store.putInt("signup",0);
                        store.putString("userId",General.getUserid());
                        store.putInt("login_status",1);
                        Intent intent = new Intent(PasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.execute();

        });




    }
}
