package com.example.portal;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Result;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class OpeningPage extends AppCompatActivity {

    private static final String TAG = "portal";

    ImageView PortalSymbol;

    String OutsystemsUsername, OutsystemsFullName, IdPushNotification;
    int OutsystemsLoginStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_page);

        PortalSymbol=(ImageView)findViewById(R.id.PortalSymbol);

        //Used for fade_in animation
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        PortalSymbol.startAnimation(animationFadeIn);
        //------------------------------------------

        //ActionBar Settings
//        getSupportActionBar().hide();   //Hides ActionBar //Cannot hide action bar when using SplashScreenTheme

        new thread().execute();
    }

    //Code to run seperate thread
    public class thread extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { //Executes before background task
            Log.i(TAG, "onPreExecute");
        }
        @Override
        protected Void doInBackground(Void... params) { //Executes background task
            Log.i(TAG, "doInBackground");

            Log.i(TAG, "CheckConnection: " +CheckConnection());

            if (CheckConnection()==true){ //Checks if there is a Connection
                CheckLogin(); // Check if User is logged in from Server
            }
            else {  //No Connection
                MainLogin();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    public void CheckLogin(){
        Log.i(TAG, "Sending GET request to Apache Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByToken?Token=" + FirebaseInstanceId.getInstance().getToken();  //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v(TAG, "Response= "+response.toString());

                try {
                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        IdPushNotification = json.getString("Id");
                        OutsystemsUsername = json.getString("LoginUsername");
                        OutsystemsFullName = json.getString("FullName");
                        OutsystemsLoginStatus = Integer.parseInt(json.getString("Login").trim());

                        Log.v(TAG, "Id: " +IdPushNotification);
                        Log.v(TAG, "OutsystemsLoginStatus: " +OutsystemsLoginStatus);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                    MainLogin();
                }

                Log.v(TAG, "OutsystemsLoginStatus: " +OutsystemsLoginStatus);

                if (OutsystemsLoginStatus==1){  //Checks if LoginStatus is "1", User has logged in, go to MainPortal
                    MainPortal();   //Goes to MainPortal
                }
                else {  //Checks if LoginStatus is "0", User has logged out, go to MainLogin
                    MainLogin();   //Goes to MainLogin
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainLogin();
                return;
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void MainLogin(){   //Goes to MainLogin
        Log.i(TAG, "User is not logged in, proceed into MainLogin");

        Intent MainLogin = new Intent(this, com.example.portal.MainLogin.class);
        startActivity(MainLogin);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        finish();
    }

    public void MainPortal(){   //Goes to MainLogin
        Log.i(TAG, "User is logged in, proceed into MainPortal");

        Intent mtram = new Intent(this, com.example.portal.mtram.class);
        startActivity(mtram);

//        Intent MainPortal = new Intent(this, com.example.portal.MainPortal.class);
//        startActivity(MainPortal);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        finish();
    }

    public boolean CheckConnection(){   //Checks for Network/Wifi connection
        //Checks WIFI and Network States------------------------------------------------------------------------
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();

        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        mobileInfo.getState();

        Log.i(TAG, "Checking Wifi State: " +wifiEnabled);
        Log.i(TAG, "Checking Network State: " +mobileInfo.isConnected());

        if (wifiEnabled==true || mobileInfo.isConnected()==true){ //Checks if there is a Connection
            return true;
        }
        else {  //No Connection
            return false;
        }
    }

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
