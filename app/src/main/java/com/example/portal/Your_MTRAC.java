package com.example.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Your_MTRAC extends AppCompatActivity {

    private static final String TAG = "portal";

    //Variable Declarations
    TextView NoMTRAC;

    String JSONobj, getFullName;
    String GetIDform, GetDriver, GetVehicleNumber, GetRiskLevel;
    int GetCompletion, count;
    ListView ListForms;
    ArrayList<CustomListViewMTRAC> ArrayListMTRAMforms = new ArrayList<>();

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_mtrac);

        //Sets ProgressDialog to show only loading animation
        progressanim = new ProgressDialog(this);
        try {
            progressanim.show();
        } catch (WindowManager.BadTokenException e) {}
        progressanim.setContentView(R.layout.progressbaranim);
        progressanim.setCancelable(true);
        progressanim.setCanceledOnTouchOutside(false);
        progressanim.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressanim.setContentView(R.layout.progressbaranim);
        progressanim.show();
        //--------------------------------------------------

        //ActionBar Settings
//        getSupportActionBar().hide();   //Hides ActionBar
        getSupportActionBar().setTitle(""); //Change title of Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Set whether home should be displayed as an "up" affordance.

        //Assigns ListView to widgets
        ListForms=(ListView)findViewById(R.id.ListForms);

        //Assigns TextView to widgets
        NoMTRAC=(TextView)findViewById(R.id.NoMTRAC);

        //Set OnClick for ListView item to go next page
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener(){ //Configure OnClickListener for ListView
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //Set OnClickListener for each item in ListView

                Log.v(TAG, "CheckBox Get Full Name " +getFullName);

                CustomListViewMTRAC MTRACform = ArrayListMTRAMforms.get(position); //Custom database to store Name and Risk

                Intent Countersigning_Officer_Review = new Intent(Your_MTRAC.this, Your_MTRAC_Review.class);
                Countersigning_Officer_Review.putExtra("IDform", MTRACform.getIDform()); //Pass IDform to intent

                startActivity(Countersigning_Officer_Review);

                finish();
            }
        };

        ListForms.setOnItemClickListener(adapterViewListener); //Set ListView to OnClickListener

        count = 0;

        if (CheckConnection()==true){ //Checks if there is a Connection
            new thread().execute();
        }
        else {  //No Connection
            String Title = "Something went wrong!";
            String Message = "No Internet Connection Detected.";
            String ButtonText = "OK";
            NoConnectionalertDialog(Title, Message, ButtonText);
        }
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
            getFullName();  //Gets Full Name and uses it to set up the ListView
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    public void getFullName(){
        Log.i(TAG, "Checking Full Name");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByToken?Token=" + FirebaseInstanceId.getInstance().getToken(); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());
                JSONobj = response.toString().replaceAll("[\\[\\]]", "");

                try {
                    Log.i(TAG, "Get Full Name");

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        getFullName = json.getString("FullName");
                        Log.v(TAG, getFullName);

                        getList();
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }

                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void getList(){
        final ArrayAdapter<CustomListViewMTRAC> MTRAMforms = new CustomListViewMTRACAdapter(this, 0, ArrayListMTRAMforms); //Create Custom Adapter for ListView using ArrayList

        Log.i(TAG, "Checking List for " + getFullName.replaceAll(" ", "%20").trim());

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/FindByDriver?Driver="  + getFullName.replaceAll(" ", "%20").trim(); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());
                JSONobj = response.toString().replaceAll("[\\[\\]]", "");

                try {
                    Log.i(TAG, "Get List for Veh Comm");

                    for (int i = 0; i < response.length(); ++i) {
                        count++;
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());

                        GetIDform = json.getString("Id");
                        GetDriver = json.getString("Driver");
                        GetVehicleNumber = json.getString("Vehicle");
                        GetRiskLevel = json.getString("OverallRiskLevel");
                        GetCompletion = json.getInt("Completion");

                        Log.v(TAG, getFullName);
                        Log.v(TAG, GetDriver);
                        Log.v(TAG, GetVehicleNumber);
                        Log.v(TAG, GetRiskLevel);
                        Log.v(TAG, "" +GetCompletion);

                        //Passing values to CustomListViewMTRAC
                        ArrayListMTRAMforms.add(new CustomListViewMTRAC(GetIDform, GetDriver, GetVehicleNumber, GetRiskLevel, count, 1, GetCompletion));
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }

                if (ArrayListMTRAMforms.isEmpty()){ //Sets TextView to Visible if there are no MT-RAC Forms
                    NoMTRAC.setVisibility(View.VISIBLE);
                }

                ListForms.setAdapter(MTRAMforms); //Sets ListView to display Custom Adapter
                progressanim.dismiss(); //Removes animation by dismissing ProgressDialog

                ListAdapter listadp = ListForms.getAdapter();   //Gets ListView's Adapter to programmatically adjust ListView Properties
                if (listadp != null) {  //Sets ListView Height Programmatically to include all items in List
                    int totalHeight = 0;
                    for (int i = 0; i < listadp.getCount(); i++) {  //Counts number of items in ListView
                        View listItem = listadp.getView(i, null, ListForms);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = ListForms.getLayoutParams();
                    params.height = totalHeight + (ListForms.getDividerHeight() * (listadp.getCount() - 1));
                    ListForms.setLayoutParams(params);
                    ListForms.requestLayout();
                }

                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
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

    public void NoConnectionalertDialog(String StringTitle, String StringMessage, String StringButton) {
        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog1, null);

        TextView Title = (TextView) dialogView.findViewById(R.id.Title);
        TextView Message = (TextView) dialogView.findViewById(R.id.Message);
        Button Button = (Button) dialogView.findViewById(R.id.Button);

        Title.setText(StringTitle);
        Message.setText(StringMessage);
        Button.setText(StringButton);

        alert.setCancelable(false);
        alert.setView(dialogView);
        final AlertDialog show = alert.show();

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Back = new Intent(getBaseContext(), com.example.portal.mtram.class);
                startActivity(Back);

                finish();
            }
        });
    }

    //Sets up the "up" affordance.
    @Override
    public boolean onSupportNavigateUp(){
        Intent Back = new Intent(this, com.example.portal.mtram.class);
        startActivity(Back);

        finish();
        return true;
    }

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.

        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
