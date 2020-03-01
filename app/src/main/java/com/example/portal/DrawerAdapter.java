package com.example.portal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CS on 02-Jun-17.
 * Custom Adapter for ListView in Drawer
 */

public class DrawerAdapter extends ArrayAdapter<DrawerData> {
    private Context drawerContext;
    private List<DrawerData> drawerString;
    int OverallRiskLevel;

    //Contstructor, call on creation
    public DrawerAdapter(Context drawerContext, int resource, ArrayList<DrawerData> objects){
        super(drawerContext,resource,objects);
        this.drawerContext = drawerContext;
        this.drawerString = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerData drawerData = drawerString.get(position); //create object out of CustomDriverRisk

        LayoutInflater inflater = (LayoutInflater) drawerContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.drawer_design, null);

        //Setting Image to drawer_design.xml
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        int imageID = drawerContext.getResources().getIdentifier(drawerData.getImage(), "drawable", drawerContext.getPackageName());
        image.setImageResource(imageID);

        //Setting up TextView for Driver Name
        TextView ShowDrawerString = (TextView) view.findViewById(R.id.drawer_textview1);
        ShowDrawerString.setText(drawerData.getDrawerStringData());

        return view;
    };

}
