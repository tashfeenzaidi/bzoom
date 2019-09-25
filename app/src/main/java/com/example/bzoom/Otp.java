package com.example.bzoom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.bzoom.Utility.General;
import com.example.bzoom.Utility.Keystore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Otp extends AppCompatActivity {


    private Keystore store;//Holds our key pairs
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private PinView editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_otp);

        store = Keystore.getInstance(Otp.this);
        //initializing objects
        mAuth = FirebaseAuth.getInstance();


        editTextCode = findViewById(R.id.pinView);


        sendVerificationCode(store.get("phone_number"));

        Button btn =  findViewById(R.id.next);
        btn.setOnClickListener(v -> {

            String code = Objects.requireNonNull(editTextCode.getText()).toString();
            if (code.isEmpty() || code.length() < 6) {
                editTextCode.setError("Enter valid code");
                editTextCode.requestFocus();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code);

        });
    }


    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Otp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Otp.this, task -> {
                    if (task.isSuccessful()) {
                        //verification successful we will start the profile activity
                        Intent intent = new Intent(Otp.this, Verified.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        General.setUserUid(uid);
                        store.putString("UID",uid);
                        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                        mUser.getIdToken(true)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        String idToken = task1.getResult().getToken();
                                        store.putString("userToken",idToken);
                                        General.setUserToken(idToken);
                                        // Send token to your backend via HTTPS
                                        // ...
                                    } else {
                                        // Handle error -> task.getException();
                                    }
                                });

                        startActivity(intent);
                        finish();

                    } else {

                        //verification unsuccessful.. display an error message
                        String message = "Somthing is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }
                        Toast.makeText(Otp.this, message, Toast.LENGTH_LONG).show();

//                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
//                            snackbar.setAction("Dismiss", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                    }
                });
    }
}
