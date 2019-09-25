package com.example.bzoom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.retrofit.RetrofitClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;

public class Email extends AppCompatActivity {

    EditText email ;
    boolean isExist;
    private Keystore store;//Holds our key pairs
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_email);

        store = Keystore.getInstance(Email.this);
        email = findViewById(R.id.email);
        btn =  findViewById(R.id.next);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn.setOnClickListener(v -> {
            int login_status = store.getInt("login_status");
            String getEmail = email.getText().toString().trim();

            if (!isEmailValid(getEmail)) {
                email.setError("Invalid Email Address");
                return;
            }
            General.setRole(store.get("role"));
            General.setUserEmail(getEmail);

                new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {

                    isExist = RetrofitClass.verifyEmail("/signup-email",getEmail);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    if (isExist){
                        if (store.getInt("signup") == 1){
                            if (login_status == 3 || login_status == 1){
                                email.setError("Email address already exist");
                                return;
                            }
                        }

                        store.putString("email",getEmail);
                        Intent intent = new Intent(Email.this,PasswordActivity.class);
                        startActivity(intent);
                        finish();

                    }else {

                        if (store.getInt("signup") == 1){
                            store.putString("email",getEmail);
                            Intent intent = new Intent(Email.this,PasswordActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            email.setError("Email address does not exist.");
                        }

                    }

                }
            }.execute();
        });

    }

    public boolean isEmailValid(String email){
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
