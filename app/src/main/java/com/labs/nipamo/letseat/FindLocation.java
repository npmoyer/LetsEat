package com.labs.nipamo.letseat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class FindLocation extends Application{
    public boolean useCurrent = false;
    public boolean useCustom = false;

    private int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 100;

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
}
