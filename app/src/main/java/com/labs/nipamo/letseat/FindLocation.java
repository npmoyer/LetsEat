package com.labs.nipamo.letseat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class FindLocation extends Application{
    public boolean useCurrent = false;
    public boolean useCustom = false;

    private int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 100;
    private double latitude;
    private double longitude;

    public boolean checkForPermission(Context context){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            return false;
        }
    }

    public boolean promptForPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        return true;
    }

    public void setLocation() {
        // Check if the user selected current or custom location
        if (getCurrent()) {
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Getting latitude of the current location
                this.latitude = location.getLatitude();

                // Getting longitude of the current location
                this.longitude = location.getLongitude();
            }else if(getCustom()){

            }
        }
    }

    public void setCurrent(boolean x){
        this.useCurrent = x;
    }

    public void setCustom(boolean x){
        this.useCustom = x;
    }

    public boolean getCurrent(){
        return this.useCurrent;
    }

    public boolean getCustom(){
        return this.useCustom;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
}
