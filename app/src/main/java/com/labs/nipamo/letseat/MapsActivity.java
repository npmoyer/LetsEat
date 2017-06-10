package com.labs.nipamo.letseat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.labs.nipamo.letseat.FindPlacesConfig.APIKEY;
import static com.labs.nipamo.letseat.FindPlacesConfig.GEOMETRY;
import static com.labs.nipamo.letseat.FindPlacesConfig.LATITUDE;
import static com.labs.nipamo.letseat.FindPlacesConfig.LOCATION;
import static com.labs.nipamo.letseat.FindPlacesConfig.LONGITUDE;
import static com.labs.nipamo.letseat.FindPlacesConfig.NAME;
import static com.labs.nipamo.letseat.FindPlacesConfig.OK;
import static com.labs.nipamo.letseat.FindPlacesConfig.STATUS;
import static com.labs.nipamo.letseat.FindPlacesConfig.ZERO_RESULTS;
import static com.labs.nipamo.letseat.R.id.map;
import static com.labs.nipamo.letseat.SettingsActivity.PREFERENCES;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng myPosition;
    private String url;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
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

        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        // Check if the user selected a location setting
        if (sharedPreferences != null){
            // Use FindLocation to get the latitude and longitude
            ((FindLocation) getApplicationContext()).setLocation();
            latitude = ((FindLocation) getApplicationContext()).getLatitude();
            longitude = ((FindLocation) getApplicationContext()).getLongitude();

            // Create the url and execute the async task
            loadNearbyPlaces(latitude, longitude);
            JSONTaskMaps json = new JSONTaskMaps();
            json.execute();

            // Add a marker in to the user's current location and move the camera
            myPosition = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(myPosition).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f), 5000, null);

            // Draw a circle on the map
            int radius = ((FindPlacesConfig) getApplicationContext()).getDistance();
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(myPosition)
                    .radius(radius)
                    .strokeColor(Color.RED));
        } else{
            // Go to the settings activity so the user can enter location setting
            Intent intent = new Intent (this, SettingsActivity.class);
            startActivity(intent);
            Toast toast = Toast.makeText(MapsActivity.this,
                    "Error: Location settings are not configured", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void loadNearbyPlaces(double latitude, double longitude) {
        // Set up local variables
        String type = "restaurant";
        int distance;
        String category, rating, price;

        distance = ((FindPlacesConfig) getApplicationContext()).getDistance();
        category = ((FindPlacesConfig) getApplicationContext()).getCategory();
        rating = ((FindPlacesConfig) getApplicationContext()).getRating();
        price = ((FindPlacesConfig) getApplicationContext()).getPrice();

        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(distance);
        googlePlacesUrl.append("&types=").append(type);
        if (category != null){
            googlePlacesUrl.append("&keyword=").append(category);
        }
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + APIKEY);

        this.url = googlePlacesUrl.toString();
    }

    /* Async task for getting the JSON result */
    private class JSONTaskMaps extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String googlePlacesUrl = MapsActivity.this.url;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    googlePlacesUrl, (String) null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject result) {
                            parseLocationResult(result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), "Error: No response",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            FindPlaces.getInstance().addToRequestQueue(request);
            return "Executed";
        }

        /* Get the place name, location, rating and price */
        private void parseLocationResult(JSONObject result) {
            String placeName = null;
            double latitude, longitude;

            try {
                JSONArray jsonArray = result.getJSONArray("results");

                if (result.getString(STATUS).equalsIgnoreCase(OK)) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject place = jsonArray.getJSONObject(i);
                        if (!place.isNull(NAME)) {
                            placeName = place.getString(NAME);
                        }
                        latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                                .getDouble(LATITUDE);
                        longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                                .getDouble(LONGITUDE);

                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(latitude, longitude);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName);
                        mMap.addMarker(markerOptions);
                    }

                    Toast.makeText(getBaseContext(), jsonArray.length() + " Restaurants found!",
                            Toast.LENGTH_LONG).show();
                } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                    Toast.makeText(getBaseContext(), "Error: No matching restaurants found",
                            Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Error: Cannot parse response",
                            Toast.LENGTH_LONG).show();
            }
        }
    }
}