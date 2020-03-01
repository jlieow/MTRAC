package com.example.portal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Jerome Lieow on 15/01/2017.
 */

public class DriverNotificationDisplay extends Service {

    final int NOTIFICATION_ID = 16;

    public DriverNotificationDisplay() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        displayNotification("Vehicle Commander", "Please review the MT-RAC submitted to you.");
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text) {
        Intent resultIntent = new Intent(this, Vehicle_Commander.class);
        //resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent piResult = PendingIntent.getActivity(this, 0, resultIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.mtram)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name))
                .setColor(getResources().getColor(R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 300, 300}) // 0 acts as delay, 300 acts as time of vibration, 300 acts as delay, 300 acts as time of vibration
                //.setSound()
                .setLights(Color.WHITE, 1000, 5000)
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(piResult)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
