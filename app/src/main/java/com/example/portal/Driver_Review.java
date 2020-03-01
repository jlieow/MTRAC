package com.example.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver_Review extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button Submit;
    TextView CountersigningOfficerName, VehicleCommanderName, JourneyFrom, JourneyTo, DateDriver, TimeDriver, VehicleNumber;
    TextView Experience, TrainedandFamiliarised, VehicleType, LengthofRest, MyHealth, Weather, RouteFamiliarity, PurposeofMission, VehicleCommanderPresent, VehicleMotorcycleServiceability, OverallRiskLevel;
    CheckBox Driver1, Driver2, Driver3, Driver4, Driver5, Driver6, Driver7;

    String CountersigningOfficerNameAns, VehCommNameAns, JourneyFromAns, JourneyToAns, DateDriverAns, TimeDriverAns, DateFormat, TimeFormat, VehicleNumberAns;
    String ExperienceAns, TrainedandFamiliarisedAns, VehicleTypeAns, LengthofRestAns, MyHealthAns, WeatherAns, RouteFamiliarityAns, PurposeofMissionAns, VehicleCommanderPresentAns, VehicleMotorcycleServiceabilityAns;

    int Driver1Ans, Driver2Ans, Driver3Ans, Driver4Ans, Driver5Ans, Driver6Ans, Driver7Ans, DrivingRiding, OverallRiskLevelAns;

    int ExperienceRisk, TrainedandFamiliarisedRisk, SameAsLastDetail, VehicleTypeRisk, LengthofRestRisk, MyHealthRisk, WeatherRisk, RouteFamiliarityRisk, PurposeofMissionRisk, VehicleCommanderPresentRisk, VehicleMotorServiceabilityRisk;
    String getFullName;

    ArrayList<String> getPushNotificationCountersigningOfficerToken = new ArrayList<>();

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver__review);

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

        //Assigns Button to widgets
        Submit = (Button) findViewById(R.id.Submit);

        //Assigns TextView to widgets
        CountersigningOfficerName = (TextView) findViewById(R.id.CountersigningOfficerName);
        VehicleCommanderName = (TextView) findViewById(R.id.VehCommName);
        JourneyFrom = (TextView) findViewById(R.id.JourneyFrom);
        JourneyTo = (TextView) findViewById(R.id.JourneyTo);
        DateDriver = (TextView) findViewById(R.id.DateDriver);
        TimeDriver = (TextView) findViewById(R.id.TimeDriver);
        VehicleNumber = (TextView) findViewById(R.id.VehicleNumber);

        Experience = (TextView) findViewById(R.id.Experience);
        TrainedandFamiliarised = (TextView) findViewById(R.id.TrainedandFamiliarised);
        VehicleType = (TextView) findViewById(R.id.VehicleType);
        LengthofRest = (TextView) findViewById(R.id.LengthofRest);
        MyHealth = (TextView) findViewById(R.id.MyHealth);
        Weather = (TextView) findViewById(R.id.Weather);
        RouteFamiliarity = (TextView) findViewById(R.id.RouteFamiliarity);
        PurposeofMission = (TextView) findViewById(R.id.PurposeofMission);
        VehicleCommanderPresent = (TextView) findViewById(R.id.VehicleCommanderPresent);
        VehicleMotorcycleServiceability = (TextView) findViewById(R.id.VehicleMotorcycleServiceability);
        OverallRiskLevel = (TextView) findViewById(R.id.OverallRiskLevel);

        //Assigns Checkbox to widgets
        Driver1 = (CheckBox) findViewById(R.id.Driver1);
        Driver2 = (CheckBox) findViewById(R.id.Driver2);
        Driver3 = (CheckBox) findViewById(R.id.Driver3);
        Driver4 = (CheckBox) findViewById(R.id.Driver4);
        Driver5 = (CheckBox) findViewById(R.id.Driver5);
        Driver6 = (CheckBox) findViewById(R.id.Driver6);
        Driver7 = (CheckBox) findViewById(R.id.Driver7);

        //Listens for button clicks in Driver
        Submit.setOnClickListener(this);

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
            getFullName(); //Get Driver's Full Name
            getDataIntent(); //Gets Data via intent
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == Submit) {
            if (OverallRiskLevelAns<3){ //If risk level is Medium or lower, do this.
                //Checks CheckBox
                CheckBoxDriverChecked();

                if (Driver1Ans==1 && Driver2Ans==1 && Driver3Ans==1 && Driver4Ans==1 && Driver5Ans==1 && Driver6Ans==1 && Driver7Ans==1){   //If all boxes are checked, do this.
                    PutsDataOutsystems();   //Submits data to Firebase database

                    for (String object: getPushNotificationCountersigningOfficerToken){
                        SendPushNotificationDriverToCO(object); //Sends Push Notification
                    }
                }

                else {
                    Toast.makeText(getApplicationContext(), "Please ensure all tasks are done.", Toast.LENGTH_LONG).show();
                }
            }
            else {  //If risk level is High and above, do this.
                Toast.makeText(getApplicationContext(), "Risk is too high, please mitigate.", Toast.LENGTH_LONG).show();
            }
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

                try {
                    Log.i(TAG, "Get Full Name");

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        getFullName = json.getString("FullName");
                        Log.v(TAG, getFullName);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }

                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String Title = "Something went wrong!";
                String Message = "No Internet Connection Detected.";
                String ButtonText = "OK";
                NoConnectionalertDialog(Title, Message, ButtonText);
                return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void getTokenCountersigningOfficer(){
        Log.i(TAG, "Checking Token");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByFullName?FullName=" + getIntent().getStringExtra("CountersigningOfficerName").replaceAll(" ", "%20"); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());

                try {
                    Log.i(TAG, "Get Push Notification Token");

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        if (!getPushNotificationCountersigningOfficerToken.contains(json.getString("PushNotificationToken"))){ //If Arraylist does not contain token, add it in
                            getPushNotificationCountersigningOfficerToken.add(json.getString("PushNotificationToken"));
                            Log.v(TAG, "Push Token " +getPushNotificationCountersigningOfficerToken.get(i));
                        }
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

    //Gets Data via intent
    public void getDataIntent() {
        CountersigningOfficerNameAns = getIntent().getStringExtra("CountersigningOfficerName");
        VehCommNameAns = getIntent().getStringExtra("VehCommName");
        JourneyFromAns = getIntent().getStringExtra("JourneyFrom");
        JourneyToAns = getIntent().getStringExtra("JourneyTo");
        DateDriverAns = getIntent().getStringExtra("Date");
        TimeDriverAns = getIntent().getStringExtra("Time");
        DateFormat = getIntent().getStringExtra("DateFormat");
        TimeFormat = getIntent().getStringExtra("TimeFormat");
        VehicleNumberAns = getIntent().getStringExtra("VehicleNumber");

        DrivingRiding = getIntent().getIntExtra("DrivingRiding", 0);
        ExperienceAns = getIntent().getStringExtra("Experience");
        TrainedandFamiliarisedAns = getIntent().getStringExtra("TrainedandFamiliarised");
        VehicleTypeAns = getIntent().getStringExtra("VehicleType");
        LengthofRestAns = getIntent().getStringExtra("LengthofRest");
        MyHealthAns = getIntent().getStringExtra("MyHealth");
        WeatherAns = getIntent().getStringExtra("Weather");
        RouteFamiliarityAns = getIntent().getStringExtra("RouteFamiliarity");
        PurposeofMissionAns = getIntent().getStringExtra("PurposeofMission");
        VehicleCommanderPresentAns = getIntent().getStringExtra("VehicleCommanderPresent");
        VehicleMotorcycleServiceabilityAns = getIntent().getStringExtra("VehicleMotorcycleServiceability");
        OverallRiskLevelAns = getIntent().getIntExtra("OverallRiskLevel", 0);

        //Risk Level of each MT Risk
        ExperienceRisk = getIntent().getIntExtra("ExperienceRisk", 0);
        TrainedandFamiliarisedRisk = getIntent().getIntExtra("TrainedandFamiliarisedRisk", 0);
        SameAsLastDetail = getIntent().getIntExtra("SameAsLastDetail", 0);
        VehicleTypeRisk = getIntent().getIntExtra("VehicleTypeRisk", 0);
        LengthofRestRisk = getIntent().getIntExtra("LengthofRestRisk", 0);
        MyHealthRisk = getIntent().getIntExtra("MyHealthRisk", 0);
        WeatherRisk = getIntent().getIntExtra("WeatherRisk", 0);
        RouteFamiliarityRisk = getIntent().getIntExtra("RouteFamiliarityRisk", 0);
        PurposeofMissionRisk = getIntent().getIntExtra("PurposeofMissionRisk", 0);
        VehicleCommanderPresentRisk = getIntent().getIntExtra("VehicleCommanderPresentRisk", 0);
        VehicleMotorServiceabilityRisk = getIntent().getIntExtra("VehicleMotorcycleServiceabilityRisk", 0);

        Log.i(TAG, "ExperienceRisk..." +ExperienceRisk);
        Log.i(TAG, "TrainedandFamiliarisedRisk..." +TrainedandFamiliarisedRisk);
        Log.i(TAG, "VehicleTypeRisk..." +VehicleTypeRisk);
        Log.i(TAG, "LengthofRestRisk..." +LengthofRestRisk);
        Log.i(TAG, "MyHealthRisk..." +MyHealthRisk);
        Log.i(TAG, "WeatherRisk..." +WeatherRisk);
        Log.i(TAG, "Veh Comm Present " +VehicleCommanderPresentRisk);

        setsTextView(); //Sets TextViews with data from Intent
    }

    public void CheckBoxDriverChecked() {
        if(Driver1.isChecked()){
            Driver1Ans = 1;
        }
        else{
            Driver1Ans = 0;
        }
        if(Driver2.isChecked()){
            Driver2Ans = 1;
        }
        else{
            Driver2Ans = 0;
        }
        if(Driver3.isChecked()){
            Driver3Ans = 1;
        }
        else{
            Driver3Ans = 0;
        }
        if(Driver4.isChecked()){
            Driver4Ans = 1;
        }
        else{
            Driver4Ans = 0;
        }
        if(Driver5.isChecked()){
            Driver5Ans = 1;
        }
        else{
            Driver5Ans = 0;
        }
        if(Driver6.isChecked()){
            Driver6Ans = 1;
        }
        else{
            Driver6Ans = 0;
        }
        if(Driver7.isChecked()){
            Driver7Ans = 1;
        }
        else{
            Driver7Ans = 0;
        }
    }

    public SpannableStringBuilder StringBuilder(String Title, String Value){
        SpannableStringBuilder StringBuilder = new SpannableStringBuilder();
        StringBuilder.append(Title);
        StringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, StringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringBuilder.append(Value);

        return StringBuilder;
    }

    public void setsTextView() {
        CountersigningOfficerName.setText(StringBuilder("Countersigning Officer: ", CountersigningOfficerNameAns));
        VehicleCommanderName.setText(StringBuilder("Vehicle Commander: ", VehCommNameAns));
        JourneyFrom.setText(StringBuilder("Disembarkation Point: ", JourneyFromAns));
        JourneyTo.setText(StringBuilder("Destination: ", JourneyToAns));
        DateDriver.setText(StringBuilder("Date of MT-RAC: ", DateDriverAns));
        TimeDriver.setText(StringBuilder("Time of MT-RAC: ", TimeDriverAns));
        VehicleNumber.setText(StringBuilder("Vehicle Number: ", VehicleNumberAns));

        if (DrivingRiding == 1) {
            Experience.setText(StringBuilder("Driving Experience: ", ExperienceAns));
            VehicleType.setText(StringBuilder("Vehicle Type: ", VehicleTypeAns));
        }
        if (DrivingRiding == 2) {
            Experience.setText(StringBuilder("Driving Experience: ", ExperienceAns));
            VehicleType.setText(StringBuilder("Motorcycle Type: ", VehicleTypeAns));
        }

        TrainedandFamiliarised.setText(TrainedandFamiliarisedAns);
        VehicleType.setText(StringBuilder("Vehicle Type: ", VehicleTypeAns));
        LengthofRest.setText(StringBuilder("Fatigue/Length of Rest: ", LengthofRestAns));
        MyHealth.setText(StringBuilder("My Health: ", MyHealthAns));
        Weather.setText(StringBuilder("Weather: ", WeatherAns));
        RouteFamiliarity.setText(StringBuilder("Route Familiarity: ", RouteFamiliarityAns));
        PurposeofMission.setText(StringBuilder("Purpose of Mission: ", PurposeofMissionAns));
        VehicleCommanderPresent.setText(VehicleCommanderPresentAns);
        VehicleMotorcycleServiceability.setText(StringBuilder("Vehicle/Motorcycle Serviceability: ", VehicleMotorcycleServiceabilityAns));

        if (OverallRiskLevelAns == 0 || OverallRiskLevelAns == 1) {
            OverallRiskLevel.setText("LOW");
            OverallRiskLevel.setBackgroundColor(getBaseContext().getResources().getColor(R.color.palegreen));
        }
        if (OverallRiskLevelAns == 2) {
            OverallRiskLevel.setText("MEDIUM");
            OverallRiskLevel.setBackgroundColor(getBaseContext().getResources().getColor(R.color.palegold));
        }
        if (OverallRiskLevelAns == 3) {
            OverallRiskLevel.setText("HIGH");
            OverallRiskLevel.setBackgroundColor(getBaseContext().getResources().getColor(R.color.red2));
        }
        if (OverallRiskLevelAns == 4) {
            OverallRiskLevel.setText("NO MOVE");
            OverallRiskLevel.setTextColor(Color.WHITE);
            OverallRiskLevel.setBackgroundColor(Color.BLACK);
        }

        //Gets VehicleCommander's Tokens here as it needs the Driver and Officer's Name to load first
        getTokenCountersigningOfficer();

        progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
    }

    public void PutsDataOutsystems(){
        Log.i(TAG, "Sending POST request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/Create"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        String Title = "Success";
                        String Message = "MT-RAC was sent successfully.";
                        String ButtonText = "OK";
                        alertDialog(Title, Message, ButtonText);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);

                String Title = "Error";
                String Message = "MT-RAC was not sent.";
                String ButtonText = "OK";
                alertDialog(Title, Message, ButtonText);
//                error.printStackTrace();
                Log.v(TAG, "Unexpected VolleyError");
                requestQueue.stop();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {   //Sets Headers
                Map<String,String> headers=new HashMap<String,String>();
                headers.put("Accept", "application/json");

                Log.i(TAG, headers.toString());

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    JSONObject parent = new JSONObject();

                    parent.put("StartJourney", 0);
                    parent.put("JourneyCompleted", 0);
                    parent.put("CountersigningOfficer", CountersigningOfficerNameAns);

                    if (VehicleCommanderPresentRisk == 0){
                        parent.put("VehicleCommander", VehCommNameAns);
                    }
                    else {
                        parent.put("VehicleCommander", "No Vehicle Commander Present");
                    }

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);
                    parent.put("Driver", getFullName);
                    parent.put("DriverDate", DateFormat);
                    parent.put("DriverTime", TimeFormat);
                    parent.put("Vehicle", VehicleNumberAns);
                    parent.put("DrivingRiding", DrivingRiding);
                    parent.put("Experience", ExperienceRisk);
                    parent.put("TrainedAndFamiliarised", TrainedandFamiliarisedRisk);
                    parent.put("SameAsLastDetail", SameAsLastDetail);
                    parent.put("VehicleType", VehicleTypeRisk);
                    parent.put("LengthofRest", LengthofRestRisk);
                    parent.put("MyHealth", MyHealthRisk);
                    parent.put("Weather", WeatherRisk);
                    parent.put("RouteFamiliarity", RouteFamiliarityRisk);
                    parent.put("PurposeofMission", PurposeofMissionRisk);
                    parent.put("VehicleCommanderPresent", VehicleCommanderPresentRisk);
                    parent.put("VehicleMotorServiceability", VehicleMotorServiceabilityRisk);
                    parent.put("OverallRiskLevel", OverallRiskLevelAns);
                    parent.put("Driver1", Driver1Ans);
                    parent.put("Driver2", Driver2Ans);
                    parent.put("Driver3", Driver3Ans);
                    parent.put("Driver4", Driver4Ans);
                    parent.put("Driver5", Driver5Ans);
                    parent.put("Driver6", Driver6Ans);
                    parent.put("Driver7", Driver7Ans);

                    //Sets VehComm to 0 as it will be overwritten later on if there is indeeed a Vehicle Commander
                    parent.put("VehComm1", 0);
                    parent.put("VehComm2", 0);
                    parent.put("VehComm3", 0);
                    parent.put("VehComm4", 0);
                    parent.put("VehComm5", 0);
                    parent.put("VehComm6", 0);
                    parent.put("VehComm7", 0);
                    parent.put("VehComm8", 0);
                    parent.put("VehComm9", 0);
                    parent.put("VehComm10", 0);
                    parent.put("VehComm11", 0);
                    parent.put("VehComm12", 0);

                    parent.put("Completion", 1);
                    parent.put("IsProtected", 0);

                    Log.i(TAG, parent.toString(2));

                    String your_string_json = parent.toString(); // put your json
                    return your_string_json.getBytes();
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }

                return null;
            }

            @Override
            public String getBodyContentType() {    //Sets type to json
                return "application/json";
            }
        };

        //Starts Request
        requestQueue.add(stringRequestpost);
    }

    public void SendPushNotificationDriverToCO(final String Token) {
        Log.i(TAG, "Sending POST request to Firebase");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/FirebaseCloudMessaging/DriverToCO"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);
//                error.printStackTrace();
                Log.v(TAG, "Unexpected VolleyError");
                requestQueue.stop();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {   //Sets Headers
                Map<String,String> headers=new HashMap<String,String>();
                headers.put("Token", Token);

                Log.i(TAG, headers.toString());

                return headers;
            }
        };

        //Starts Request
        requestQueue.add(stringRequestpost);
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
                Intent Back = new Intent(getBaseContext(), com.example.portal.Driver.class);
                startActivity(Back);

                finish();
            }
        });
    }

    public void alertDialog(String StringTitle, String StringMessage, String StringButton) {
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
        alert.show();

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mtram = new Intent(getBaseContext(), com.example.portal.mtram.class);
                startActivity(mtram);
                finish();
            }
        });
    }

    //Sets up the "up" affordance.
    @Override
    public boolean onSupportNavigateUp(){
        Intent Back = new Intent(this, com.example.portal.Driver.class);
        startActivity(Back);

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent Back = new Intent(this, com.example.portal.Driver.class);
        startActivity(Back);

        finish();
        return;
    }

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
