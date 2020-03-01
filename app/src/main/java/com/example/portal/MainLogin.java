package com.example.portal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainLogin extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button Login;
    EditText UsernameAns, LockAns;

    CheckBox Unhide;
    TextView RegisterAccount;
    String getFullName, IdPushNotification;

    Boolean TokenExist;

    int anim = 0;

    ProgressDialog progressanim;    //Set up for loading animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        //ActionBar Settings
        getSupportActionBar().hide();   //Hides ActionBar

        //Assigns Button to variables
        Login=(Button)findViewById(R.id.Login);

        //Assigns EditText to variables
        UsernameAns=(EditText)findViewById(R.id.UsernameAns);
        LockAns=(EditText)findViewById(R.id.LockAns);

        //Assigns CheckBox to variables
        Unhide=(CheckBox)findViewById(R.id.Unhide);

        //Assigns TextView to variables
        RegisterAccount=(TextView)findViewById(R.id.RegisterAccount);

        //Listens for button clicks in Main Activity
        Login.setOnClickListener(this);
        Unhide.setOnClickListener(this);
        RegisterAccount.setOnClickListener(this);

        if (CheckConnection()==false){   //Checks for Network/Wifi connection
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

            CheckAccount();  //Gets Full Name and uses it to set up the ListView

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {  //Executes after background task is completed
            Log.i(TAG, "OnPostExecute");
        }
    }

    @Override
    public void onClick(View view) {
        if (view == Login){
            if (UsernameAns.length()==0){    //If EditText is empty, creates error
                UsernameAns.setError("Body is empty");
//                UsernameAns.requestFocus();
            }
            if (LockAns.length()==0){    //If EditText is empty, creates error
                LockAns.setError("Body is empty");
//                PasswordAns.requestFocus();
            }
            if (UsernameAns.length()>0 && LockAns.length()>0) {  //Runs when all EditText are not empty
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
                anim = 1;
                //--------------------------------------------------

                if (CheckConnection()==true){   //Checks for Network/Wifi connection
                    new thread().execute();
                }
                else {
                    progressanim.dismiss(); //Removes animation by dismissing ProgressDialog

                    String Title = "Something went wrong!";
                    String Message = "No Internet Connection Detected.";
                    String ButtonText = "OK";
                    NoConnectionalertDialog(Title, Message, ButtonText);
                }
            }
        }
        if (view == Unhide){  //Action when Unhide CheckBox is clicked
            if(Unhide.isChecked()){
                Log.i(TAG, "Unhide is Checked");
                LockAns.setTransformationMethod(null);  //Unmasks Password
            }
            else{
                Log.i(TAG, "Unhide is Unchecked");
                LockAns.setTransformationMethod(new PasswordTransformationMethod());    //Masks Password
            }
        }
        if (view == RegisterAccount){  //Action when Unhide CheckBox is clicked
            Intent RegisterAccount = new Intent(this, com.example.portal.RegisterAccount.class);
            startActivity(RegisterAccount);

            finish();
        }
    }

    public void CheckAccount() {
        Log.i(TAG, "Sending POST request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountUsernamePassword/CheckPassword"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        requestQueue.stop();
                        Log.i(TAG, response);

                        if (response.matches("Login Success")){
                            CheckDataToken();
                            //AccountLogin();
                        }
                        if (response.matches("No User Exists")){
                            Toast.makeText(getApplicationContext(), "User not registered", Toast.LENGTH_LONG).show();
                            progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
                        }
                        if (response.matches("Wrong Password")){
                            Toast.makeText(getApplicationContext(), "Wrong password was entered", Toast.LENGTH_LONG).show();
                            progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
                        }

                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler

                Log.i(TAG, "Something went wrong...");
//                error.printStackTrace();
                Log.v(TAG, "Unexpected VolleyError");

                progressanim.dismiss(); //Removes animation by dismissing ProgressDialog

                String Title = "Something went wrong!";
                String Message = "No Internet Connection Detected.";
                String ButtonText = "OK";
                NoConnectionalertDialog(Title, Message, ButtonText);

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

                    parent.put("LoginUsername", UsernameAns.getText().toString());
                    parent.put("Password", LockAns.getText().toString());

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

    public void CheckDataToken(){
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

                if (response.toString().replaceAll("[\\[\\]]", "").matches("")){  //If JSONobj is null, Token does not exist
                    Log.i(TAG, "Token does not exist");
                    TokenExist = Boolean.TRUE;
                }
                else {  //Token exists
                    try {
                        Log.v(TAG, "Token exists");
                        TokenExist = Boolean.FALSE;

                        for (int i = 0; i < response.length(); ++i) {
                            JSONObject object = response.getJSONObject(i);
                            Log.v(TAG, object.toString());

                            JSONObject json = new JSONObject(object.toString());
                            IdPushNotification = json.getString("Id");
                            Log.v(TAG, IdPushNotification);
                        }
                    } catch (JSONException e) {
//                        e.printStackTrace();
                        Log.v(TAG, "Unexpected JSONException");
                    }

                }
                GetFullName();

                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void GetFullName(){
        Log.i(TAG, "Getting Full Name");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountDetails/FindByUsername?LoginUsername=" +UsernameAns.getText(); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString());

                try {
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

                if (TokenExist){    //Token does not exist
                    CreateDataToken();
                }
                else {              //Token exists
                    PutDataToken();
                }
                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void CreateDataToken(){

        Log.i(TAG, "Sending POST request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/Create"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        AccountLogin();

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

                    parent.put("LoginUsername", UsernameAns.getText().toString());
                    parent.put("Fullname", getFullName);
                    parent.put("PushNotificationToken", FirebaseInstanceId.getInstance().getToken().toString());
                    parent.put("Login", 1);
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

    public void PutDataToken(){
        StringBuilder usernameans;
        StringBuilder passwordans;

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlput = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/Update"; //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a PUT request to the provided URL
        //StringRequest specifies the request type, target URL and how to handle target responses and errors
        StringRequest stringRequestpost = new StringRequest(Request.Method.PUT, server_urlput,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        AccountLogin();

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

                    parent.put("Id", IdPushNotification);
                    parent.put("LoginUsername", UsernameAns.getText().toString());
                    parent.put("FullName", getFullName);
                    parent.put("PushNotificationToken", FirebaseInstanceId.getInstance().getToken().toString());
                    parent.put("Login", 1);
                    parent.put("IsProtected", 0);

//                    Log.i(TAG, parent.toString(2));

//                    Iterator keys = parent.keys();  //Returns an iterator of the String names in this object.
//                    while(keys.hasNext())
//                        parent.remove(keys.next().toString());  //Removes the named mapping if it exists; does nothing otherwise.

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

    public void AccountLogin() {
        Log.i(TAG, "Full Name is " +getFullName);

        Intent mtram = new Intent(this, com.example.portal.mtram.class);
        startActivity(mtram);

//        Intent MainPortal = new Intent(this, com.example.portal.MainPortal.class);
//        startActivity(MainPortal);

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
                show.dismiss();
            }
        });
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

    @Override
    public void onBackPressed() {
        //Do nothing so user cannot accidentally go back into restricted portion of app

        if (anim == 1){
            progressanim.dismiss(); //Removes animation by dismissing ProgressDialog
        }

        return;
    }

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.

        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
