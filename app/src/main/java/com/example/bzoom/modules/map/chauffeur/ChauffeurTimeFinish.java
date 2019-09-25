package com.example.bzoom.modules.map.chauffeur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bzoom.Utility.Keystore;
import com.example.bzoom.modal.firebase.AvailableCar;
import com.example.bzoom.modal.firebase.Firebase;

public class ChauffeurTimeFinish extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AvailableCar.status = "timeEnd";
        Firebase.unsubscribeFromTopic(Keystore.getInstance(context).get("chauffeurTopicSubs"));
    }
}
