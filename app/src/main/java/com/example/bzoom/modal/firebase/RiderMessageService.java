package com.example.bzoom.modal.firebase;


import android.content.Intent;
import android.util.Log;
import com.example.bzoom.DriverConnected;
import com.example.bzoom.RequestAlertDialogActivity;
import com.example.bzoom.Utility.General;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

public class RiderMessageService extends MessageService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);



        if (remoteMessage.getData().size() > 0) {
            message(remoteMessage);
        }
    }

    private void message(RemoteMessage remoteMessage){

        String topic = remoteMessage.getFrom();

//        String lonDrop = remoteMessage.getData().get("lon_drop");
//        String customerID = remoteMessage.getData().get("customer");
//        String rideID = remoteMessage.getData().get("ride_id");
//        if (topic.equals("/topics/driver")){
//           double distance = MapUtility.distanceBetweenTwoPoint(MainActivity.currentLocation.getLatitude(),MainActivity.currentLocation.getLongitude(),Double.parseDouble(lat) ,Double.parseDouble(lon));
//           if (distance <= 5){
//               sendNotification();
//           }else return;
//        }
        String uId = "/topics/"+General.getUserUid();
        if (topic != null){
            if (topic.equals("/topics/rider")){
                Intent intent = new Intent(getApplication(), DriverConnected.class);
                getApplicationContext().startActivity(intent);
            }

            if (topic.equals("/topics/driver") ){

                String body =    remoteMessage.getNotification().getBody();
                String to =  remoteMessage.getTo();
                String from =  remoteMessage.getFrom();

                sendNotification();
//               double distance = MapUtility.distanceBetweenTwoPoint(Double.parseDouble(latDrop),Double.parseDouble(lonDrop),Double.parseDouble(lat) ,Double.parseDouble(lon));
//               if (distance <= 5){
//                   sendNotification();
//               }else return;
            }
            if (topic.equals("/topics/"+General.getUserUid())){

                String body =    remoteMessage.getNotification().getBody();
                String to =  remoteMessage.getTo();
                String from =  remoteMessage.getFrom();
                sendNotification();
            }



        }



//        String status = keystore.get(getString(R.string.rid_status));
//        if (status.equals(getString(R.string.occupy))){
//            return;
//        }
//        sendNotification();
    }
    private void sendNotification() {
        Intent intent = new Intent(getApplication(), RequestAlertDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        long[] pattern = {500,500,500,500,500};
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.icon)
//                .setContentTitle(messageTitle)
//                .setContentText(messageBody)
//                .setVibrate(pattern)
//                .setLights(Color.BLUE,1,1)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        getApplicationContext().startActivity(intent);
    }

}
