package com.labs.nipamo.letseat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Set up local variables
        mMap = googleMap;
        double latitude;
        double longitude;

        // Enabling MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Use FindLocation to get the latitude and longitude
        ((FindLocation) getApplicationContext()).setLocation();
        latitude = ((FindLocation) getApplicationContext()).getLatitude();
        longitude = ((FindLocation) getApplicationContext()).getLongitude();

        // Add a marker in to the user's current location and move the camera
        myPosition = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(myPosition).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f), 3000, null);
    }
}
