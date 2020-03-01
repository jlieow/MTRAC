package com.example.portal;

/**
 * Created by CS on 02-Jun-17.
 * Custom Data base for listview in Drawer - Passing values to DrawerAdapter.java
 */

public class DrawerData {

    String DrawerStringData;
    String image;

    public DrawerData(String DrawerStringData, String image){
        this.DrawerStringData=DrawerStringData;
        this.image=image;

    }

    public String getDrawerStringData(){ return DrawerStringData; }

    public String getImage(){ return image; }
}
