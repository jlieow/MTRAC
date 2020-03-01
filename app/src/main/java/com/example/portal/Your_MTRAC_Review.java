package com.example.portal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Your_MTRAC_Review extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button StartJourney, JourneyCompleted;

    TextView FormStatus, CountersigningOfficerName, VehicleCommanderName, JourneyFrom, JourneyTo, DriverName, Date, Time, VehicleNumber, OverallRiskLevel;

    RelativeLayout JourneyStartComplete;
    TextView StartJourneyDateTime, JourneyCompletedDateTime, StartJourneyDateTimeLabel, JourneyCompletedDateTimeLabel;
    CheckBox KeepForm;

    int StartJouneyAns, JourneyCompletedAns;
    String StartJourneyDateTimeAns, JourneyCompletedDateTimeAns;
    String getFullName;
    String CountersigningOfficerNameAns, DateCountersigningOfficerAns, TimeCountersigningOfficerAns;
    String VehicleCommanderNameAns, DateVehicleCommanderAns, TimeVehicleCommanderAns;
    String DriverNameAns, DateDriverAns, TimeDriverAns, JourneyFromAns, JourneyToAns, VehicleNumberAns;
    int DrivingRiding, ExperienceRisk, TrainedandFamiliarisedRisk, SameAsLastDetail, VehicleTypeRisk, LengthofRestRisk, MyHealthRisk, WeatherRisk, RouteFamiliarityRisk, PurposeofMissionRisk, VehicleCommanderPresentRisk, VehicleMotorServiceabilityRisk;

    int OverallRiskLevelAns, Completion, Reject, Driver1, Driver2, Driver3, Driver4, Driver5, Driver6, Driver7;
    int VehComm1, VehComm2, VehComm3, VehComm4, VehComm5, VehComm6, VehComm7, VehComm8, VehComm9, VehComm10, VehComm11, VehComm12;
    int KeepFormAns;

    private int Year, Month, Day, Hour, Minute, Select;
    private String Month2, Day2, Hour2, Minute2;

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_mtrac__review);

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
        StartJourney=(Button)findViewById(R.id.StartJourney);
        JourneyCompleted=(Button)findViewById(R.id.JourneyCompleted);

        //Assigns TextView to widgets
        FormStatus=(TextView)findViewById(R.id.FormStatus);
        CountersigningOfficerName=(TextView)findViewById(R.id.CountersigningOfficerName);
        VehicleCommanderName=(TextView)findViewById(R.id.VehCommName);

        JourneyFrom=(TextView)findViewById(R.id.JourneyFrom);
        JourneyTo=(TextView)findViewById(R.id.JourneyTo);
        DriverName=(TextView)findViewById(R.id.SubmitMTRAC);
        Date=(TextView)findViewById(R.id.DateDriver);
        Time=(TextView)findViewById(R.id.TimeDriver);
        VehicleNumber=(TextView)findViewById(R.id.VehicleNumber);
        OverallRiskLevel=(TextView)findViewById(R.id.OverallRiskLevel);

        //Assigns RelativeLayout to widgets
        JourneyStartComplete=(RelativeLayout)findViewById(R.id.JourneyStartComplete);

        //Assigns TextView to widgets
        StartJourneyDateTimeLabel=(TextView)findViewById(R.id.StartJourneyDateTimeLabel);
        StartJourneyDateTime=(TextView)findViewById(R.id.StartJourneyDateTime);
        JourneyCompletedDateTimeLabel=(TextView)findViewById(R.id.JourneyCompletedDateTimeLabel);
        JourneyCompletedDateTime=(TextView)findViewById(R.id.JourneyCompletedDateTime);

        //Assigns CheckBox to widgets
        KeepForm=(CheckBox)findViewById(R.id.KeepForm);

        //Listens for button clicks in Driver
        StartJourney.setOnClickListener(this);
        JourneyCompleted.setOnClickListener(this);
        KeepForm.setOnClickListener(this);

        //Sets initial state for StartJourney and JourneyCompleted Buttons
        StartJourney.setEnabled(Boolean.FALSE);
        JourneyCompleted.setEnabled(Boolean.FALSE);
        StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
        JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));

        //Hides Layout Initially
        JourneyStartComplete.setVisibility(View.GONE);
        StartJourneyDateTimeLabel.setVisibility(View.GONE);
        StartJourneyDateTime.setVisibility(View.GONE);
        JourneyCompletedDateTimeLabel.setVisibility(View.GONE);
        JourneyCompletedDateTime.setVisibility(View.GONE);
        KeepForm.setVisibility(View.GONE);

        getFullName();  //Gets Full Name, then gets data from Outsystems and inserts into TextView Widgets

        new thread().execute();
    }

    //Function to add Setting Menu on Right side of Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Log.i(TAG, "Action Bar Button: " +item.toString());

        if (item.toString().matches("clear")){
            String Title = "Delete MT-RAC";
            String Message = "Are you sure you want to delete the MT-RAC?";
            clearDialog(Title, Message);
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View view) {
        if (view == StartJourney){
            Select = 1;     //Sets Select as 1 to initialise StartJourneyDateTimeAns
            DateTime();    //Calls method to initialise DateDriver and TimeDriver
            StartJouneyAns = 1;
            JourneyCompletedAns = 0;
            putDataOutsystemsStartJourney();
            setsTextView();    //Re-initialises TextView to show items related to StartJourney and JourneyCompleted
        }

        if (view == JourneyCompleted){
            Select = 2;     //Sets Select as 1 to initialise JourneyCompletedDateTimeAns
            DateTime();    //Calls method to initialise DateDriver and TimeDriver
            StartJouneyAns = 1;
            JourneyCompletedAns = 1;
            putDataOutsystemsJourneyCompleted();
            setsTextView();    //Re-initialises TextView to show items related to StartJourney and JourneyCompleted
        }

        if (view == KeepForm){
            if (KeepForm.isChecked()){
                KeepFormAns = 1;
            }
            else {
                KeepFormAns = 0;
            }
            putDataOutsystemsKeepForm();
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

                        GetDataOutsystems();    //Gets data from Outsystems and inserts into TextView Widgets
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

                        Completion = Integer.parseInt(json.getString("Completion"));

                        if (Completion == 0){
                            Reject = Integer.parseInt(json.getString("Reject"));
                        }

                        VehicleCommanderPresentRisk = Integer.parseInt(json.getString("VehicleCommanderPresent"));

                        StartJouneyAns = Integer.parseInt(json.getString("StartJourney"));

                        if (StartJouneyAns==1){
                            StartJourneyDateTimeAns = json.getString("StartJourneyDateTime").toString();
                            JourneyCompletedAns = json.getInt("JourneyCompleted");
                            if (JourneyCompletedAns==1){
                                JourneyCompletedDateTimeAns = json.getString("JourneyCompletedDateTime").toString();
                            }
                        }

                        CountersigningOfficerNameAns = json.getString("CountersigningOfficer");
                        if (Completion>=2){ //Means MTRAC has been approved by COuntersigning Officer
                            DateCountersigningOfficerAns = json.getString("CountersigningOfficerDate");
                            TimeCountersigningOfficerAns = json.getString("CountersigningOfficerTime");
                        }

                        VehicleCommanderNameAns = json.getString("VehicleCommander");
                        if (VehicleCommanderPresentRisk==0){    //Checks if Vehicle Commander is present
                            if (Completion==3){ //Means MTRAC has been approved thus if there is a Vehicle Commander, this will be filled
                                DateVehicleCommanderAns = json.getString("VehicleCommanderDate");
                                TimeVehicleCommanderAns = json.getString("VehicleCommanderTime");
                            }
                        }

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
                        VehicleMotorServiceabilityRisk = Integer.parseInt(json.getString("VehicleMotorServiceability"));
                        OverallRiskLevelAns = Integer.parseInt(json.getString("OverallRiskLevel"));

                        Driver1 = Integer.parseInt(json.getString("Driver1"));
                        Driver2 = Integer.parseInt(json.getString("Driver2"));
                        Driver3 = Integer.parseInt(json.getString("Driver3"));
                        Driver4 = Integer.parseInt(json.getString("Driver4"));
                        Driver5 = Integer.parseInt(json.getString("Driver5"));
                        Driver6 = Integer.parseInt(json.getString("Driver6"));
                        Driver7 = Integer.parseInt(json.getString("Driver7"));

                        if (Completion>=2){ //Means MTRAC has been approved by COuntersigning Officer
                            VehComm1 = Integer.parseInt(json.getString("VehComm1"));
                            VehComm2 = Integer.parseInt(json.getString("VehComm2"));
                            VehComm3 = Integer.parseInt(json.getString("VehComm3"));
                            VehComm4 = Integer.parseInt(json.getString("VehComm4"));
                            VehComm5 = Integer.parseInt(json.getString("VehComm5"));
                            VehComm6 = Integer.parseInt(json.getString("VehComm6"));
                            VehComm7 = Integer.parseInt(json.getString("VehComm7"));
                            VehComm8 = Integer.parseInt(json.getString("VehComm8"));
                            VehComm9 = Integer.parseInt(json.getString("VehComm9"));
                            VehComm10 = Integer.parseInt(json.getString("VehComm10"));
                            VehComm11 = Integer.parseInt(json.getString("VehComm11"));
                            VehComm12 = Integer.parseInt(json.getString("VehComm12"));
                        }

//                        KeepFormAns = Integer.parseInt(json.getString("IsProtected"));

                        Log.v(TAG, "Completion is: " +Completion);
                        Log.v(TAG, "Reject is: " +Reject);
                        Log.v(TAG, "" +StartJouneyAns);

                        setsTextView(); //Inserts into TextView Widgets
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

    public SpannableStringBuilder StringBuilder(String Title, String Value){
        SpannableStringBuilder StringBuilder = new SpannableStringBuilder();
        StringBuilder.append(Title);
        StringBuilder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, StringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        StringBuilder.append(Value);

        return StringBuilder;
    }

    public void setsTextView(){ //Sets every TextView widget

        if (Completion==0){ //0 means MTRAC has been Rejected
            if (Reject==1){
                FormStatus.setText("MT-RAC has been rejected by the Countersigning Officer.");
            }
            if (Reject==2){
                FormStatus.setText("MT-RAC has been rejected by the Vehicle Commander.");
            }

            //Do not enable StartJourney and JourneyCompleted Buttons
            StartJourney.setEnabled(Boolean.FALSE);
            JourneyCompleted.setEnabled(Boolean.FALSE);
            StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
            JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));
        }
        if (Completion==1){ //1 means MTRAC form has not been approved by Vehicle Commander
            FormStatus.setText("Awaiting approval by Countersigning Officer.");

            StartJourney.setEnabled(Boolean.FALSE);
            JourneyCompleted.setEnabled(Boolean.FALSE);
            StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
            JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));
        }
        if (Completion==2){ //2 means MTRAC form has been approved by Vehicle Commander but has not been approved by Countersigning Officer
            FormStatus.setText("Countersigning Officer has approved. Awaiting Vehicle Commander Approval.");

            StartJourney.setEnabled(Boolean.FALSE);
            JourneyCompleted.setEnabled(Boolean.FALSE);
            StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
            JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));
        }
        if (Completion==3){ //3 means MTRAC form has been approved by Vehicle Commander and Countersigning Officer
            FormStatus.setText("MTRAC has been Approved. You may now start your journey.");

            if (StartJouneyAns==0){
                StartJourney.setEnabled(Boolean.TRUE);
                JourneyCompleted.setEnabled(Boolean.FALSE);
                StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.green2)));
                JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));

                JourneyStartComplete.setVisibility(View.GONE);
            }
        }

        if (StartJouneyAns==1){ //If StartJourney button has been pressed
            StartJourney.setEnabled(Boolean.FALSE);
            JourneyCompleted.setEnabled(Boolean.TRUE);
            StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
            JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.red)));

            JourneyStartComplete.setVisibility(View.VISIBLE);
            StartJourneyDateTimeLabel.setVisibility(View.VISIBLE);
            StartJourneyDateTime.setVisibility(View.VISIBLE);
            JourneyCompletedDateTimeLabel.setVisibility(View.GONE);
            JourneyCompletedDateTime.setVisibility(View.GONE);

            StartJourneyDateTime.setText(setDateTime(StartJourneyDateTimeAns));

            if (JourneyCompletedAns == 1){  //If JourneyCompleted button has been pressed
                StartJourney.setEnabled(Boolean.FALSE);
                JourneyCompleted.setEnabled(Boolean.FALSE);
                StartJourney.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palegreen)));
                JourneyCompleted.setBackgroundTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.palered)));

                JourneyStartComplete.setVisibility(View.VISIBLE);
                StartJourneyDateTimeLabel.setVisibility(View.VISIBLE);
                StartJourneyDateTime.setVisibility(View.VISIBLE);
                JourneyCompletedDateTimeLabel.setVisibility(View.VISIBLE);
                JourneyCompletedDateTime.setVisibility(View.VISIBLE);

                JourneyCompletedDateTime.setText(setDateTime(JourneyCompletedDateTimeAns));

                KeepForm.setVisibility(View.VISIBLE);   //Sets CheckBox Visible
            }
        }

        CountersigningOfficerName.setText(CountersigningOfficerNameAns);
        VehicleCommanderName.setText(VehicleCommanderNameAns);

        DriverName.setText(StringBuilder("Driver: ", DriverNameAns));
        Date.setText(StringBuilder("Date of MT-RAC: ", setDate(DateDriverAns)));
        Time.setText(StringBuilder("Time of MT-RAC: ", TimeDriverAns.substring(0,5)));
        JourneyFrom.setText(StringBuilder("Disembarkation Point: ", JourneyFromAns));
        JourneyTo.setText(StringBuilder("Destination: ", JourneyToAns));
        VehicleNumber.setText(StringBuilder("Vehicle Number: ", VehicleNumberAns));

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

        if (KeepFormAns==1) {
            KeepForm.setChecked(Boolean.TRUE);
        }
        if (KeepFormAns==0) {
            KeepForm.setChecked(Boolean.FALSE);
        }

        progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
    }

    public void putDataOutsystemsStartJourney(){
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
                headers.put("Accept", "application/json");

                Log.i(TAG, headers.toString());

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    JSONObject parent = new JSONObject();

                    parent.put("Id", getIntent().getStringExtra("IDform"));
                    parent.put("StartJourney", StartJouneyAns);
                    parent.put("StartJourneyDateTime", StartJourneyDateTimeAns);

                    parent.put("JourneyCompleted", JourneyCompletedAns);

                    parent.put("CountersigningOfficer", CountersigningOfficerNameAns);
                    parent.put("CountersigningOfficerDate", DateCountersigningOfficerAns);
                    parent.put("CountersigningOfficerTime", TimeCountersigningOfficerAns);

                    parent.put("VehicleCommander", VehicleCommanderNameAns);
                    if (VehicleCommanderPresentRisk==0) {    //If Vehicle Commander is present get Name
                        parent.put("VehicleCommanderDate", DateVehicleCommanderAns);
                        parent.put("VehicleCommanderTime", TimeVehicleCommanderAns);
                    }

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);

                    parent.put("Driver", getFullName);
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

                    parent.put("VehComm1", VehComm1);
                    parent.put("VehComm2", VehComm2);
                    parent.put("VehComm3", VehComm3);
                    parent.put("VehComm4", VehComm4);
                    parent.put("VehComm5", VehComm5);
                    parent.put("VehComm6", VehComm6);
                    parent.put("VehComm7", VehComm7);
                    parent.put("VehComm8", VehComm8);
                    parent.put("VehComm9", VehComm9);
                    parent.put("VehComm10", VehComm10);
                    parent.put("VehComm11", VehComm11);
                    parent.put("VehComm12", VehComm12);

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

    public void putDataOutsystemsJourneyCompleted(){
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
                headers.put("Accept", "application/json");

                Log.i(TAG, headers.toString());

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    JSONObject parent = new JSONObject();

                    parent.put("Id", getIntent().getStringExtra("IDform"));
                    parent.put("StartJourney", StartJouneyAns);
                    parent.put("StartJourneyDateTime", StartJourneyDateTimeAns);

                    parent.put("JourneyCompleted", JourneyCompletedAns);
                    parent.put("JourneyCompletedDateTime", JourneyCompletedDateTimeAns);

                    parent.put("CountersigningOfficer", CountersigningOfficerNameAns);
                    parent.put("CountersigningOfficerDate", DateCountersigningOfficerAns);
                    parent.put("CountersigningOfficerTime", TimeCountersigningOfficerAns);

                    parent.put("VehicleCommander", VehicleCommanderNameAns);
                    if (VehicleCommanderPresentRisk==0) {    //If Vehicle Commander is present get Name
                        parent.put("VehicleCommanderDate", DateVehicleCommanderAns);
                        parent.put("VehicleCommanderTime", TimeVehicleCommanderAns);
                    }

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);

                    parent.put("Driver", getFullName);
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

                    parent.put("VehComm1", VehComm1);
                    parent.put("VehComm2", VehComm2);
                    parent.put("VehComm3", VehComm3);
                    parent.put("VehComm4", VehComm4);
                    parent.put("VehComm5", VehComm5);
                    parent.put("VehComm6", VehComm6);
                    parent.put("VehComm7", VehComm7);
                    parent.put("VehComm8", VehComm8);
                    parent.put("VehComm9", VehComm9);
                    parent.put("VehComm10", VehComm10);
                    parent.put("VehComm11", VehComm11);
                    parent.put("VehComm12", VehComm12);

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

    public void putDataOutsystemsKeepForm(){
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
                headers.put("Accept", "application/json");

                Log.i(TAG, headers.toString());

                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    JSONObject parent = new JSONObject();

                    parent.put("Id", getIntent().getStringExtra("IDform"));
                    parent.put("StartJourney", StartJouneyAns);
                    parent.put("StartJourneyDateTime", StartJourneyDateTimeAns);

                    parent.put("JourneyCompleted", JourneyCompletedAns);
                    parent.put("JourneyCompletedDateTime", JourneyCompletedDateTimeAns);

                    parent.put("CountersigningOfficer", CountersigningOfficerNameAns);
                    parent.put("CountersigningOfficerDate", DateCountersigningOfficerAns);
                    parent.put("CountersigningOfficerTime", TimeCountersigningOfficerAns);

                    parent.put("VehicleCommander", VehicleCommanderNameAns);
                    parent.put("VehicleCommander", VehicleCommanderNameAns);
                    if (VehicleCommanderPresentRisk==0) {    //If Vehicle Commander is present get Name
                        parent.put("VehicleCommanderDate", DateVehicleCommanderAns);
                        parent.put("VehicleCommanderTime", TimeVehicleCommanderAns);
                    }

                    parent.put("JourneyFrom", JourneyFromAns);
                    parent.put("JourneyTo", JourneyToAns);

                    parent.put("Driver", getFullName);
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

                    parent.put("VehComm1", VehComm1);
                    parent.put("VehComm2", VehComm2);
                    parent.put("VehComm3", VehComm3);
                    parent.put("VehComm4", VehComm4);
                    parent.put("VehComm5", VehComm5);
                    parent.put("VehComm6", VehComm6);
                    parent.put("VehComm7", VehComm7);
                    parent.put("VehComm8", VehComm8);
                    parent.put("VehComm9", VehComm9);
                    parent.put("VehComm10", VehComm10);
                    parent.put("VehComm11", VehComm11);
                    parent.put("VehComm12", VehComm12);

                    parent.put("Completion", 3);

                    parent.put("IsProtected", KeepFormAns);

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

    public void DeleteDataOutsystems(){
        Log.i(TAG, "Sending DELETE request to Apache Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urldelete = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/MTRACforms/Delete?Id=" +getIntent().getStringExtra("IDform"); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a DELETE request to the provided URL
        //StringRequest specifies the request type, target URL and how to handle target responses and errors
        StringRequest stringRequestget = new StringRequest(Request.Method.DELETE, server_urldelete,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler

                        String Title = "Deleted";
                        String Message = "MT-RAC is deleted.";
                        String ButtonText = "OK";
                        alertDialog(Title, Message, ButtonText);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //Error Handler
//                error.printStackTrace();

                String Title = "Deleted";
                String Message = "MT-RAC is not deleted.";
                String ButtonText = "OK";
                alertDialog(Title, Message, ButtonText);

                Log.v(TAG, "Unexpected Error");
                requestQueue.stop();
            }
        });

        //Starts Request
        requestQueue.add(stringRequestget);
    }

    public void DateTime() {
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH)+1;
        Month2 = Integer.toString(c.get(Calendar.MONTH)+1);
        Day = c.get(Calendar.DAY_OF_MONTH);
        Day2 = Integer.toString(c.get(Calendar.DAY_OF_MONTH));

        Hour = c.get(Calendar.HOUR_OF_DAY);
        Hour2 = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        Minute = c.get(Calendar.MINUTE);
        Minute2 = Integer.toString(c.get(Calendar.MINUTE));

        //If month < 10, add a 0 in front
        if(Month<10) {
            Month2="0"+Month;
        }

        //If day < 10, add a 0 in front
        if(Day<10) {
            Day2="0"+Day;
        }

        //If hour < 10, add a 0 in front
        if(Hour<10) {
            Hour2="0"+Hour;
        }

        //If minute < 10, add a 0 in front
        if(Minute<10) {
            Minute2="0"+Minute;
        }

        if (Select == 1){
            StartJourneyDateTimeAns = Year+ "-" +Month2+ "-" +Day2+ " " +Hour+ ":" +Minute+ ":00" ;
        }
        if (Select == 2){
            JourneyCompletedDateTimeAns = Year+ "-" +Month2+ "-" +Day2+ " " +Hour+ ":" +Minute+ ":00" ;
        }
    }

    public String setDateTime(String DateTime) {
        String[] Months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        String setDateTime, Year, Day, Hour2, Minute2;
        int Month, Hour, Minute;

        //Sets Date portion-----------------------------------------------------------
        Year = DateTime.substring(0, 4);
        Month = Integer.parseInt(DateTime.substring(5, 7));
        Day = DateTime.substring(8, 10);

        //Sets Time portion-----------------------------------------------------------
        Hour = Integer.parseInt(DateTime.substring(11,13));
        Minute = Integer.parseInt(DateTime.substring(14,16));

        setDateTime = Day+" "+Months[Month-1]+" "+Year+ " " +Hour+":"+Minute; //Initialises with Date followed by Time

//        //If hour < 10, add a 0 in front
//        if(Hour<10) {
//            Hour2="0"+Hour;
//        }
//        else{
//            Hour2=""+Hour;
//        }
//
//        //If minute < 10, add a 0 in front
//        if(Minute<10) {
//            Minute2="0"+Minute;
//        }
//        else{
//            Minute2=""+Minute;
//        }

//        setDateTime = Day+" "+Months[Month-1]+" "+Year+ " " +Hour2+":"+Minute2; //Initialises with Date followed by Time

        return setDateTime;
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

        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mtram = new Intent(getBaseContext(), com.example.portal.mtram.class);
                startActivity(mtram);
                finish();
            }
        });

        alert.setCancelable(false);
        alert.setView(dialogView);
        alert.show();
    }

    public void clearDialog(String StringTitle, String StringMessage) {
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
                Log.i(TAG, "Delete MT-RAC");
                DeleteDataOutsystems();
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
        Intent Back = new Intent(this, Your_MTRAC.class);
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
