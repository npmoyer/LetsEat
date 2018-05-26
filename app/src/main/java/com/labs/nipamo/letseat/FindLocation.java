package com.labs.nipamo.letseat;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import static com.labs.nipamo.letseat.Constants.Permissions.MY_PERMISSIONS_ACCESS_FINE_LOCATION;
import static com.labs.nipamo.letseat.Constants.Settings.CURRENTSAVE;
import static com.labs.nipamo.letseat.Constants.Settings.CUSTOMSAVE;
import static com.labs.nipamo.letseat.Constants.Settings.PREFERENCES;

public class FindLocation extends Application{

    private double latitude;
    private double longitude;
    private String zip;

    SharedPreferences sharedPreferences;

    public boolean checkForPermission(Context context){
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean promptForPermission(Activity activity){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        return true;
    }

    public void setLocation() {
        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        boolean customSave, currentSave;
        customSave = sharedPreferences.getBoolean(CUSTOMSAVE, false);
        currentSave = sharedPreferences.getBoolean(CURRENTSAVE, true);


        // Check if the user selected current or custom location
        if (currentSave) {
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Set latitude of the current location
                this.latitude = location.getLatitude();
                // Set longitude of the current location
                this.longitude = location.getLongitude();
            }
            }else if(customSave) {
            try {
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = geocoder.getFromLocationName(zip, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    // Set latitude of zip code location
                    this.latitude = address.getLatitude();
                    // Set longitude of zip code location
                    this.longitude = address.getLongitude();
                    Toast.makeText(this, zip, Toast.LENGTH_LONG).show();
                } else {
                    // Display appropriate message when Geocoder services are not available
                    Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error parsing zipcode", Toast.LENGTH_LONG).show();
            }
        }
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
