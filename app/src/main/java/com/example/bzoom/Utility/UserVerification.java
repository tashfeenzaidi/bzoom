package com.example.bzoom.Utility;

import android.content.Context;

import com.example.bzoom.MainActivity;

public class UserVerification {

    Keystore store;
    Context cont;
    public UserVerification(Context context) {
        cont = context;
        store = Keystore.getInstance(context);
    }

    public boolean checkUser(){


        String user_status =  store.get("login_status");

        if(user_status != null && user_status.equals("1")){ return true;}

        return false;

    }
}
