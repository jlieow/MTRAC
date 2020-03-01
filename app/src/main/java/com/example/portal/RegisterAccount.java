package com.example.portal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterAccount extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button Register;
    EditText LoginUsername, FullName, Email, PasswordMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        getSupportActionBar().setTitle(""); //Change title of Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Set whether home should be displayed as an "up" affordance.

        //Assigns EditText to variables
        LoginUsername=(EditText)findViewById(R.id.LoginUsername);
        FullName=(EditText)findViewById(R.id.FullName);
        Email=(EditText)findViewById(R.id.Email);
//        Password=(EditText)findViewById(R.id.EditPassword);
        PasswordMatch=(EditText)findViewById(R.id.PasswordMatch);

        //Assigns Button to variables
        Register=(Button)findViewById(R.id.Register);

        //Listens for button clicks in Main Activity
        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == Register){  //Action when ShowPassword CheckBox is clicked
            int Check = 0;
            final Pattern hasWhiteSpace = Pattern.compile("\\s"); //Checks for whitespaces
            final Pattern hasUppercase = Pattern.compile("[A-Z]");  //Checks for Uppercase
            final Pattern hasLowercase = Pattern.compile("[a-z]");  //Checks for Lowercase
            final Pattern hasNumber = Pattern.compile("\\d");   //Checks for Number
            final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]"); //Checks for Special Characters

            if (LoginUsername.length()==0){    //If EditText is empty, creates error
                Check = 1;
                LoginUsername.setError("Body is empty");
//                Username.requestFocus();
            }

            if (hasWhiteSpace.matcher(LoginUsername.getText().toString()).find()){    //If EditText has whitespace, creates error
                Check = 1;
                LoginUsername.setError("Username should not have white space");
//                Username.requestFocus();
            }

            if (FullName.length()==0){    //If EditText is empty, creates error
                Check = 1;
                FullName.setError("Body is empty");
//                Username.requestFocus();
            }

            if (Email.length()==0){     //If EditText is empty, creates error
                Check = 1;
                Email.setError("Body is empty");
//                Email.requestFocus();
            }

            if (((EditText)findViewById(R.id.EditPassword)).length()==0){     //If EditText is empty, creates error
                Check = 1;
                ((EditText)findViewById(R.id.EditPassword)).setError("Body is empty");
//                Password.requestFocus();
            }

            if (hasWhiteSpace.matcher(((EditText)findViewById(R.id.EditPassword)).getText().toString()).find()){    //If EditText has whitespace, creates error
                Check = 1;
                LoginUsername.setError("Password should not have white space");
//                Username.requestFocus();
            }
            else {
                if (!hasUppercase.matcher(((EditText)findViewById(R.id.EditPassword)).getText().toString()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.EditPassword)).setError("Password needs an uppercase.");
                }

                if (!hasLowercase.matcher(((EditText)findViewById(R.id.EditPassword)).getText().toString()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.EditPassword)).setError("Password needs an lowercase.");
                }

                if (!hasNumber.matcher(((EditText)findViewById(R.id.EditPassword)).getText().toString()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.EditPassword)).setError("Password needs an number.");
                }

                if (!hasSpecialChar.matcher(((EditText)findViewById(R.id.EditPassword)).getText().toString()).find()) {
                    Check = 1;
                    ((EditText)findViewById(R.id.EditPassword)).setError("Password needs a special character i.e. !,@,#, etc..");
                }
                if (((EditText)findViewById(R.id.EditPassword)).getText().toString().trim().length()<6){
                    Check = 1;
                    ((EditText)findViewById(R.id.EditPassword)).setError("Password needs to be at least 6 characters long.");
//                Password.requestFocus();
                }
            }

            if (PasswordMatch.length()==0){     //If EditText is empty, creates error
                Check = 1;
                PasswordMatch.setError("Body is empty");
//                Password.requestFocus();
            }

            if (Check == 0){  //Runs when all Check == 0 which means no issues found
                if (((EditText)findViewById(R.id.EditPassword)).getText().toString().trim().matches(PasswordMatch.getText().toString().trim())){  //Checks if both password matches
                    CheckAccountExistence();
                }
                else {
                    ((EditText)findViewById(R.id.EditPassword)).setError("Passwords do not match");
                    PasswordMatch.setError("Passwords do not match");
                }
//                Intent Login = new Intent(this, com.example.portal.MainLogin.class);
//                startActivity(Login);
            }
        }
    }

    public void CheckAccountExistence() {   //Check if Username has been selected

        Log.i(TAG, "Sending GET request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountDetails/FindByUsername?LoginUsername=" +LoginUsername.getText(); //Points to target which is obtained from IPV4 Address from IP Config

        //Submits a GET request to the provided URL
        //jsonArrayRequest specifies the request type, target URL and how to handle target responses and errors
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, server_urlget, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, response.toString().replaceAll("[\\[\\]]", ""));

                if (response.toString().replaceAll("[\\[\\]]", "").matches("")){  //If CheckUsername is null, Username is unused
                    Log.i(TAG, "Username is unused");
                    RegisterInOutsystems1(); //Creates and stores new account details in Outsystems
                    Toast.makeText(getApplicationContext(), "Account is successfully created.", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.i(TAG, "Username is used");
                    Toast.makeText(getApplicationContext(), "Username is already used. Please select a new username.", Toast.LENGTH_LONG).show();
                }

                requestQueue.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void RegisterInOutsystems1() {    //Creates and stores new account details in Outsystems

        Log.i(TAG, "Sending POST request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountDetails/Create"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        RegisterInOutsystems2(); //Creates and stores new account details in Outsystems

                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
//                Log.i(TAG, "Something went wrong..." +error);

                alertDialog("Failure", "Something went wrong. Account was not registered.", "OK");

//                error.printStackTrace();
//                Log.v(TAG, "Unexpected JSONException: " +error.toString());
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

                    parent.put("LoginUsername", LoginUsername.getText().toString().trim());
                    parent.put("FullName", FullName.getText().toString().trim());
                    parent.put("Email", Email.getText().toString().trim());
                    parent.put("IsProtected", 0);

//                    Log.i(TAG, parent.toString(2));

                    String your_string_json = parent.toString(); // put your json
                    return your_string_json.getBytes();
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException: " +e.toString());
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

    public void RegisterInOutsystems2() {    //Creates and stores new account details in Outsystems

        Log.i(TAG, "Sending POST request to Outsystems Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlpost = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountUsernamePassword/Create"; //Points to target which is obtained from IPV4 Address from IP Config

        StringRequest stringRequestpost = new StringRequest(Request.Method.POST, server_urlpost,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {   //Server Response Handler
                        //PostResponse.setText(response);

                        alertDialog("Success", "Account is registered successfully.", "OK");

                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {    //On Error Response Handler
                Log.i(TAG, "Something went wrong..." +error);

                alertDialog("Failure", "Something went wrong. Account was not registered.", "OK");

//                error.printStackTrace();
                Log.v(TAG, "Unexpected JSONException: " +error.toString());
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

                    parent.put("LoginUsername", LoginUsername.getText().toString().trim());
                    parent.put("Password", ((EditText)findViewById(R.id.EditPassword)).getText().toString().trim());
                    parent.put("IsProtected", 0);

//                    Log.i(TAG, parent.toString(2));

                    String your_string_json = parent.toString(); // put your json
                    return your_string_json.getBytes();
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException: " +e.toString());
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
                Intent MainLogin = new Intent(getBaseContext(), com.example.portal.MainLogin.class);
                startActivity(MainLogin);

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
        Intent Back = new Intent(this, com.example.portal.MainLogin.class);
        startActivity(Back);

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent Back = new Intent(this, com.example.portal.MainLogin.class);
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
