package com.example.portal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Jerome Lieow on 14/01/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "com.example.portal";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if(remoteMessage.getData().toString().equals("{From=DriverToCO}")) {
                DriverToCO();
            }
            if(remoteMessage.getData().toString().equals("{From=InformDriverMTRACComplete}")) {
                InformDriverMTRACComplete();
            }
            if(remoteMessage.getData().toString().equals("{From=CSOToVC}")) {
                CSOToVC();
            }
            if(remoteMessage.getData().toString().equals("{From=CSOReject}")) {
                CSOReject();
            }
            if(remoteMessage.getData().toString().equals("{From=VCReject}")) {
                VCReject();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainPortal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }

    private void DriverToCO() {
        Intent intent = new Intent(this, Countersigning_Officer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                .setContentTitle("Countersigning Officer")
                .setContentText("Please review the MT-RAC submitted to you.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(2 /* ID of notification */, notificationBuilder.build());
    }

    private void InformDriverMTRACComplete() {
        Intent intent = new Intent(this, Your_MTRAC.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                .setContentTitle("Driver")
                .setContentText("Your MT-RAM has been approved.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(3 /* ID of notification */, notificationBuilder.build());
    }

    private void CSOToVC() {
        Intent intent = new Intent(this, Vehicle_Commander.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                .setContentTitle("Vehicle Commander")
                .setContentText("Please review the MT-RAC submitted to you.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(4 /* ID of notification */, notificationBuilder.build());
    }

    private void CSOReject() {
        Intent intent = new Intent(this, Your_MTRAC.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                .setContentTitle("MT-RAC Rejected")
                .setContentText("Your MT-RAC has been rejected by the Vehicle Commander.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(4 /* ID of notification */, notificationBuilder.build());
    }

    private void VCReject() {
        Intent intent = new Intent(this, Your_MTRAC.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.jeep)
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                .setContentTitle("MT-RAC Rejected")
                .setContentText("Your MT-RAC has been rejected by the Vehicle Commander.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(4 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
