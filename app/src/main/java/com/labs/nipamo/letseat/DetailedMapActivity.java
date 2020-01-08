package com.labs.nipamo.letseat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.labs.nipamo.letseat.Constants.Permissions.REQUEST_COARSE_LOCATION;
import static com.labs.nipamo.letseat.Constants.Permissions.REQUEST_FINE_LOCATION;

public class DetailedMapActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_map);
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
        GoogleMap mMap = googleMap;
        LatLng placePosition;
        LatLng myPosition;

        // Enabling MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION);

        } else {
            mMap.setMyLocationEnabled(true);
        }

        // Use FindLocation to get the latitude and longitude
        ((FindLocation) getApplicationContext()).setLocation();
        myPosition = new LatLng(((FindLocation) getApplicationContext()).getLatitude(),
                ((FindLocation) getApplicationContext()).getLongitude());

        // Add a marker in to the place location and move the camera
        placePosition = new LatLng(((FindPlaces) getApplicationContext()).getLatNum(),
                ((FindPlaces) getApplicationContext()).getLngNum());
        googleMap.addMarker(new MarkerOptions().position(placePosition).title(((FindPlaces)
                getApplicationContext()).getNameText()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placePosition, 14));

        // Draw a circle on the map
        int radius = ((FindPlacesConfig) getApplicationContext()).getDistance();
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(myPosition)
                .radius(radius)
                .strokeColor(Color.RED));
    }
}
