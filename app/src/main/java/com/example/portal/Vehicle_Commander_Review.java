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

public class Vehicle_Commander_Review extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button Submit, Reject;
    TextView CountersigningOfficerName, DateVehComm, TimeVehComm, DriverName, DateDriver, TimeDriver, JourneyFrom, JourneyTo, VehicleNumber;
    TextView ExperienceText, Experience, TrainedandFamiliarised, VehicleTypeText, VehicleType, LengthofRest, MyHealth, Weather, RouteFamiliarity, PurposeofMission, VehicleCommanderPresent, VehicleMotorcycleServiceability, OverallRiskLevel;
    TextView DriverTaskCompleted, DriverTaskIncomplete, DriverComplete1, DriverComplete2, DriverComplete3, DriverComplete4, DriverComplete5, DriverComplete6, DriverComplete7;

    View DriverTaskCompletedLine, DriverTaskIncompleteLine;

    CheckBox DriverIncomplete1, DriverIncomplete2, DriverIncomplete3, DriverIncomplete4, DriverIncomplete5, DriverIncomplete6, DriverIncomplete7;
    CheckBox VehComm1, VehComm2, VehComm3, VehComm4, VehComm5, VehComm6, VehComm7, VehComm8, VehComm9, VehComm10, VehComm11, VehComm12, ConfirmationofChecks;

    public String test;

    String StartJourneyDateTimeAns, JourneyCompletedDateTimeAns;
    int StartJourneyAns, JourneyCompletedAns, OverallRiskLevelAns, Driver1, Driver2, Driver3, Driver4, Driver5, Driver6, Driver7;
    int VehComm1Ans, VehComm2Ans, VehComm3Ans, VehComm4Ans, VehComm5Ans, VehComm6Ans, VehComm7Ans, VehComm8Ans, VehComm9Ans, VehComm10Ans, VehComm11Ans, VehComm12Ans;

    String getFullName;
    String CountersigningOfficerNameAns, DateCountersigningOfficerAns, TimeCountersigningOfficerAns, DriverNameAns, DateDriverAns, TimeDriverAns, JourneyFromAns, JourneyToAns, VehicleNumberAns;
    int DrivingRiding, ExperienceRisk, TrainedandFamiliarisedRisk, SameAsLastDetail, VehicleTypeRisk, LengthofRestRisk, MyHealthRisk, WeatherRisk, RouteFamiliarityRisk, PurposeofMissionRisk, VehicleCommanderPresentRisk, VehicleMotorServiceabilityRisk;

    String DateFormat, TimeFormat;

    ArrayList<String> getPushNotificationTokenDriver = new ArrayList<>();

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle__commander__review);

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

        //Assigns buttons to widgets
        Submit = (Button) findViewById(R.id.Submit);
        Reject = (Button) findViewById(R.id.Reject);

        //Assigns TextView to widgets
        CountersigningOfficerName = (TextView) findViewById(R.id.VehCommName);
        DateVehComm = (TextView) findViewById(R.id.DateVehComm);
        TimeVehComm = (TextView) findViewById(R.id.TimeVehComm);

        DriverName = (TextView) findViewById(R.id.SubmitMTRAC);
        DateDriver = (TextView) findViewById(R.id.DateDriver);
        TimeDriver = (TextView) findViewById(R.id.TimeDriver);

        JourneyFrom = (TextView) findViewById(R.id.JourneyFrom);
        JourneyTo = (TextView) findViewById(R.id.JourneyTo);
        VehicleNumber = (TextView) findViewById(R.id.VehicleNumber);

        ExperienceText = (TextView) findViewById(R.id.ExperienceText);
        Experience = (TextView) findViewById(R.id.Experience);
        TrainedandFamiliarised = (TextView) findViewById(R.id.TrainedandFamiliarised);
        VehicleTypeText = (TextView) findViewById(R.id.VehicleTypeText);
        VehicleType = (TextView) findViewById(R.id.VehicleType);
        LengthofRest = (TextView) findViewById(R.id.LengthofRest);
        MyHealth = (TextView) findViewById(R.id.MyHealth);
        Weather = (TextView) findViewById(R.id.Weather);
        RouteFamiliarity = (TextView) findViewById(R.id.RouteFamiliarity);
        PurposeofMission = (TextView) findViewById(R.id.PurposeofMission);
        VehicleCommanderPresent = (TextView) findViewById(R.id.VehicleCommanderPresent);
        VehicleMotorcycleServiceability = (TextView) findViewById(R.id.VehicleMotorcycleServiceability);
        OverallRiskLevel = (TextView) findViewById(R.id.OverallRiskLevel);

        DriverTaskCompleted = (TextView) findViewById(R.id.DriverTaskCompleted);

        DriverComplete1 = (TextView) findViewById(R.id.DriverComplete1);
        DriverComplete2 = (TextView) findViewById(R.id.DriverComplete2);
        DriverComplete3 = (TextView) findViewById(R.id.DriverComplete3);
        DriverComplete4 = (TextView) findViewById(R.id.DriverComplete4);
        DriverComplete5 = (TextView) findViewById(R.id.DriverComplete5);
        DriverComplete6 = (TextView) findViewById(R.id.DriverComplete6);
        DriverComplete7 = (TextView) findViewById(R.id.DriverComplete7);

        DriverTaskIncomplete = (TextView) findViewById(R.id.DriverTaskIncomplete);

        //Assigns Checkbox to widgets
        DriverIncomplete1 = (CheckBox) findViewById(R.id.DriverIncomplete1);
        DriverIncomplete2 = (CheckBox) findViewById(R.id.DriverIncomplete2);
        DriverIncomplete3 = (CheckBox) findViewById(R.id.DriverIncomplete3);
        DriverIncomplete4 = (CheckBox) findViewById(R.id.DriverIncomplete4);
        DriverIncomplete5 = (CheckBox) findViewById(R.id.DriverIncomplete5);
        DriverIncomplete6 = (CheckBox) findViewById(R.id.DriverIncomplete6);
        DriverIncomplete7 = (CheckBox) findViewById(R.id.DriverIncomplete7);

        VehComm1 = (CheckBox) findViewById(R.id.VehComm1);
        VehComm2 = (CheckBox) findViewById(R.id.VehComm2);
        VehComm3 = (CheckBox) findViewById(R.id.VehComm3);
        VehComm4 = (CheckBox) findViewById(R.id.VehComm4);
        VehComm5 = (CheckBox) findViewById(R.id.VehComm5);
        VehComm6 = (CheckBox) findViewById(R.id.VehComm6);
        VehComm7 = (CheckBox) findViewById(R.id.VehComm7);
        VehComm8 = (CheckBox) findViewById(R.id.VehComm8);
        VehComm9 = (CheckBox) findViewById(R.id.VehComm9);
        VehComm10 = (CheckBox) findViewById(R.id.VehComm10);
        VehComm11 = (CheckBox) findViewById(R.id.VehComm11);
        VehComm12 = (CheckBox) findViewById(R.id.VehComm12);
        ConfirmationofChecks = (CheckBox) findViewById(R.id.ConfirmationofChecks);

        //Assigns View to widgets
        DriverTaskCompletedLine = (View) findViewById(R.id.DriverTaskCompletedLine);
        DriverTaskIncompleteLine = (View) findViewById(R.id.DriverTaskIncompleteLine);


        //Listens for button clicks in Driver
        ConfirmationofChecks.setOnClickListener(this);
        Submit.setOnClickListener(this);
        Reject.setOnClickListener(this);

        getFullName();  //Gets Full Name from Outsystems
        GetDataOutsystems();    //Gets data from Outsystems and inserts into TextView Widgets

        new thread().execute();
    }

    //Code to run seperate thread
    public class thread extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() { Log.i(TAG, "onPreExecute"); }
        @Override
        protected Void doInBackground(Void... params) { //Executes background task
            Log.i(TAG, "doInBackground");
            getFullName();  //Gets Full Name from Outsystems
            GetDataOutsystems();    //Gets data from Outsystems and inserts into TextView Widgets
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == ConfirmationofChecks){
            VehComm1.setChecked(Boolean.TRUE);
            VehComm2.setChecked(Boolean.TRUE);
            VehComm3.setChecked(Boolean.TRUE);
            VehComm4.setChecked(Boolean.TRUE);
            VehComm5.setChecked(Boolean.TRUE);
            VehComm6.setChecked(Boolean.TRUE);
            VehComm7.setChecked(Boolean.TRUE);
            VehComm8.setChecked(Boolean.TRUE);
            VehComm9.setChecked(Boolean.TRUE);
            VehComm10.setChecked(Boolean.TRUE);
            VehComm11.setChecked(Boolean.TRUE);
            VehComm12.setChecked(Boolean.TRUE);
        }

        if (view == Submit){
            //Checks CheckBox
            CheckBoxVehCommChecked();


            if (VehComm1Ans==1 && VehComm2Ans==1 && VehComm3Ans==1 && VehComm4Ans==1 && VehComm5Ans==1 && VehComm6Ans==1 && VehComm7Ans==1 && VehComm8Ans==1 && VehComm9Ans==1 && VehComm10Ans==1 && VehComm11Ans==1 && VehComm12Ans==1 && ConfirmationofChecks.isChecked()){   //If all boxes are checked, do this.
                //Submits data to Firebase database
                PutsDataOutsystems();

                for (String object: getPushNotificationTokenDriver){
                    SendPushNotificationInformDriverMTRACComplete(object);
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "Please ensure all tasks are done.", Toast.LENGTH_LONG).show();
            }
        }

        if (view == Reject){
            String Title = "Reject";
            String Message = "Are you sure you want to reject the MT-RAC?";
            RejecttDialog(Title, Message);
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

    public void getTokenDriver(){
        Log.i(TAG, "Checking Token");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByFullName?FullName=" + DriverNameAns.replaceAll(" ", "%20"); //Points to target which is obtained from IPV4 Address from IP Config

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
                        if (!getPushNotificationTokenDriver.contains(json.getString("PushNotificationToken"))){ //If Arraylist does not contain token, add it in
                            getPushNotificationTokenDriver.add(json.getString("PushNotificationToken"));
                            Log.v(TAG, "Push Token " +getPushNotificationTokenDriver.get(i));
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

    public void GetDataOutsystems() {
        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/FindByIDform?IDform="  + getIntent().getStringExtra("IDform"); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());

                try {
                    Log.i(TAG, "Get form data");

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());

                        StartJourneyAns = Integer.parseInt(json.getString("StartJourney"));
                        JourneyCompletedAns = Integer.parseInt(json.getString("JourneyCompleted"));

                        if (StartJourneyAns==1){
                            StartJourneyDateTimeAns = json.getString("StartJourneyDateTime").toString();
                        }
                        if (JourneyCompletedAns==1){
                            JourneyCompletedDateTimeAns = json.getString("JourneyCompletedDateTime").toString();
                        }

                        CountersigningOfficerNameAns = json.getString("CountersigningOfficer");
                        DateCountersigningOfficerAns = json.getString("CountersigningOfficerDate").toString();
                        TimeCountersigningOfficerAns = json.getString("CountersigningOfficerTime").toString();

                        DriverNameAns = json.getString("Driver");
                        DateDriverAns = json.getString("DriverDate").toString();
                        TimeDriverAns = json.getString("DriverTime").toString();

                        JourneyFromAns = json.getString("JourneyFrom").toString();
                        JourneyToAns = json.getString("JourneyTo").toString();

                        VehicleNumberAns = json.getString("Vehicle");
                        DrivingRiding = Integer.parseInt(json.getString("DrivingRiding"));
                        ExperienceRisk = Integer.parseInt(json.getString("Experience"));
                        TrainedandFamiliarisedRisk = Integer.parseInt(json.getString("TrainedandFamiliarised"));
                        SameAsLastDetail = Integer.parseInt(json.getString("SameAsLastDetail"));
                        VehicleTypeRisk = Integer.parseInt(json.getString("VehicleType"));
                        LengthofRestRisk = Integer.parseInt(json.getString("LengthofRest"));
                        MyHealthRisk = Integer.parseInt(json.getString("MyHealth"));
                        WeatherRisk = Integer.parseInt(json.getString("Weather"));
                        RouteFamiliarityRisk = Integer.parseInt(json.getString("RouteFamiliarity"));
                        PurposeofMissionRisk = Integer.parseInt(json.getString("PurposeofMission"));
                        VehicleCommanderPresentRisk = Integer.parseInt(json.getString("VehicleCommanderPresent"));
                        VehicleMotorServiceabilityRisk = Integer.parseInt(json.getString("VehicleMotorServiceability"));
                        OverallRiskLevelAns = Integer.parseInt(json.getString("OverallRiskLevel"));

                        Driver1 = Integer.parseInt(json.getString("Driver1"));
                        Driver2 = Integer.parseInt(json.getString("Driver2"));
                        Driver3 = Integer.parseInt(json.getString("Driver3"));
                        Driver4 = Integer.parseInt(json.getString("Driver4"));
                        Driver5 = Integer.parseInt(json.getString("Driver5"));
                        Driver6 = Integer.parseInt(json.getString("Driver6"));
                        Driver7 = Integer.parseInt(json.getString("Driver7"));

                        Log.v(TAG, "DriverTime " +TimeDriverAns);
                        Log.v(TAG, "DrivingRiding " +DrivingRiding);
                        Log.v(TAG, "" +ExperienceRisk);
                        Log.v(TAG, "" +TrainedandFamiliarisedRisk);
                        Log.v(TAG, "" +SameAsLastDetail);
                        Log.v(TAG, "" +VehicleTypeRisk);
                        Log.v(TAG, "" +LengthofRestRisk);
                        Log.v(TAG, "" +MyHealthRisk);
                        Log.v(TAG, "" +WeatherRisk);
                        Log.v(TAG, "" +RouteFamiliarityRisk);
                        Log.v(TAG, "" +PurposeofMissionRisk);
                        Log.v(TAG, "" +VehicleCommanderPresentRisk);
                        Log.v(TAG, "" +VehicleMotorServiceabilityRisk);

                        setTextView();  //Inserts into TextView Widgets
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

    public String setDate(String Date) {
        String[] Months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String setDate, Year, Day;
        int Month;

        Year = Date.substring(0, 4);
        Month = Integer.parseInt(Date.substring(5, 7));
        Day = Date.substring(8, 10);

        setDate = Day+" "+Months[Month-1]+" "+Year;

        return setDate;
    }

    public String setTime(String Time) {
        int Hour, Minute;
        String setTime, Hour2, Minute2;

        Hour = Integer.parseInt(Time.substring(0,2));
        Minute = Integer.parseInt(Time.substring(3,5));

        //If hour < 10, add a 0 in front
        if(Hour<10) {
            Hour2="0"+Hour;
        }
        else{
            Hour2=""+Hour;
        }

        //If minute < 10, add a 0 in front
        if(Minute<10) {
            Minute2="0"+Minute;
        }
        else{
            Minute2=""+Minute;
        }

        setTime = Hour2+":"+Minute2;

        return setTime;
    }

    public SpannableStringBuilder StringBuilder(String Title, String Value){
        SpannableStringBuilder StringBuilder = new SpannableStringBuilder();
        StringBuilder.append(Title);
        StringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, StringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringBuilder.append(Value);

        return StringBuilder;
    }

    public void setTextView() {
        CountersigningOfficerName.setText(StringBuilder("Countersigning Officer: ", CountersigningOfficerNameAns));

        DateVehComm.setText(StringBuilder("Date of Review: ", getIntent().getStringExtra("Date")));
        TimeVehComm.setText(StringBuilder("Time of Review: ", getIntent().getStringExtra("Time")));
        DateFormat = getIntent().getStringExtra("DateFormat");
        TimeFormat = getIntent().getStringExtra("TimeFormat");

        DriverName.setText(StringBuilder("Driver: ", DriverNameAns));
        DateDriver.setText(StringBuilder("Date of MT-RAC: ", setDate(DateDriverAns)));
        TimeDriver.setText(StringBuilder("Time of MT-RAC: ", TimeDriverAns.substring(0,5)));
        JourneyFrom.setText(StringBuilder("Disembarkation Point: ", JourneyToAns));
        JourneyTo.setText(StringBuilder("Destination: ", JourneyToAns));
        VehicleNumber.setText(StringBuilder("Vehicle Number: ", VehicleNumberAns));

        if (DrivingRiding==1){
            if (ExperienceRisk==1){
                Experience.setText(StringBuilder("Driving Experience: ", "CAT A, B"));
            }
            if (ExperienceRisk==2){
                Experience.setText(StringBuilder("Driving Experience: ", "CAT C"));
            }
            if (ExperienceRisk==3){
                Experience.setText(StringBuilder("Driving Experience: ", "CAT D"));
            }
        }
        if (DrivingRiding==2){
            if (ExperienceRisk==1){
                Experience.setText(StringBuilder("Riding Experience: ", "More than 6 mths"));
            }
            if (ExperienceRisk==2){
                Experience.setText(StringBuilder("Riding Experience: ", "More than 3 mths"));
            }
            if (ExperienceRisk==3){
                Experience.setText(StringBuilder("Riding Experience: ", "More than 1 mth"));
            }
        }

        if (TrainedandFamiliarisedRisk==1){
            TrainedandFamiliarised.setText("Yes, trained and familiarised");
        }
        else {
            TrainedandFamiliarised.setText("Not trained or familiarised");
        }

        if (SameAsLastDetail==1){
            VehicleType.setText(StringBuilder("Vehicle Type: ", "Same as last detail"));
        }
        else {
            if (DrivingRiding==1){
                if (VehicleTypeRisk == 1){
                    VehicleType.setText(StringBuilder("Vehicle Type: ", "GP Car/Pick Up"));
                }
                if (VehicleTypeRisk == 2){
                    VehicleType.setText(StringBuilder("Vehicle Type: ", "Mini-Bus/Jeep/LR/UM"));
                }
                if (VehicleTypeRisk == 3){
                    VehicleType.setText(StringBuilder("Vehicle Type: ", "Coach/3 Ton/5 Ton/7 Ton"));
                }
            }
            if (DrivingRiding==2){
                if (VehicleTypeRisk == 1){
                    VehicleType.setText(StringBuilder("Vehicle Type: ", "200cc"));
                }
                if (VehicleTypeRisk == 2){
                    VehicleType.setText(StringBuilder("Vehicle Type: ", "750cc"));
                }
            }
        }

        if (LengthofRestRisk==1){
            LengthofRest.setText(StringBuilder("Fatigue/Length of Rest: ", "More than 6 hours"));
        }
        if (LengthofRestRisk==2){
            LengthofRest.setText(StringBuilder("Fatigue/Length of Rest: ", "Less than 6 hours: At Home/Camp"));
        }
        if (LengthofRestRisk==3){
            LengthofRest.setText(StringBuilder("Fatigue/Length of Rest: ", "More than 6 hours: In the Field"));
        }

        if (MyHealthRisk==1){
            MyHealth.setText(StringBuilder("My Health: ", "Good"));
        }
        if (MyHealthRisk==2){
            MyHealth.setText(StringBuilder("My Health: ", "Fair (Just recovered from illness < 1 day"));
        }
        if (MyHealthRisk==4){
            MyHealth.setText(StringBuilder("My Health: ", "Poor or still under medication that causes drowsiness (Still ill or on Attend C)"));
        }

        if (WeatherRisk==1){
            Weather.setText(StringBuilder("Weather: ", "Dry"));
        }
        if (WeatherRisk==2){
            Weather.setText(StringBuilder("Weather: ", "Wet"));
        }
        if (WeatherRisk==3){
            Weather.setText(StringBuilder("Weather: ", "Heavy Showers"));
        }

        if (RouteFamiliarityRisk==1){
            RouteFamiliarity.setText(StringBuilder("Route Familiarity: ", "Familiar"));
        }
        if (RouteFamiliarityRisk==3){
            RouteFamiliarity.setText(StringBuilder("Route Familiarity: ", "Not Familiar"));
        }

        if (PurposeofMissionRisk==1){
            PurposeofMission.setText(StringBuilder("Purpose of Mission: ", "Admin"));
        }
        if (PurposeofMissionRisk==2){
            PurposeofMission.setText(StringBuilder("Purpose of Mission: ", "Training/Special Mission Orientated Towing Vehicle/Gun"));
        }
        if (PurposeofMissionRisk==3){
            PurposeofMission.setText(StringBuilder("Purpose of Mission: ", "Occasional Towing Trailer/Vehicle/Gun"));
        }

        if (VehicleCommanderPresentRisk==0){
            VehicleCommanderPresent.setText("Vehicle Commander Present");
        }
        if (DrivingRiding == 1){
            if (VehicleCommanderPresentRisk==1){
                VehicleCommanderPresent.setText("Not Present, Driver is CAT A, B");
            }
            if (VehicleCommanderPresentRisk==2){
                VehicleCommanderPresent.setText("Not Present, Driver is CAT C");
            }
            if (VehicleCommanderPresentRisk==4){
                VehicleCommanderPresent.setText("Not Present, Driver is CAT D");
            }
        }
        if (DrivingRiding == 2){
            if (VehicleCommanderPresentRisk==1){
                VehicleCommanderPresent.setText("Not Present, More than 6 mths");
            }
            if (VehicleCommanderPresentRisk==2){
                VehicleCommanderPresent.setText("Not Present, More than 3 mths");
            }
            if (VehicleCommanderPresentRisk==4){
                VehicleCommanderPresent.setText("Not Present, Less than 1 mth");
            }
        }

        if (VehicleMotorServiceabilityRisk==1){
            VehicleMotorcycleServiceability.setText(StringBuilder("Vehicle/Motorcycle Serviceability: ", "No Fault"));
        }
        if (VehicleMotorServiceabilityRisk==3){
            VehicleMotorcycleServiceability.setText(StringBuilder("Vehicle/Motorcycle Serviceability: ", "Minor Fault"));
        }
        if (VehicleMotorServiceabilityRisk==4){
            VehicleMotorcycleServiceability.setText(StringBuilder("Vehicle/Motorcycle Serviceability: ", "Major Fault"));
        }

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

        //Setting Driver Complete Visibility
        if (Driver1==1){
            DriverComplete1.setVisibility(View.VISIBLE);
            DriverIncomplete1.setVisibility(View.GONE);

        }
        if (Driver1==0){
            DriverComplete1.setVisibility(View.GONE);
            DriverIncomplete1.setVisibility(View.VISIBLE);
        }
        if (Driver2==1){
            DriverComplete2.setVisibility(View.VISIBLE);
            DriverIncomplete2.setVisibility(View.GONE);
        }
        if (Driver2==0){
            DriverComplete2.setVisibility(View.GONE);
            DriverIncomplete2.setVisibility(View.VISIBLE);
        }
        if (Driver3==1){
            DriverComplete3.setVisibility(View.VISIBLE);
            DriverIncomplete3.setVisibility(View.GONE);
        }
        if (Driver3==0){
            DriverComplete3.setVisibility(View.GONE);
            DriverIncomplete3.setVisibility(View.VISIBLE);
        }
        if (Driver4==1){
            DriverComplete4.setVisibility(View.VISIBLE);
            DriverIncomplete4.setVisibility(View.GONE);
        }
        if (Driver4==0){
            DriverComplete4.setVisibility(View.GONE);
            DriverIncomplete4.setVisibility(View.VISIBLE);
        }
        if (Driver5==1){
            DriverComplete5.setVisibility(View.VISIBLE);
            DriverIncomplete5.setVisibility(View.GONE);
        }
        if (Driver5==0){
            DriverComplete5.setVisibility(View.GONE);
            DriverIncomplete5.setVisibility(View.VISIBLE);
        }
        if (Driver6==1){
            DriverComplete6.setVisibility(View.VISIBLE);
            DriverIncomplete6.setVisibility(View.GONE);
        }
        if (Driver6==0){
            DriverComplete6.setVisibility(View.GONE);
            DriverIncomplete6.setVisibility(View.VISIBLE);
        }
        if (Driver7==1){
            DriverComplete7.setVisibility(View.VISIBLE);
            DriverIncomplete7.setVisibility(View.GONE);
        }
        if (Driver7==0){
            DriverComplete7.setVisibility(View.GONE);
            DriverIncomplete7.setVisibility(View.VISIBLE);
        }

        DriverTaskCompleted.setVisibility(View.VISIBLE);
        DriverTaskCompletedLine.setVisibility(View.VISIBLE);
        DriverTaskIncomplete.setVisibility(View.VISIBLE);
        DriverTaskIncompleteLine.setVisibility(View.VISIBLE);

        if (Driver1==1&&Driver2==1&&Driver3==1&&Driver4==1&&Driver5==1&&Driver6==1&&Driver7==1) {
            DriverTaskCompleted.setVisibility(View.VISIBLE);
            DriverTaskCompletedLine.setVisibility(View.VISIBLE);
            DriverTaskIncomplete.setVisibility(View.GONE);
            DriverTaskIncompleteLine.setVisibility(View.GONE);
        }

        if (Driver1==0&&Driver2==0&&Driver3==0&&Driver4==0&&Driver5==0&&Driver6==0&&Driver7==0) {
            DriverTaskCompleted.setVisibility(View.GONE);
            DriverTaskCompletedLine.setVisibility(View.GONE);
            DriverTaskIncomplete.setVisibility(View.VISIBLE);
            DriverTaskIncompleteLine.setVisibility(View.VISIBLE);
        }

        //Gets Driver Tokens hers as it needs the Driver's Name to load first
        getTokenDriver();

        progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
    }

    public void CheckBoxVehCommChecked() {
        //Checks DriverIncomplete CheckBox
        if(DriverIncomplete1.isChecked()){
            Driver1 = 1;
        }
        if(DriverIncomplete2.isChecked()){
            Driver2 = 1;
        }
        if(DriverIncomplete3.isChecked()){
            Driver3 = 1;
        }
        if(DriverIncomplete4.isChecked()){
            Driver4 = 1;
        }
        if(DriverIncomplete5.isChecked()){
            Driver5 = 1;
        }
        if(DriverIncomplete6.isChecked()){
            Driver6 = 1;
        }
        if(DriverIncomplete7.isChecked()){
            Driver7 = 1;
        }

        //Checks VehComm CheckBox
        if(VehComm1.isChecked()){
            VehComm1Ans = 1;
        }
        else{
            VehComm1Ans = 0;
        }
        if(VehComm2.isChecked()){
            VehComm2Ans = 1;
        }
        else{
            VehComm2Ans = 0;
        }
        if(VehComm3.isChecked()){
            VehComm3Ans = 1;
        }
        else{
            VehComm3Ans = 0;
        }
        if(VehComm4.isChecked()){
            VehComm4Ans = 1;
        }
        else{
            VehComm4Ans = 0;
        }
        if(VehComm5.isChecked()){
            VehComm5Ans = 1;
        }
        else{
            VehComm5Ans = 0;
        }
        if(VehComm6.isChecked()){
            VehComm6Ans = 1;
        }
        else{
            VehComm6Ans = 0;
        }
        if(VehComm7.isChecked()){
            VehComm7Ans = 1;
        }
        else{
            VehComm7Ans = 0;
        }
        if(VehComm8.isChecked()){
            VehComm8Ans = 1;
        }
        else{
            VehComm8Ans = 0;
        }
        if(VehComm9.isChecked()){
            VehComm9Ans = 1;
        }
        else{
            VehComm9Ans = 0;
        }
        if(VehComm10.isChecked()){
            VehComm10Ans = 1;
        }
        else{
            VehComm10Ans = 0;
        }
        if(VehComm11.isChecked()){
            VehComm11Ans = 1;
        }
        else{
            VehComm11Ans = 0;
        }
        if(VehComm12.isChecked()){
            VehComm12Ans = 1;
        }
        else{
            VehComm12Ans = 0;
        }
    }

    public void PutsDataOutsystems() {
        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlput = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/Update"; //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a PUT request to the provided URL
        //StringRequest specifies the request type, target URL and how to handle target responses and errors
        StringRequest stringRequestpost = new StringRequest(Request.Method.PUT, server_urlput,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        String Title = "Success";
                        String Message = "MT-RAC is approved.";
                        String ButtonText = "OK";
                        alertDialog(Title, Message, ButtonText);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);

                String Title = "Error";
                String Message = "MT-RAC is not approved.";
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

                    parent.put("Id", getIntent().getStringExtra("IDform"));

                    parent.put("StartJourney", StartJourneyAns);
                    parent.put("JourneyCompleted", JourneyCompletedAns);

                    if (StartJourneyAns==1){
                        parent.put("StartJourneyDateTime", StartJourneyDateTimeAns);
                    }
                    if (JourneyCompletedAns==1){
                        parent.put("JourneyCompletedDateTime", JourneyCompletedDateTimeAns);
                    }

                    parent.put("VehicleCommander", getFullName);
                    parent.put("VehicleCommanderDate", DateFormat);
                    parent.put("VehicleCommanderTime", TimeFormat);

                    parent.put("CountersigningOfficer", getFullName);
                    parent.put("CountersigningOfficerDate", DateCountersigningOfficerAns);
                    parent.put("CountersigningOfficerTime", TimeCountersigningOfficerAns);

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);

                    parent.put("Driver", DriverNameAns);
                    parent.put("DriverDate", DateDriverAns);
                    parent.put("DriverTime", TimeDriverAns);

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

                    parent.put("Driver1", Driver1);
                    parent.put("Driver2", Driver2);
                    parent.put("Driver3", Driver3);
                    parent.put("Driver4", Driver4);
                    parent.put("Driver5", Driver5);
                    parent.put("Driver6", Driver6);
                    parent.put("Driver7", Driver7);

                    parent.put("VehComm1", VehComm1Ans);
                    parent.put("VehComm2", VehComm2Ans);
                    parent.put("VehComm3", VehComm3Ans);
                    parent.put("VehComm4", VehComm4Ans);
                    parent.put("VehComm5", VehComm5Ans);
                    parent.put("VehComm6", VehComm6Ans);
                    parent.put("VehComm7", VehComm7Ans);
                    parent.put("VehComm8", VehComm8Ans);
                    parent.put("VehComm9", VehComm9Ans);
                    parent.put("VehComm10", VehComm10Ans);
                    parent.put("VehComm11", VehComm11Ans);
                    parent.put("VehComm12", VehComm12Ans);

                    parent.put("Completion", 3);

                    parent.put("IsProtected", 0);

//                    Log.i(TAG, parent.toString(2));

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

    public void RejectDataOutsystems() {
        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlput = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/Update"; //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a PUT request to the provided URL
        //StringRequest specifies the request type, target URL and how to handle target responses and errors
        StringRequest stringRequestpost = new StringRequest(Request.Method.PUT, server_urlput,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        for (String object: getPushNotificationTokenDriver){
                            SendPushNotificationVCReject(object);
                        }

                        String Title = "Rejected";
                        String Message = "MT-RAC is rejected and not approved.";
                        String ButtonText = "OK";
                        alertDialog(Title, Message, ButtonText);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);

                String Title = "Something went wrong!";
                String Message = "No Internet Connection Detected.";
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

                    parent.put("Id", getIntent().getStringExtra("IDform"));

                    parent.put("StartJourney", StartJourneyAns);
                    parent.put("JourneyCompleted", JourneyCompletedAns);

                    if (StartJourneyAns==1){
                        parent.put("StartJourneyDateTime", StartJourneyDateTimeAns);
                    }
                    if (JourneyCompletedAns==1){
                        parent.put("JourneyCompletedDateTime", JourneyCompletedDateTimeAns);
                    }

                    parent.put("VehicleCommander", getFullName);
                    parent.put("VehicleCommanderDate", DateFormat);
                    parent.put("VehicleCommanderTime", TimeFormat);

                    parent.put("CountersigningOfficer", getFullName);
                    parent.put("CountersigningOfficerDate", DateCountersigningOfficerAns);
                    parent.put("CountersigningOfficerTime", TimeCountersigningOfficerAns);

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);

                    parent.put("Driver", DriverNameAns);
                    parent.put("DriverDate", DateDriverAns);
                    parent.put("DriverTime", TimeDriverAns);

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

                    parent.put("Driver1", Driver1);
                    parent.put("Driver2", Driver2);
                    parent.put("Driver3", Driver3);
                    parent.put("Driver4", Driver4);
                    parent.put("Driver5", Driver5);
                    parent.put("Driver6", Driver6);
                    parent.put("Driver7", Driver7);

                    parent.put("VehComm1", VehComm1Ans);
                    parent.put("VehComm2", VehComm2Ans);
                    parent.put("VehComm3", VehComm3Ans);
                    parent.put("VehComm4", VehComm4Ans);
                    parent.put("VehComm5", VehComm5Ans);
                    parent.put("VehComm6", VehComm6Ans);
                    parent.put("VehComm7", VehComm7Ans);
                    parent.put("VehComm8", VehComm8Ans);
                    parent.put("VehComm9", VehComm9Ans);
                    parent.put("VehComm10", VehComm10Ans);
                    parent.put("VehComm11", VehComm11Ans);
                    parent.put("VehComm12", VehComm12Ans);

                    parent.put("Completion", 0);
                    parent.put("Reject", 2);    //Reject 1 means Countersigning Officer rejected MT-RAC, Reject 2 means Vehicle Commander rejected MT-RAC

                    parent.put("IsProtected", 0);

//                    Log.i(TAG, parent.toString(2));

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

    public void SendPushNotificationInformDriverMTRACComplete(final String Token) {
        Log.i(TAG, "Sending POST request to Firebase");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/FirebaseCloudMessaging/InformDriverMTRACComplete"; //Points to target which is obtained from IPV4 Address from IP Config

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

    public void SendPushNotificationVCReject(final String Token) {
        Log.i(TAG, "Sending POST request to Firebase");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/FirebaseCloudMessaging/VCReject"; //Points to target which is obtained from IPV4 Address from IP Config

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
                Intent Back = new Intent(getBaseContext(), com.example.portal.Vehicle_Commander.class);
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

    public void RejecttDialog(String StringTitle, String StringMessage) {
        final AlertDialog.Builder alert;
        alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alertdialog2, null);

        TextView Title = (TextView) dialogView.findViewById(R.id.Title);
        TextView Message = (TextView) dialogView.findViewById(R.id.Message);
        Button Button1 = (Button) dialogView.findViewById(R.id.Button1);
        Button Button2 = (Button) dialogView.findViewById(R.id.Button2);

        Title.setText(StringTitle);
        Message.setText(StringMessage);

        alert.setCancelable(false);
        alert.setView(dialogView);
        final AlertDialog show = alert.show();

        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RejectDataOutsystems();
                show.dismiss();
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.dismiss();
            }
        });
    }

    //Sets up the "up" affordance.
    @Override
    public boolean onSupportNavigateUp(){
        Intent Back = new Intent(this, com.example.portal.Vehicle_Commander.class);
        startActivity(Back);

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent Back = new Intent(this, com.example.portal.Vehicle_Commander.class);
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
