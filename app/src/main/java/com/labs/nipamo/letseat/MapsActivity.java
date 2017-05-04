package com.labs.nipamo.letseat;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.labs.nipamo.letseat.FindPlacesConfig.APIKEY;
import static com.labs.nipamo.letseat.FindPlacesConfig.GEOMETRY;
import static com.labs.nipamo.letseat.FindPlacesConfig.ICON;
import static com.labs.nipamo.letseat.FindPlacesConfig.LATITUDE;
import static com.labs.nipamo.letseat.FindPlacesConfig.LOCATION;
import static com.labs.nipamo.letseat.FindPlacesConfig.LONGITUDE;
import static com.labs.nipamo.letseat.FindPlacesConfig.NAME;
import static com.labs.nipamo.letseat.FindPlacesConfig.OK;
import static com.labs.nipamo.letseat.FindPlacesConfig.PLACE_ID;
import static com.labs.nipamo.letseat.FindPlacesConfig.PROXIMITY_RADIUS;
import static com.labs.nipamo.letseat.FindPlacesConfig.REFERENCE;
import static com.labs.nipamo.letseat.FindPlacesConfig.STATUS;
import static com.labs.nipamo.letseat.FindPlacesConfig.SUPERMARKET_ID;
import static com.labs.nipamo.letseat.FindPlacesConfig.TAG;
import static com.labs.nipamo.letseat.FindPlacesConfig.VICINITY;
import static com.labs.nipamo.letseat.FindPlacesConfig.ZERO_RESULTS;

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

        loadNearbyPlaces(latitude, longitude);
    }

    private void loadNearbyPlaces(double latitude, double longitude){
        String type = "restaurant";

        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(PROXIMITY_RADIUS);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + APIKEY);

        JsonObjectRequest request = new JsonObjectRequest(googlePlacesUrl.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {

                        Log.i(TAG, "onResponse: Result= " + result.toString());
                        parseLocationResult(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: Error= " + error);
                        Log.e(TAG, "onErrorResponse: Error= " + error.getMessage());
                    }
                });

        FindPlaces.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String id, place_id, placeName = null, reference, icon, vicinity = null;
        double latitude, longitude;

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {

                mMap.clear();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject place = jsonArray.getJSONObject(i);

                    id = place.getString(SUPERMARKET_ID);
                    place_id = place.getString(PLACE_ID);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                    }
                    if (!place.isNull(VICINITY)) {
                        vicinity = place.getString(VICINITY);
                    }
                    latitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LATITUDE);
                    longitude = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                            .getDouble(LONGITUDE);
                    reference = place.getString(REFERENCE);
                    icon = place.getString(ICON);

                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerOptions.position(latLng);
                    markerOptions.title(placeName + " : " + vicinity);

                    mMap.addMarker(markerOptions);
                }

                Toast.makeText(getBaseContext(), jsonArray.length() + " Restaurants found!",
                        Toast.LENGTH_LONG).show();
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(getBaseContext(), "No Restaurants found in 5KM radius!!!",
                        Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
            Log.e(TAG, "parseLocationResult: Error=" + e.getMessage());
        }
    }
}
