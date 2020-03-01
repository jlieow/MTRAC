package com.example.portal;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jerome Lieow on 15/01/2017.
 */

public class BackgroundNotificationService extends IntentService {

    private static final String TAG = "com.example.portal";

    int DriverNotification = 0;
    int VehCommNotification = 0;
    int CountersigningOfficerNotification = 0;


    public BackgroundNotificationService() {
        super("BackgroundNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "The service has now started");

        while (true){  //Infinite Loop
            Log.i(TAG, "In the loop");

            GetDataFirebaseDriverNotification();

            if (DriverNotification==1){
                Intent DriverNotificationDisplay = new Intent(this, DriverNotificationDisplay.class);
                startService(DriverNotificationDisplay);
                PutDataFirebaseDriverNotification();
                DriverNotification = 0; //Resets checking so that its not captured accidentally
                Log.i(TAG, "Driver Notification");
            }

            GetDataFirebaseVehCommNotification();

            if (VehCommNotification==1){
                Intent VehCommNotificationDisplay = new Intent(this, VehCommNotificationDisplay.class);
                startService(VehCommNotificationDisplay);
                PutDataFirebaseVehCommNotification();
                VehCommNotification = 0; //Resets checking so that its not captured accidentally
                Log.i(TAG, "VehComm Notification");
            }

            GetDataFirebaseCountersigningOfficerNotification();

            if (CountersigningOfficerNotification==1){
                Intent CountersigningOfficerNotificationDisplay = new Intent(this, CountersigningOfficerNotificationDisplay.class);
                startService(CountersigningOfficerNotificationDisplay);
                PutDataFirebaseCountersigningOfficerNotification();
                CountersigningOfficerNotification = 0; //Resets checking so that its not captured accidentally
                Log.i(TAG, "CountersigningOfficer Notification");
            }

            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

        }

    }

    public void GetDataFirebaseDriverNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the MT-RAC-Driver child items in the database
        final DatabaseReference getDriverNotificationRef = database.getReference("Portal").child("MTRAM-Driver").child("DriverNotification");

        getDriverNotificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DriverNotification = Integer.valueOf(dataSnapshot.getValue().toString());
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {return;}
            public void onChildRemoved(DataSnapshot dataSnapshot) {return;}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {return;}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void PutDataFirebaseDriverNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the child of the MT-RAC-Driver child items it the database
        final DatabaseReference putDriverNotificationRef = database.getReference("Portal").child("MTRAM-Driver").child("DriverNotification").child("NotificationCheck");
//        final DatabaseReference DriverRef = database.getReference("MT-RAC-Driver");

        // Set the child's data to the value passed in from StructureDriverData()
        putDriverNotificationRef.setValue("0");
    }

    public void GetDataFirebaseVehCommNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the MT-RAC-Driver child items in the database
        final DatabaseReference getVehCommNotificationRef = database.getReference("Portal").child("MTRAM-VehComm").child("VehCommNotification");

        getVehCommNotificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                VehCommNotification = Integer.valueOf(dataSnapshot.getValue().toString());
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {return;}
            public void onChildRemoved(DataSnapshot dataSnapshot) {return;}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {return;}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void PutDataFirebaseVehCommNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the child of the MT-RAC-Driver child items it the database
        final DatabaseReference putVehCommNotificationRef = database.getReference("Portal").child("MTRAM-VehComm").child("VehCommNotification").child("NotificationCheck");
//        final DatabaseReference DriverRef = database.getReference("MT-RAC-Driver");

        // Set the child's data to the value passed in from StructureDriverData()
        putVehCommNotificationRef.setValue("0");
    }

    public void GetDataFirebaseCountersigningOfficerNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the MT-RAC-Driver child items in the database
        final DatabaseReference getCountersigningOfficerNotificationRef = database.getReference("Portal").child("MTRAM-CountersigningOfficer").child("CountersigningOfficerNotification");

        getCountersigningOfficerNotificationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CountersigningOfficerNotification = Integer.valueOf(dataSnapshot.getValue().toString());
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {return;}
            public void onChildRemoved(DataSnapshot dataSnapshot) {return;}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {return;}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void PutDataFirebaseCountersigningOfficerNotification() {

        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the child of the MT-RAC-Driver child items it the database
        final DatabaseReference putCountersigningOfficerNotificationRef = database.getReference("Portal").child("MTRAM-CountersigningOfficer").child("CountersigningOfficerNotification").child("NotificationCheck");
//        final DatabaseReference DriverRef = database.getReference("MT-RAC-Driver");

        // Set the child's data to the value passed in from StructureDriverData()
        putCountersigningOfficerNotificationRef.setValue("0");
    }

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
