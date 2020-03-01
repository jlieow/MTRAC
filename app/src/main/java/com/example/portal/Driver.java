package com.example.portal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Driver extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "portal";

    //For placesautocomplete-----------------------------------------------------------------------------------------
    private static final int GOOGLE_API_CLIENT_ID = 0;

    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)      //Returns only autocomplete results with a precise address
            .setCountry("SGP")                                           //Filter autocomplete results to a specific country, Singapore
            .build();

//    private static final LatLngBounds BOUNDS_SINGAPORE = new LatLngBounds(          //Keeps search results within Singapore's Lata

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapterFrom;
    private PlaceArrayAdapter mPlaceArrayAdapterTo;

    String SelectedJourneyFrom = "Null";
    String SelectedJourneyTo = "Null";
    //For placesautocomplete-----------------------------------------------------------------------------------------

    //Variable Declarations
    AutoCompleteTextView autoTextViewCountersigningOfficerName, autoTextViewVehCommName, autoTextViewJourneyFrom, autoTextViewJourneyTo, autoTextViewVehicleNumber;
    CheckBox CheckBoxVehComm;
    Button Next;
    TextView DrivingExperienceText, VehicleTypeText, IfDifferentText, LengthOfRestText;

    RadioGroup DrivingRiding, Experience1, TrainedandFamiliarised, VehicleType, NewVehicleType, LengthofRest, LessThan6HoursLocation, MyHealth, Weather, RouteFamiliarity, PurposeofMission, VehicleMotorcycleServiceability;
    RadioButton Driving, Riding, Driving_Exp_A, Driving_Exp_B, Driving_Exp_C, Yes_trained_and_familiarised, Not_trained_or_familiarised, Same_as_last_detail, Different, Vehicle_Type_A, Vehicle_Type_B, Vehicle_Type_C;
    RadioButton More_than_6_hours, Less_than_6_hours, Home_Camp, Field, Good, Fair_Just_recovered_from_illness_1_day, Poor_Still_ill_or_on_Attend_C, Still_under_medication_that_causes_drowsiness;
    RadioButton Dry, Wet,  Heavy_Showers, Familiar_With_Route, Not_Familiar_With_Route, Admin, Training_Special_Mission_Oriented_Towing_Vehicle_Gun, Occasional_Towing_Trailer_Vehicle_Gun;
    RadioButton No_Fault, Minor_Fault, Major_Fault;

    private int Year, Month, Day, Hour, Minute, Second;
    private String Date, Time, Hour2, Minute2, DateFormat, TimeFormat;

    String ExperienceAns, TrainedandFamiliarisedAns, VehicleTypeAns, LengthofRestAns, MyHealthAns, WeatherAns, RouteFamiliarityAns, PurposeofMissionAns, VehicleCommanderPresentAns, VehicleMotorcycleServiceabilityAns;
    int DrivingRidingAns, ExperienceRisk, TrainedandFamiliarisedRisk, SameAsLastDetail, VehicleTypeRisk, LengthofRestRisk, MyHealthRisk, WeatherRisk, RouteFamiliarityRisk, PurposeofMissionRisk, VehicleCommanderPresentRisk, VehicleMotorcycleServiceabilityRisk, OverallRiskLevel;
    int IncompleteFormEditText, IncompleteFormRadioButton;

    String[] Months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    String JSONobj;
    String getFullName;
    List<String> autoNames = new ArrayList<String>();
    List<String> autoVehNumber = new ArrayList<String>();

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        //ActionBar Settings
//        getSupportActionBar().hide();   //Hides ActionBar
        getSupportActionBar().setTitle(""); //Change title of Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Set whether home should be displayed as an "up" affordance.

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

        //Assigns Button to widgets
        Next=(Button)findViewById(R.id.Next);

        //Assigns AutoCompleteTextView to widgets
        autoTextViewCountersigningOfficerName=(AutoCompleteTextView)findViewById(R.id.autoTextViewCountersigningOfficerName);
        autoTextViewVehCommName=(AutoCompleteTextView)findViewById(R.id.autoTextViewVehCommName);
        autoTextViewJourneyFrom=(AutoCompleteTextView)findViewById(R.id.autoTextViewJourneyFrom);
        autoTextViewJourneyTo=(AutoCompleteTextView)findViewById(R.id.autoTextViewJourneyTo);
        autoTextViewVehicleNumber=(AutoCompleteTextView)findViewById(R.id.autoTextViewVehicleNumber);

        //Assigns CheckBox to widgets
        CheckBoxVehComm=(CheckBox)findViewById(R.id.CheckBoxVehComm);

        //Assigns TextView to widgets
        DrivingExperienceText=(TextView)findViewById(R.id.DrivingExperienceText);
        VehicleTypeText=(TextView)findViewById(R.id.VehicleTypeText);
        IfDifferentText=(TextView)findViewById(R.id.IfDifferentText);
        LengthOfRestText=(TextView)findViewById(R.id.LengthOfRestText);

        //Assigns RadioGroup to widgets
        DrivingRiding=(RadioGroup)findViewById(R.id.DrivingRiding);
        Experience1=(RadioGroup)findViewById(R.id.Experience1);
        TrainedandFamiliarised=(RadioGroup)findViewById(R.id.TrainedandFamiliarised);
        VehicleType=(RadioGroup)findViewById(R.id.VehicleType);
        NewVehicleType=(RadioGroup)findViewById(R.id.NewVehicleType);
        LengthofRest=(RadioGroup)findViewById(R.id.LengthofRest);
        LessThan6HoursLocation=(RadioGroup)findViewById(R.id.LessThan6HoursLocation);
        MyHealth=(RadioGroup)findViewById(R.id.MyHealth);
        Weather=(RadioGroup)findViewById(R.id.Weather);
        RouteFamiliarity=(RadioGroup)findViewById(R.id.RouteFamiliarity);
        PurposeofMission=(RadioGroup)findViewById(R.id.PurposeofMission);
        VehicleMotorcycleServiceability=(RadioGroup)findViewById(R.id.VehicleMotorcycleServiceability);

        //Assigns RadioButton to widgets
        Driving=(RadioButton)findViewById(R.id.Driving);
        Riding=(RadioButton)findViewById(R.id.Riding);
        Driving_Exp_A=(RadioButton)findViewById(R.id.Driving_Exp_A);
        Driving_Exp_B=(RadioButton)findViewById(R.id.Driving_Exp_B);
        Driving_Exp_C=(RadioButton)findViewById(R.id.Driving_Exp_C);
        Yes_trained_and_familiarised=(RadioButton)findViewById(R.id.Yes_trained_and_familiarised);
        Not_trained_or_familiarised=(RadioButton)findViewById(R.id.Not_trained_or_familiarised);
        Same_as_last_detail=(RadioButton)findViewById(R.id.Same_as_last_detail);
        Different=(RadioButton)findViewById(R.id.Different);
        Vehicle_Type_A=(RadioButton)findViewById(R.id.Vehicle_Type_A);
        Vehicle_Type_B=(RadioButton)findViewById(R.id.Vehicle_Type_B);
        Vehicle_Type_C=(RadioButton)findViewById(R.id.Vehicle_Type_C);
        More_than_6_hours=(RadioButton)findViewById(R.id.More_than_6_hours);
        Less_than_6_hours=(RadioButton)findViewById(R.id.Less_than_6_hours);
        Home_Camp=(RadioButton)findViewById(R.id.Home_Camp);
        Field=(RadioButton)findViewById(R.id.Field);
        Good=(RadioButton)findViewById(R.id.Good);
        Fair_Just_recovered_from_illness_1_day=(RadioButton)findViewById(R.id.Fair_Just_recovered_from_illness_1_day);
        Poor_Still_ill_or_on_Attend_C=(RadioButton)findViewById(R.id.Poor_Still_ill_or_on_Attend_C);
        Still_under_medication_that_causes_drowsiness=(RadioButton)findViewById(R.id.Still_under_medication_that_causes_drowsiness);
        Dry=(RadioButton)findViewById(R.id.Dry);
        Wet=(RadioButton)findViewById(R.id.Wet);
        Heavy_Showers=(RadioButton)findViewById(R.id.Heavy_Showers);
        Familiar_With_Route=(RadioButton)findViewById(R.id.Familiar_With_Route);
        Not_Familiar_With_Route=(RadioButton)findViewById(R.id.Not_Familiar_With_Route);
        Admin=(RadioButton)findViewById(R.id.Admin);
        Training_Special_Mission_Oriented_Towing_Vehicle_Gun=(RadioButton)findViewById(R.id.Training_Special_Mission_Oriented_Towing_Vehicle_Gun);
        Occasional_Towing_Trailer_Vehicle_Gun=(RadioButton)findViewById(R.id.Occasional_Towing_Trailer_Vehicle_Gun);
        No_Fault=(RadioButton)findViewById(R.id.No_Fault);
        Minor_Fault=(RadioButton)findViewById(R.id.Minor_Fault);
        Major_Fault=(RadioButton)findViewById(R.id.Major_Fault);


        //Listens for Button clicks
        CheckBoxVehComm.setOnClickListener(this);
        Next.setOnClickListener(this);

        //Listens for Radio Button Clicks
        Driving.setOnClickListener(this);
        Riding.setOnClickListener(this);
        Driving_Exp_A.setOnClickListener(this);
        Driving_Exp_B.setOnClickListener(this);
        Driving_Exp_C.setOnClickListener(this);
        Yes_trained_and_familiarised.setOnClickListener(this);
        Not_trained_or_familiarised.setOnClickListener(this);
        Same_as_last_detail.setOnClickListener(this);
        Different.setOnClickListener(this);
        Vehicle_Type_A.setOnClickListener(this);
        Vehicle_Type_B.setOnClickListener(this);
        Vehicle_Type_C.setOnClickListener(this);
        More_than_6_hours.setOnClickListener(this);
        Less_than_6_hours.setOnClickListener(this);
        Home_Camp.setOnClickListener(this);
        Field.setOnClickListener(this);
        Good.setOnClickListener(this);
        Fair_Just_recovered_from_illness_1_day.setOnClickListener(this);
        Poor_Still_ill_or_on_Attend_C.setOnClickListener(this);
        Still_under_medication_that_causes_drowsiness.setOnClickListener(this);
        Dry.setOnClickListener(this);
        Wet.setOnClickListener(this);
        Heavy_Showers.setOnClickListener(this);
        Familiar_With_Route.setOnClickListener(this);
        Not_Familiar_With_Route.setOnClickListener(this);
        Admin.setOnClickListener(this);
        Training_Special_Mission_Oriented_Towing_Vehicle_Gun.setOnClickListener(this);
        Occasional_Towing_Trailer_Vehicle_Gun.setOnClickListener(this);
        No_Fault.setOnClickListener(this);
        Minor_Fault.setOnClickListener(this);
        Major_Fault.setOnClickListener(this);


        //Calls method to initialise DateDriver and TimeDriver
        DateTime();

        //Sets initial states for Radio Buttons
        DrivingExperienceText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Driving_Exp_A.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Driving_Exp_B.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Driving_Exp_C.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Driving_Exp_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Driving_Exp_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Driving_Exp_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Driving_Exp_A.setEnabled(false);
        Driving_Exp_B.setEnabled(false);
        Driving_Exp_C.setEnabled(false);

        VehicleTypeText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Same_as_last_detail.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Different.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Same_as_last_detail.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Different.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Same_as_last_detail.setEnabled(false);
        Different.setEnabled(false);

        IfDifferentText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Vehicle_Type_A.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Vehicle_Type_B.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Vehicle_Type_C.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Vehicle_Type_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Vehicle_Type_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Vehicle_Type_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Vehicle_Type_A.setEnabled(false);
        Vehicle_Type_B.setEnabled(false);
        Vehicle_Type_C.setEnabled(false);

        LengthOfRestText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Home_Camp.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Field.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
        Home_Camp.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Field.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
        Home_Camp.setEnabled(false);
        Field.setEnabled(false);

        //Sets initial states for variables
        DrivingRidingAns=0;
        ExperienceRisk=0;
        TrainedandFamiliarisedRisk=0;
        SameAsLastDetail=0;
        VehicleTypeRisk=0;
        LengthofRestRisk=0;
        MyHealthRisk=0;
        WeatherRisk=0;
        RouteFamiliarityRisk=0;
        PurposeofMissionRisk=0;
        VehicleCommanderPresentRisk=0;
        VehicleMotorcycleServiceabilityRisk=0;
        OverallRiskLevel=0;
        IncompleteFormEditText=0;
        IncompleteFormRadioButton=0;

        //Loads autocompletetextview with data
        ArrayAdapter<String> adapterCountersigningOfficer = new ArrayAdapter<String>(this, R.layout.my_list_item_myautocomplete, autoNames);
        ArrayAdapter<String> adapterVehicleCommander = new ArrayAdapter<String>(this, R.layout.my_list_item_myautocomplete, autoNames);
        ArrayAdapter<String> adapterVehicleNumber = new ArrayAdapter<String>(this, R.layout.my_list_item_myautocomplete, autoVehNumber);

        //Used to specify minimum number of characters the user has to type in order to display the drop down hint.
        autoTextViewCountersigningOfficerName.setThreshold(1);
        autoTextViewVehCommName.setThreshold(1);
        autoTextViewVehicleNumber.setThreshold(1);

        //Setting adapter
        autoTextViewCountersigningOfficerName.setAdapter(adapterCountersigningOfficer);
        autoTextViewVehCommName.setAdapter(adapterVehicleCommander);
        autoTextViewVehicleNumber.setAdapter(adapterVehicleNumber);


        //For placesautocomplete-----------------------------------------------------------------------------------------
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        autoTextViewJourneyFrom.setThreshold(2);
        autoTextViewJourneyTo.setThreshold(2);

        autoTextViewJourneyFrom.setOnItemClickListener(mAutocompleteClickListenerFrom);
        mPlaceArrayAdapterFrom = new PlaceArrayAdapter(this, R.layout.placesautocompletelistitem,
                null, typeFilter);
        autoTextViewJourneyFrom.setAdapter(mPlaceArrayAdapterFrom);

        autoTextViewJourneyTo.setOnItemClickListener(mAutocompleteClickListenerTo);
        mPlaceArrayAdapterTo = new PlaceArrayAdapter(this, R.layout.placesautocompletelistitem,
                null, typeFilter);
        autoTextViewJourneyTo.setAdapter(mPlaceArrayAdapterTo);
        //For placesautocomplete-----------------------------------------------------------------------------------------

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
            getFullName(); //Get Driver's Full Name and loads autocompletetextview with data and removes User's name from list
            getVehicleNumber();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    //For placesautocomplete-----------------------------------------------------------------------------------------
    private AdapterView.OnItemClickListener mAutocompleteClickListenerFrom
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapterFrom.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);

            SelectedJourneyFrom = item.description.toString();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListenerTo
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapterTo.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);

            SelectedJourneyTo = item.description.toString();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

            Log.i(TAG, "Place: " + places.get(0).toString());

            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapterFrom.setGoogleApiClient(mGoogleApiClient);
        mPlaceArrayAdapterTo.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapterFrom.setGoogleApiClient(null);
        mPlaceArrayAdapterTo.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }
    //For placesautocomplete-----------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view == CheckBoxVehComm){
            if (CheckBoxVehComm.isChecked()){
                autoTextViewVehCommName.setText("");
                autoTextViewVehCommName.setEnabled(Boolean.FALSE);
            }
            else {
                autoTextViewVehCommName.setEnabled(Boolean.TRUE);
            }
        }

        /*******************************************************************************RadioButtons*******************************************************************************/
        //Assigns values to variables when each RadioButton is clicked
        RadioButtonClick(view);

        /*******************************************************************************When Next is clicked*******************************************************************************/

        if (view == Next){

            //Resets IncompleteForm and OverallRiskLevel
            IncompleteFormEditText=0;
            IncompleteFormRadioButton=0;
            OverallRiskLevel=0;

            //Checks for unselected RadioButtons and alculates Overall Risk Level
            Incomplete();
            OverallRiskLevel();

            Log.i(TAG, "autoTextViewJourneyFrom: " +autoTextViewJourneyFrom.getText().toString());
            Log.i(TAG, "autoTextViewJourneyTo: " +autoTextViewJourneyTo.getText().toString());

            Log.i(TAG, "SelectedJourneyFrom: " +SelectedJourneyFrom);
            Log.i(TAG, "SelectedJourneyTo: " +SelectedJourneyTo);

            if (IncompleteFormEditText==0 && IncompleteFormRadioButton==0 && autoTextViewJourneyFrom.getText().toString().matches(SelectedJourneyFrom) && autoTextViewJourneyTo.getText().toString().matches(SelectedJourneyTo)) {

                Intent Driver_Review = new Intent(this, com.example.portal.Driver_Review.class);
                //Passing Data via intent

                Driver_Review.putExtra("CountersigningOfficerName", autoTextViewCountersigningOfficerName.getText().toString().trim());

                if (CheckBoxVehComm.isChecked()){   //Vehicle Commander is not Present
                    Driver_Review.putExtra("VehCommName", "No Vehicle Commander Present");
                }
                else {  //Vehicle Commander is Present
                    Driver_Review.putExtra("VehCommName", autoTextViewVehCommName.getText().toString().trim());
                }
                Driver_Review.putExtra("JourneyFrom", autoTextViewJourneyFrom.getText().toString().trim());
                Driver_Review.putExtra("JourneyTo", autoTextViewJourneyTo.getText().toString().trim());
                Driver_Review.putExtra("Date", Date);
                Driver_Review.putExtra("Time", Time);
                Driver_Review.putExtra("DateFormat", DateFormat);
                Driver_Review.putExtra("TimeFormat", TimeFormat);
                Driver_Review.putExtra("VehicleNumber", autoTextViewVehicleNumber.getText().toString().trim());

                Driver_Review.putExtra("DrivingRiding", DrivingRidingAns);

                Driver_Review.putExtra("Experience", ExperienceAns);
                Driver_Review.putExtra("TrainedandFamiliarised", TrainedandFamiliarisedAns);
                Driver_Review.putExtra("VehicleType", VehicleTypeAns);
                Driver_Review.putExtra("LengthofRest", LengthofRestAns);
                Driver_Review.putExtra("MyHealth", MyHealthAns);
                Driver_Review.putExtra("Weather", WeatherAns);
                Driver_Review.putExtra("RouteFamiliarity", RouteFamiliarityAns);
                Driver_Review.putExtra("PurposeofMission", PurposeofMissionAns);
                Driver_Review.putExtra("VehicleCommanderPresent", VehicleCommanderPresentAns);
                Driver_Review.putExtra("VehicleMotorcycleServiceability", VehicleMotorcycleServiceabilityAns);

                //Risk Level of each MT Risk
                Driver_Review.putExtra("ExperienceRisk", ExperienceRisk);
                Driver_Review.putExtra("TrainedandFamiliarisedRisk", TrainedandFamiliarisedRisk);
                Driver_Review.putExtra("SameAsLastDetail", SameAsLastDetail);
                Driver_Review.putExtra("VehicleTypeRisk", VehicleTypeRisk);
                Driver_Review.putExtra("LengthofRestRisk", LengthofRestRisk);
                Driver_Review.putExtra("MyHealthRisk", MyHealthRisk);
                Driver_Review.putExtra("WeatherRisk", WeatherRisk);
                Driver_Review.putExtra("RouteFamiliarityRisk", RouteFamiliarityRisk);
                Driver_Review.putExtra("PurposeofMissionRisk", PurposeofMissionRisk);
                Driver_Review.putExtra("VehicleCommanderPresentRisk", VehicleCommanderPresentRisk);
                Driver_Review.putExtra("VehicleMotorcycleServiceabilityRisk", VehicleMotorcycleServiceabilityRisk);

                Driver_Review.putExtra("OverallRiskLevel", OverallRiskLevel);

                startActivity(Driver_Review);
            }
        }
    }

    public void DateTime() {
        final Calendar c = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH)+1;
        Day = c.get(Calendar.DAY_OF_MONTH);

        Hour = c.get(Calendar.HOUR_OF_DAY);
        Hour2 = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        Minute = c.get(Calendar.MINUTE);
        Minute2 = Integer.toString(c.get(Calendar.MINUTE));

        //If hour < 10, add a 0 in front
        if(Hour<10) {
            Hour2="0"+Hour;
        }

        //If minute < 10, add a 0 in front
        if(Minute<10) {
            Minute2="0"+Minute;
        }

        Date = Day+" "+Months[Month-1]+" "+Year;
        Time = Hour2+":"+Minute2;

        DateFormat = Year+ "-" +Month+ "-" +Day;
        TimeFormat = Hour+ ":" +Minute+ ":" +Second;
    }

    /****************************************************Used to get data for autocompletetextviewVehComm****************************************************/
    public void getFullName(){
        Log.i(TAG, "Checking Token");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByToken?Token=" +FirebaseInstanceId.getInstance().getToken(); //Points to target which is obtained from IPV4 Address from IP Config

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

                        FilterFullName(); //Loads autocompletetextview with data and removes User's name from list
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

    public void FilterFullName() {
        Log.i(TAG, "Sending GET request to Apache Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountDetails/Get";  //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v(TAG, response.toString());

                try {

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        Log.v(TAG, json.getString("FullName"));

                        autoNames.add(json.getString("FullName").trim());

                        autoNames.remove(getFullName);    //Removes User's Name
                    }

                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }
                progressanim.dismiss(); //Removes animation by dismissing ProgressDialog

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void getVehicleNumber() {
        Log.i(TAG, "Sending GET request to Apache Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/VehicleNumberList/Get";  //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.v(TAG, response.toString());

                try {

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject object = response.getJSONObject(i);
                        Log.v(TAG, object.toString());

                        JSONObject json = new JSONObject(object.toString());
                        Log.v(TAG, json.getString("VehicleNumber"));

                        autoVehNumber.add(json.getString("VehicleNumber").trim());
                    }

                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }

                autoNames.remove(getFullName);    //Removes User's Name
                progressanim.dismiss(); //Removes animation by dismissing ProgressDialog

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }
    /****************************************************Used to get data for autocompletetextviewVehComm****************************************************/


    public void RadioButtonClick(View view) {
        if (view == Driving) {
            DrivingExperienceText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_A.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_B.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_C.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_A.setEnabled(true);
            Driving_Exp_B.setEnabled(true);
            Driving_Exp_C.setEnabled(true);
            DrivingExperienceText.setText("Driving Experience:");
            Driving_Exp_A.setText("CAT A, B");
            Driving_Exp_B.setText("CAT C");
            Driving_Exp_C.setText("CAT D");

            Vehicle_Type_A.setText("GP Car/Pick Up");
            Vehicle_Type_B.setText("Mini-Bus/Jeep/LR/UM");
            Vehicle_Type_C.setText("Coach/3 Ton/5 Ton/7 Ton");
            Vehicle_Type_C.setVisibility(View.VISIBLE);

            DrivingRidingAns=1;

            VehicleTypeText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Same_as_last_detail.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Different.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Same_as_last_detail.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Different.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Same_as_last_detail.setEnabled(true);
            Different.setEnabled(true);
        }
        if (view == Riding) {
            DrivingExperienceText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_A.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_B.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_C.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Driving_Exp_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Driving_Exp_A.setEnabled(true);
            Driving_Exp_B.setEnabled(true);
            Driving_Exp_C.setEnabled(true);
            DrivingExperienceText.setText("Riding Experience:");
            Driving_Exp_A.setText("More than 6 mths");
            Driving_Exp_B.setText("More than 3 mths");
            Driving_Exp_C.setText("Less than 1 mth");

            Vehicle_Type_A.setText("200cc");
            Vehicle_Type_B.setText("750cc");
            Vehicle_Type_C.setVisibility(View.GONE);

            DrivingRidingAns=2;

            VehicleTypeText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Same_as_last_detail.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Different.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Same_as_last_detail.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Different.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Same_as_last_detail.setEnabled(true);
            Different.setEnabled(true);
        }
        if (view == Driving_Exp_A) {
            ExperienceRisk=1;
            ExperienceAns=Driving_Exp_A.getText().toString();

        }
        if (view == Driving_Exp_B) {
            ExperienceRisk=2;
            ExperienceAns=Driving_Exp_B.getText().toString();
        }
        if (view == Driving_Exp_C) {
            ExperienceRisk=3;
            ExperienceAns=Driving_Exp_C.getText().toString();
        }
        if (view == Yes_trained_and_familiarised) {
            TrainedandFamiliarisedRisk=1;
            TrainedandFamiliarisedAns=Yes_trained_and_familiarised.getText().toString();
        }
        if (view == Not_trained_or_familiarised) {
            TrainedandFamiliarisedRisk=4;
            TrainedandFamiliarisedAns=Not_trained_or_familiarised.getText().toString();
        }
        if (view == Same_as_last_detail) {
            IfDifferentText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Vehicle_Type_A.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Vehicle_Type_B.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Vehicle_Type_C.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Vehicle_Type_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
            Vehicle_Type_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
            Vehicle_Type_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
            Vehicle_Type_A.setEnabled(false);
            Vehicle_Type_B.setEnabled(false);
            Vehicle_Type_C.setEnabled(false);

            SameAsLastDetail=1;
            VehicleTypeAns="Same as last detail";
        }
        if (view == Different) {
            if (DrivingRidingAns == 1){
                IfDifferentText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_A.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_B.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_C.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
                Vehicle_Type_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
                Vehicle_Type_C.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
                Vehicle_Type_A.setEnabled(true);
                Vehicle_Type_B.setEnabled(true);
                Vehicle_Type_C.setEnabled(true);
            }
            if (DrivingRidingAns == 2){
                IfDifferentText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_A.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_B.setTextColor(getBaseContext().getResources().getColor(R.color.black));
                Vehicle_Type_A.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
                Vehicle_Type_B.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
                Vehicle_Type_A.setEnabled(true);
                Vehicle_Type_B.setEnabled(true);
            }
        }
        if (view == Vehicle_Type_A) {
            VehicleTypeRisk=1;
            VehicleTypeAns="Different: "+Vehicle_Type_A.getText().toString();

        }
        if (view == Vehicle_Type_B) {
            VehicleTypeRisk=2;
            VehicleTypeAns="Different: "+Vehicle_Type_B.getText().toString();
        }
        if (view == Vehicle_Type_C) {
            VehicleTypeRisk=3;
            VehicleTypeAns="Different: "+Vehicle_Type_C.getText().toString();
        }
        if (view == More_than_6_hours) {
            LengthOfRestText.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Home_Camp.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Field.setTextColor(getBaseContext().getResources().getColor(R.color.greyout));
            Home_Camp.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
            Field.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.greyout)));
            Home_Camp.setEnabled(false);
            Field.setEnabled(false);

            LengthofRestRisk=1;
            LengthofRestAns="More than 6 hours";
        }
        if (view == Less_than_6_hours) {
            LengthOfRestText.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Home_Camp.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Field.setTextColor(getBaseContext().getResources().getColor(R.color.black));
            Home_Camp.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Field.setButtonTintList(ColorStateList.valueOf(getBaseContext().getResources().getColor(R.color.black)));
            Home_Camp.setEnabled(true);
            Field.setEnabled(true);
        }
        if (view == Home_Camp) {
            LengthofRestRisk=2;
            LengthofRestAns=Less_than_6_hours.getText().toString()+": At "+Home_Camp.getText().toString();
        }
        if (view == Field) {
            LengthofRestRisk=3;
            LengthofRestAns=Less_than_6_hours.getText().toString()+": At "+Field.getText().toString();
        }
        if (view == Good) {
            MyHealthRisk=1;
            MyHealthAns=Good.getText().toString();
        }
        if (view == Fair_Just_recovered_from_illness_1_day) {
            MyHealthRisk=2;
            MyHealthAns=Fair_Just_recovered_from_illness_1_day.getText().toString();
        }
        if (view == Poor_Still_ill_or_on_Attend_C) {
            MyHealthRisk=4;
            MyHealthAns=Poor_Still_ill_or_on_Attend_C.getText().toString();
        }
        if (view == Still_under_medication_that_causes_drowsiness) {
            MyHealthRisk=4;
            MyHealthAns=Still_under_medication_that_causes_drowsiness.getText().toString();
        }
        if (view == Dry) {
            WeatherRisk=1;
            WeatherAns=Dry.getText().toString();
        }
        if (view == Wet) {
            WeatherRisk=2;
            WeatherAns=Wet.getText().toString();
        }
        if (view == Heavy_Showers) {
            WeatherRisk=3;
            WeatherAns=Heavy_Showers.getText().toString();
        }
        if (view == Familiar_With_Route) {
            RouteFamiliarityRisk=1;
            RouteFamiliarityAns=Familiar_With_Route.getText().toString();
        }
        if (view == Not_Familiar_With_Route) {
            RouteFamiliarityRisk=3;
            RouteFamiliarityAns=Not_Familiar_With_Route.getText().toString();
        }
        if (view == Admin) {
            PurposeofMissionRisk=1;
            PurposeofMissionAns=Admin.getText().toString();
        }
        if (view == Training_Special_Mission_Oriented_Towing_Vehicle_Gun) {
            PurposeofMissionRisk=2;
            PurposeofMissionAns=Training_Special_Mission_Oriented_Towing_Vehicle_Gun.getText().toString();
        }
        if (view == Occasional_Towing_Trailer_Vehicle_Gun) {
            PurposeofMissionRisk=3;
            PurposeofMissionAns=Occasional_Towing_Trailer_Vehicle_Gun.getText().toString();
        }
        if (!CheckBoxVehComm.isChecked()) {  //Means Vehicle Commander is Present
            VehicleCommanderPresentRisk=0;
            VehicleCommanderPresentAns="Present";
        }
        if (CheckBoxVehComm.isChecked()) { //Means Vehicle Commander is not Present
            if (DrivingRidingAns == 1){
                if (ExperienceRisk == 1) {
                    VehicleCommanderPresentRisk=1;
                    VehicleCommanderPresentAns="Not Present, Driver is CAT A, B";
                }
                if (ExperienceRisk == 2) {
                    VehicleCommanderPresentRisk=2;
                    VehicleCommanderPresentAns="Not Present, Driver is CAT C";
                }
                if (ExperienceRisk == 3) {
                    VehicleCommanderPresentRisk=3;
                    VehicleCommanderPresentAns="Not Present, Driver is CAT D";
                }
            }
            if (DrivingRidingAns == 2){
                if (ExperienceRisk == 1) {
                    VehicleCommanderPresentRisk=1;
                    VehicleCommanderPresentAns="Not Present, More than 6 mths";
                }
                if (ExperienceRisk == 2) {
                    VehicleCommanderPresentRisk=2;
                    VehicleCommanderPresentAns="Not Present, More than 3 mths";
                }
                if (ExperienceRisk == 3) {
                    VehicleCommanderPresentRisk=3;
                    VehicleCommanderPresentAns="Not Present, Less than 1 mth";
                }
            }
        }
        if (view == No_Fault) {
            VehicleMotorcycleServiceabilityRisk=1;
            VehicleMotorcycleServiceabilityAns=No_Fault.getText().toString();
        }
        if (view == Minor_Fault) {
            VehicleMotorcycleServiceabilityRisk=3;
            VehicleMotorcycleServiceabilityAns=Minor_Fault.getText().toString();
        }
        if (view == Major_Fault) {
            VehicleMotorcycleServiceabilityRisk=4;
            VehicleMotorcycleServiceabilityAns=Major_Fault.getText().toString();
        }
    }

    //Checks if any EditText is empty and Radio Buttons are unselected
    public void Incomplete() {

        if (autoTextViewCountersigningOfficerName.getText().toString().matches("")) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please Enter Countersigning Officer.", Toast.LENGTH_LONG).show();
        }
        if (!autoNames.contains(autoTextViewCountersigningOfficerName.getText().toString())) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please do not key in your own Countersigning Officer, and please select one from the drop down menu.", Toast.LENGTH_LONG).show();
        }

        if (autoTextViewVehCommName.getText().toString().matches("")) {
            if (CheckBoxVehComm.isChecked()){
                //Do nothing and do not make IncompleteFormEditText = 1
            }
            else {
                IncompleteFormEditText=1;
                Toast.makeText(getApplicationContext(), "Please Enter Vehicle Commander.", Toast.LENGTH_LONG).show();
            }
        }
        if (!autoNames.contains(autoTextViewVehCommName.getText().toString())) {
            if (CheckBoxVehComm.isChecked()){
                //Do nothing and do not make IncompleteFormEditText = 1
            }
            else {
                IncompleteFormEditText=1;
                Toast.makeText(getApplicationContext(), "Please do not key in your own Vehicle Commander, and please select one from the drop down menu.", Toast.LENGTH_LONG).show();
            }
        }

        if (autoTextViewJourneyFrom.getText().toString().matches("")) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please Enter Disembarkation Point.", Toast.LENGTH_LONG).show();
        }

        if (autoTextViewJourneyTo.getText().toString().matches("")) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please Enter Destination.", Toast.LENGTH_LONG).show();
        }

        if (autoTextViewVehicleNumber.getText().toString().matches("")) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please fill in Vehicle Number.", Toast.LENGTH_LONG).show();
        }
        if (!autoVehNumber.contains(autoTextViewVehicleNumber.getText().toString())) {
            IncompleteFormEditText=1;
            Toast.makeText(getApplicationContext(), "Please do not key in your own Vehicle Number, and please select one from the drop down menu.", Toast.LENGTH_LONG).show();
        }

        if (!autoTextViewJourneyFrom.getText().toString().matches(SelectedJourneyFrom)){
            Toast.makeText(getApplicationContext(), "Please do not key in your own Journey From, and please select one from the drop down menu.", Toast.LENGTH_LONG).show();
        }

        if (!autoTextViewJourneyTo.getText().toString().matches(SelectedJourneyTo)){
            Toast.makeText(getApplicationContext(), "Please do not key in your own Destination, and please select one from the drop down menu.", Toast.LENGTH_LONG).show();
        }

        if (DrivingRiding.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (Experience1.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (TrainedandFamiliarised.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (VehicleType.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (NewVehicleType.getCheckedRadioButtonId() == -1) {
            if (Same_as_last_detail.isChecked()) {
                //Does not check for RadioButton if Same_as_last_detail selected
            }
            else {
                IncompleteFormRadioButton=1;
            }
        }
        if (LengthofRest.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (LessThan6HoursLocation.getCheckedRadioButtonId() == -1) {
            if (More_than_6_hours.isChecked()) {
                //Does not check for RadioButton if Same_as_last_detail selected
            }
            else {
                IncompleteFormRadioButton=1;
            }
        }
        if (MyHealth.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (Weather.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (RouteFamiliarity.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (PurposeofMission.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }
        if (VehicleMotorcycleServiceability.getCheckedRadioButtonId() == -1) {
            IncompleteFormRadioButton=1;
        }

        //Activates Toast when a RadioButton is unchecked
        if (IncompleteFormRadioButton==1){
            Toast.makeText(getApplicationContext(), "Please answer all questions.", Toast.LENGTH_LONG).show();
        }
    }

    //Calculates Overall Risk Level
    public void OverallRiskLevel() {
        if (OverallRiskLevel<ExperienceRisk) {
            OverallRiskLevel=ExperienceRisk;
        }
        if (OverallRiskLevel<TrainedandFamiliarisedRisk) {
            OverallRiskLevel=TrainedandFamiliarisedRisk;
        }
        if (OverallRiskLevel<VehicleTypeRisk) {
            OverallRiskLevel=VehicleTypeRisk;
        }
        if (OverallRiskLevel<LengthofRestRisk) {
            OverallRiskLevel=LengthofRestRisk;
        }
        if (OverallRiskLevel<MyHealthRisk) {
            OverallRiskLevel=MyHealthRisk;
        }
        if (OverallRiskLevel<WeatherRisk) {
            OverallRiskLevel=WeatherRisk;
        }
        if (OverallRiskLevel<RouteFamiliarityRisk) {
            OverallRiskLevel=RouteFamiliarityRisk;
        }
        if (OverallRiskLevel<PurposeofMissionRisk) {
            OverallRiskLevel=PurposeofMissionRisk;
        }
        if (OverallRiskLevel<VehicleCommanderPresentRisk) {
            OverallRiskLevel=VehicleCommanderPresentRisk;
        }
        if (OverallRiskLevel<VehicleMotorcycleServiceabilityRisk) {
            OverallRiskLevel=VehicleMotorcycleServiceabilityRisk;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {    //Activates when anything other than EditText is touched
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null && view instanceof EditText) {
                Rect r = new Rect();
                view.getGlobalVisibleRect(r);
                int rawX = (int)ev.getRawX();
                int rawY = (int)ev.getRawY();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);    //These two lines will force the
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);                                          //keyboard to be hidden in all situations

                if (!r.contains(rawX, rawY)) {
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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
