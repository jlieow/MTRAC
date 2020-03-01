package com.example.portal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPortal extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "portal";

    //Variable Declarations
    Button Logout;
    ImageButton mtrambutton;

    String OutsystemsUsername, OutsystemsFullName, OutsystemsLoginStatus, IdPushNotification;

    //Navigation Drawer ******************************************************************************************************
    private ActionBarDrawerToggle mDrawerToggle; //toggle button
    private DrawerLayout mDrawerLayout; //one for the DrawerLayout we added to our layout
    private String mActivityTitle; //a String that we will use to update the title in the Action Bar

    private ListView DrawerList;
    private ArrayAdapter<DrawerData> DrawerAdapter;
    private ArrayList<DrawerData> DrawerArray=new ArrayList<>();

    private ActionBarDrawerToggle Toggle;
    //Navigation Drawer ******************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_portal);

        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        CheckUserDetails(); // Get User Details

        //ActionBar Settings
//        getSupportActionBar().hide();   //Hides ActionBar
        getSupportActionBar().setTitle(""); //Change title of Action Bar

        //Assigns Button to variables
        Logout=(Button)findViewById(R.id.Logout);

        //Assigns ImageButton to variables
        mtrambutton=(ImageButton)findViewById(R.id.mtrambutton);

        //Listens for button clicks in Main Activity
        mtrambutton.setOnClickListener(this);
        Logout.setOnClickListener(this);

        //Navigation Drawer **************************************************************************
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(0xff6490a2); //Changes Action Bar Color to Icon Blue
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

        DrawerList = (ListView)findViewById(R.id.DrawerList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_main_portal);

        addDrawerItems();
        setupDrawer();
        //Navigation Drawer **************************************************************************
    }

    @Override
    public void onBackPressed() {
        //Do nothing so user cannot accidentally go back into restricted portion of app
        return;
    }

    @Override
    public void onClick(View view) {
        if (view == mtrambutton){
            Intent mtram = new Intent(this, com.example.portal.mtram.class);
            startActivity(mtram);
            finish();
        }
        if (view == Logout){
            Log.i(TAG, "Logout");

            PutDataLogout();

            Intent MainLogin = new Intent(this, com.example.portal.MainLogin.class);
            startActivity(MainLogin);
            finish();
        }
    }

    //Navigation Drawer ********************************************************************************************************************
    private void addDrawerItems() {
        //Change Text and Icon of Drawer List here ("String", "image")
        DrawerArray.add(new DrawerData("Account Settings","drawer_ic_account_circle"));
//        DrawerArray.add(new DrawerData("V Comm","ic_car"));
//        DrawerArray.add(new DrawerData("Settings","ic_gear"));

        DrawerAdapter = new DrawerAdapter(this,0,DrawerArray); //Get List Adapter from Custom Drawer Adapter
        DrawerList.setAdapter(DrawerAdapter);

        DrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:  Intent case0 = new Intent(MainPortal.this, AccountSettings.class);
                        startActivity(case0);
                        finish();
                        break;

//                    case 1: Intent case1 = new Intent(MainPortal.this, Vehicle_Commander.class);
//                        startActivity(case1);
//                        finish();
//                        break;

//                    case 2: Intent case2 = new Intent(MainPortal.this, SettingPage.class);
//                        startActivity(case2);
//                        finish();
//                        break;

                    default:
                        Toast.makeText(MainPortal.this, "default choice", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupDrawer() {
        Toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(Toggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        Toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toggle.onConfigurationChanged(newConfig);
    }

    //Function to add Setting Menu on Right side of Action Bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_hamburger, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //Set drawer and drawer toggle to RIGHT
        if (id == R.id.menuRight) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);

            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Set top left action bar button to BACK
    /*@Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }*/
    //Test Navigation Drawer ********************************************************************************************************************

    public void CheckUserDetails(){
        Log.i(TAG, "Sending GET request to Apache Server");

        // Instantiate the RequestQueue.
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        String server_urlget = "https://6aelgsbab.outsystemscloud.com/aelgsbab/rest/AccountPushNotification/FindByToken?Token=" +FirebaseInstanceId.getInstance().getToken();  //Points to target which is obtained from IPV4 Address from IP Config

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
                        OutsystemsLoginStatus = json.getString("Login").trim();

                        Log.v(TAG, "Id " +IdPushNotification);
                        Log.v(TAG, OutsystemsLoginStatus);

//                        if (OutsystemsLoginStatus.equals("0")){  //Checks if LoginStatus is "false", User has logged out, go to MainLogin
//                            Logout();   //Goes to MainLogin
//                        }
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.v(TAG, "Unexpected JSONException");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {return;}
        });

        requestQueue.add(jsonArrayRequest);
    }

    public void PutDataLogout(){
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
                    parent.put("LoginUsername", OutsystemsUsername);
                    parent.put("FullName", OutsystemsFullName);
                    parent.put("PushNotificationToken", FirebaseInstanceId.getInstance().getToken().toString());
                    parent.put("Login", 0);
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

    @Override
    public void onDestroy() {   //The final call you receive before your activity is destroyed. Occurs when finish() is called.
        super.onDestroy();
        Runtime.getRuntime().gc();  //Returns the runtime object associated with the current Java application. Then runs the garbage collector. Calling this method suggests that the Java virtual machine expend effort toward recycling unused objects in order to make the memory they currently occupy available for quick reuse. When control returns from the method call, the virtual machine has made its best effort to recycle all discarded objects.
    }
}
