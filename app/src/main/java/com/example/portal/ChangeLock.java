package com.example.portal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import java.util.regex.Pattern;

public class ChangeLock extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

//    EditText CurrentPassword, NewPassword, RetypePassword;
    CheckBox Show;
    Button Submit;

    String getLoginUsername, getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setTitle(""); //Change title of Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Set whether home should be displayed as an "up" affordance.

        //Assigns Button to variables
        Submit=(Button)findViewById(R.id.Submit);

        //Assigns EditText to variables
//        CurrentPassword=(EditText)findViewById(R.id.CurrentPassword);
//        NewPassword=(EditText)findViewById(R.id.NewPassword);
//        RetypePassword=(EditText)findViewById(R.id.RetypePassword);

        //Assigns CheckBox to variables
        Show=(CheckBox)findViewById(R.id.Show);

        //Listens for button clicks in Main Activity
        Show.setOnClickListener(this);
        Submit.setOnClickListener(this);

        //Get User's Full Name
        getLoginUsername();
    }

    @Override
    public void onClick(View view) {
        if (view == Show){
            if(Show.isChecked()){
                Log.i(TAG, "ShowPassword is Checked");
                ((EditText)findViewById(R.id.CurrentPassword)).setTransformationMethod(null);  //Unmasks Password
                ((EditText)findViewById(R.id.NewPassword)).setTransformationMethod(null);  //Unmasks Password
                ((EditText)findViewById(R.id.RetypePassword)).setTransformationMethod(null);  //Unmasks Password
            }
            else{
                Log.i(TAG, "ShowPassword is Unchecked");
                ((EditText)findViewById(R.id.CurrentPassword)).setTransformationMethod(new PasswordTransformationMethod());    //Masks Password
                ((EditText)findViewById(R.id.NewPassword)).setTransformationMethod(new PasswordTransformationMethod());    //Masks Password
                ((EditText)findViewById(R.id.RetypePassword)).setTransformationMethod(new PasswordTransformationMethod());    //Masks Password
            }
        }
        if (view == Submit){
            int Check = 0;

            final Pattern hasWhiteSpace = Pattern.compile("\\s"); //Checks for whitespaces
            final Pattern hasUppercase = Pattern.compile("[A-Z]");  //Checks for Uppercase
            final Pattern hasLowercase = Pattern.compile("[a-z]");  //Checks for Lowercase
            final Pattern hasNumber = Pattern.compile("\\d");   //Checks for Number
            final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]"); //Checks for Special Characters

            if (((EditText)findViewById(R.id.CurrentPassword)).length()==0){    //If EditText is empty, creates error
                Check = 1;
                ((EditText)findViewById(R.id.CurrentPassword)).setError("Body is empty");
            }
            if (((EditText)findViewById(R.id.NewPassword)).length()==0){    //If EditText is empty, creates error
                Check = 1;
                ((EditText)findViewById(R.id.NewPassword)).setError("Body is empty");
            }

            if (hasWhiteSpace.matcher(((EditText)findViewById(R.id.NewPassword)).getText().toString()).find()){    //If EditText has whitespace, creates error
                Check = 1;
                ((EditText)findViewById(R.id.NewPassword)).setError("Username should not have white space");
//                Username.requestFocus();
            }
            else {
                if (!hasUppercase.matcher(((EditText)findViewById(R.id.NewPassword)).getText()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.NewPassword)).setError("Password needs an uppercase.");
                }

                if (!hasLowercase.matcher(((EditText)findViewById(R.id.NewPassword)).getText()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.NewPassword)).setError("Password needs an lowercase.");
                }

                if (!hasNumber.matcher(((EditText)findViewById(R.id.NewPassword)).getText()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.NewPassword)).setError("Password needs an number.");
                }

                if (!hasSpecialChar.matcher(((EditText)findViewById(R.id.NewPassword)).getText()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.NewPassword)).setError("Password needs a special character i.e. !,@,#, etc..");
                }
                if (((EditText)findViewById(R.id.NewPassword)).length()<6){
                    Check = 1;
                    ((EditText)findViewById(R.id.NewPassword)).setError("Password needs to be at least 6 characters long.");
//                Password.requestFocus();
                }
            }
            if (((EditText)findViewById(R.id.NewPassword)).length()==0){     //If EditText is empty, creates error
                Check = 1;
                ((EditText)findViewById(R.id.NewPassword)).setError("Body is empty");
            }
            if (!((EditText)findViewById(R.id.NewPassword)).getText().toString().matches(((EditText)findViewById(R.id.RetypePassword)).getText().toString())){
                Check = 1;
                ((EditText)findViewById(R.id.NewPassword)).setError("Passwords do not match");
                ((EditText)findViewById(R.id.NewPassword)).setError("Passwords do not match");
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            }

            if (Check == 0){
                if (((EditText)findViewById(R.id.CurrentPassword)).length()>0 && ((EditText)findViewById(R.id.NewPassword)).length()>0 && ((EditText)findViewById(R.id.RetypePassword)).length()>0){
                    getPassword();
                }
            }
        }
    }

    public void getLoginUsername(){
        Log.i(TAG, "Checking Token");

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
                        getLoginUsername = json.getString("LoginUsername");
                        Log.v(TAG, getLoginUsername);
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

    public void getPassword(){
        Log.i(TAG, "Checking Token");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountUsernamePassword/FindByUsername?LoginUsername=" + getLoginUsername; //Points to target which is obtained from IPV4 Address from IP Config

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
                        getID = json.getString("Id");
                        Log.v(TAG, getLoginUsername);

                        if (((EditText)findViewById(R.id.CurrentPassword)).getText().toString().matches(json.getString("Password"))){
                            Toast.makeText(getApplicationContext(), "Changing Passwords", Toast.LENGTH_LONG).show();
                            putsPasswordOutsystems();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
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

    public void putsPasswordOutsystems() {
        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlput = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountUsernamePassword/Update"; //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a PUT request to the provided URL
        //StringRequest specifies the request type, target URL and how to handle target responses and errors
        StringRequest stringRequestpost = new StringRequest(Request.Method.PUT, server_urlput,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        alertDialog("Success", "Password has been changed.", "OK");

                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);

                error.printStackTrace();
                alertDialog("Failure", "Password was not changed.", "OK");

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

                    parent.put("Id", getID);
                    parent.put("LoginUsername", getLoginUsername);
                    parent.put("Password", ((EditText)findViewById(R.id.NewPassword)).getText().toString());
                    parent.put("IsProtected", 0);

//                    Log.i(TAG, android.text.Html.escapeHtml(parent.toString(2).trim()).toString());

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

    //Sets up the "up" affordance.
    @Override
    public boolean onSupportNavigateUp(){
        Intent Back = new Intent(ChangeLock.this, AccountSettings.class);
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
