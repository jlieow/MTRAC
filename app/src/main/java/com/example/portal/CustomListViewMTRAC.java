package com.example.portal;

/**
 * Created by CS on 29-May-17.
 * Custom Adapter "data base" to replace "String" in ArrayAdapter<String>
 */

public class CustomListViewMTRAC {

    int NumberForms, Visible, FormStatus;
    String IDform, Driver, VehicleNumber, RiskLevel;

    //Possible to add on more values
    //String Veh_com, String date etc...

    public CustomListViewMTRAC(String IDform, String Driver, String VehicleNumber, String RiskLevel, int NumberForms, int Visible, int FormStatus)
    {
        this.IDform=IDform;
        this.Driver=Driver;
        this.VehicleNumber=VehicleNumber;
        this.RiskLevel=RiskLevel;
        this.NumberForms=NumberForms;
        this.Visible=Visible;
        this.FormStatus=FormStatus;
    }

    public String getIDform(){ return IDform; }
    public String getDriver(){ return Driver; }
    public String getVehicleNumber(){ return VehicleNumber; }
    public String getRiskLevel(){ return RiskLevel; }

    public int getNumberForms(){ return NumberForms; }
    public int getVisible(){ return Visible; }
    public int getFormStatus(){ return FormStatus; }

}
