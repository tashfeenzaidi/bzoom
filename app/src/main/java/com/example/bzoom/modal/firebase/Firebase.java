package com.example.bzoom.modal.firebase;

import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bzoom.Constants;
import com.example.bzoom.MainActivity;
import com.example.bzoom.Utility.Keystore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Firebase {

    private final String driver = "driver";
    private final String rider = "rider";
    private final String available = "available";
    private static final FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
    private final DatabaseReference mDatabase  = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDriver;
    private DatabaseReference mAvailable;
    private DatabaseReference mRider;
    Keystore keystore;
    static String idToken;

    public Firebase(){
    }


    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public DatabaseReference getmDriver() {
        mDriver = mDatabase.child(driver);
        return mDriver;
    }

    public DatabaseReference getmAvailable() {
        mAvailable = mDatabase.child(available);
        return mAvailable;
    }

    public DatabaseReference getmRider() {
        mDriver = mDatabase.child(rider);
        return mDriver;
    }

    public static String userToken(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        idToken = task1.getResult().getToken();
                        // Send token to your backend via HTTPS
                        // ...
                    } else {
                        // Handle error -> task.getException();
                    }
                });
        return idToken;
    }

    public static String getCurrentToken(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    // Get new Instance ID token
                    idToken = task.getResult().getToken();
                });
        return idToken;
    }

    public static boolean subscribeToTopic(String topic){

        firebaseMessaging.subscribeToTopic(topic)
                .addOnCompleteListener(task -> {
                    String msg = "failed";
                    if (!task.isSuccessful()) {
                       msg ="succeeded";
                       return;
                    }

                });
        return true;
    }

    public static void unsubscribeFromTopic(String topic){
        firebaseMessaging.unsubscribeFromTopic(topic);
    }

    public static void sendMessage(String topic,String token){

        FirebaseMessaging.getInstance().send(
                new RemoteMessage.Builder(topic)
                        .setMessageId("")
                        .addData("token", token)
                        .build());

    }

    public static void conditionSubscription(String token){
        // Define a condition which will send to devices which are subscribed
// to either the Google stock or the tech industry topics.
//        String condition = "'stock-GOOG' in topics || 'industry-tech' in topics";
//
//// See documentation on defining a message payload.
//
//
        RemoteMessage rm = new RemoteMessage.Builder("test")
                .addData("message", "Hello")
                .build();
        FirebaseMessaging.getInstance().send(rm);

    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static void sendNotification(String topic,JSONObject payload) {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json=new JSONObject();
                    JSONObject dataJson=new JSONObject();
                    dataJson.put("body",topic);
                    dataJson.put("title",topic+" request");
                    json.put("notification",dataJson);
                    json.put("to","/topics/"+topic);
                    json.put("data",payload);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization","key="+ Constants.LEGACY_SERVER_KEY)
                            .url("https://fcm.googleapis.com/fcm/send")
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                }catch (Exception e){
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();

    }
}
