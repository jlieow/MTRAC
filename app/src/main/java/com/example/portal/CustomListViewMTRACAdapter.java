package com.example.portal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CS on 29-May-17.
 * Custom Adapter for ListView used in Search Button
 */


public class CustomListViewMTRACAdapter extends ArrayAdapter<CustomListViewMTRAC>{
    private Context context;
    private List<CustomListViewMTRAC> MTRAMform;
    int OverallRiskLevel;

    //Contstructor, call on creation
    public CustomListViewMTRACAdapter(Context context, int resource, ArrayList<CustomListViewMTRAC> objects){
        super(context,resource,objects);
        this.context = context;
        this.MTRAMform = objects;
    }

    //called when rendering the list
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListViewMTRAC getForm = MTRAMform.get(position); //create object out of CustomDriverRisk

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_item1, null);

        //Setting up TextView for form ID
        TextView IDform = (TextView)view.findViewById(R.id.IDform);
        IDform.setText(getForm.getIDform());
        IDform.setVisibility(View.GONE);

        //Setting up TextView for Driver Name
        TextView Driver = (TextView)view.findViewById(R.id.SubmitMTRAC);
        Driver.setText(getForm.getDriver());

        //Setting up TextView for Vehicle Number
        TextView VehicleNumber = (TextView)view.findViewById(R.id.VehicleNumber);
        VehicleNumber.setText(getForm.getVehicleNumber());

        //Settting up TextView for Risk Level
        TextView RiskLevelLabel=(TextView)view.findViewById(R.id.RiskLevelLabel);
        TextView RiskLevel = (TextView)view.findViewById(R.id.RiskLevel);

        OverallRiskLevel = Integer.valueOf(getForm.getRiskLevel());
        if (OverallRiskLevel == 0 || OverallRiskLevel == 1) {
            RiskLevel.setText("LOW");
            RiskLevelLabel.setBackgroundColor(getContext().getResources().getColor(R.color.palegreen));
            RiskLevel.setBackgroundColor(getContext().getResources().getColor(R.color.palegreen));
        }
        if (OverallRiskLevel == 2) {
            RiskLevel.setText("MEDIUM");
            RiskLevelLabel.setBackgroundColor(getContext().getResources().getColor(R.color.palegold));
            RiskLevel.setBackgroundColor(getContext().getResources().getColor(R.color.palegold));
        }
        if (OverallRiskLevel == 3) {
            RiskLevel.setText("HIGH");
            RiskLevelLabel.setBackgroundColor(getContext().getResources().getColor(R.color.palered));
            RiskLevel.setBackgroundColor(getContext().getResources().getColor(R.color.palered));
        }
        if (OverallRiskLevel == 4) {
            RiskLevel.setText("NO MOVE");
            RiskLevelLabel.setTextColor(Color.WHITE);
            RiskLevel.setTextColor(Color.WHITE);
            RiskLevelLabel.setBackgroundColor(Color.BLACK);
            RiskLevel.setBackgroundColor(Color.BLACK);
        }

        //Setting up TextView for FormStatus
        TextView FormStatus = (TextView)view.findViewById(R.id.FormStatus);

        if (getForm.getVisible()==1){
            FormStatus.setVisibility(View.VISIBLE);
        }
        else {
            FormStatus.setVisibility(View.GONE);
        }

        if (getForm.getFormStatus()==0){
            FormStatus.setText(" " +getForm.getNumberForms()+ ". Your MT-RAC has been rejected.");
            FormStatus.setBackgroundColor(getContext().getResources().getColor(R.color.greybackground));
        }
        if (getForm.getFormStatus()==1){
            FormStatus.setText(" " +getForm.getNumberForms()+ ". Awaiting Approval by Countersigning Officer.");
            FormStatus.setBackgroundColor(getContext().getResources().getColor(R.color.palered));
        }
        if (getForm.getFormStatus()==2){
            FormStatus.setText(" " +getForm.getNumberForms()+ ". Awaiting Approval by Vehicle Commander.");
            FormStatus.setBackgroundColor(getContext().getResources().getColor(R.color.palegold));
        }
        if (getForm.getFormStatus()==3){
            FormStatus.setText(" " +getForm.getNumberForms()+ ". MTRAC has been Approved.");
            FormStatus.setBackgroundColor(getContext().getResources().getColor(R.color.palegreen));
        }

        if (getForm.getFormStatus()>3){ //If Completion is not within the scope use this.
            FormStatus.setText(" " +getForm.getNumberForms()+ ".");
            FormStatus.setBackgroundColor(getContext().getResources().getColor(R.color.greybackground));
        }



        return view;
    }
}
