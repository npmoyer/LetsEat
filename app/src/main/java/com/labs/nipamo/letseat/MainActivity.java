package com.labs.nipamo.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.labs.nipamo.letseat.FindPlacesConfig.*;

public class MainActivity extends AppCompatActivity {

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /* Called when the user taps a button in the menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Called when the user taps the "Show Map" button */
    public void viewMap(View view){
        // Check if the user selected a location setting
       if (((FindLocation) getApplicationContext()).getCurrent() ||
               ((FindLocation) getApplicationContext()).getCustom()){
            // Start the Maps Activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else{
           Toast toast = Toast.makeText(MainActivity.this,
                   "Configure location settings first", Toast.LENGTH_LONG);
           toast.show();
       }
    }

    /* Called when the user taps the "Choose Options" button */
    public void chooseOptions(View view){
        Intent intent = new Intent(this, OptionsActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the "Let's Eat" button */
    public void letsEat(View view){
        TextView result = (TextView) findViewById(R.id.result);
        Button details = (Button) findViewById(R.id.details_button);
        Button onMap =  (Button) findViewById(R.id.show_on_map_button);
        Button directions = (Button) findViewById(R.id.directions_button);
        double latitude;
        double longitude;

        // Check if the user selected a location setting
        if (((FindLocation) getApplicationContext()).getCurrent() ||
                ((FindLocation) getApplicationContext()).getCustom()){
            // Use FindLocation to get the latitude and longitude
            ((FindLocation) getApplicationContext()).setLocation();
            latitude = ((FindLocation) getApplicationContext()).getLatitude();
            longitude = ((FindLocation) getApplicationContext()).getLongitude();
            loadNearbyPlaces(latitude, longitude);

            // Display the result
            if (this.name != null){
                result.setText(this.name);
            }
            result.setVisibility(View.VISIBLE);
            details.setVisibility(View.VISIBLE);
            onMap.setVisibility(View.VISIBLE);
            directions.setVisibility(View.VISIBLE);
        } else{
            Toast toast = Toast.makeText(MainActivity.this,
                    "Configure location settings first", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /* Called when the user taps on a button that is not implemented yet */
    public void notImplemented(View view){
        Toast toast = Toast.makeText(MainActivity.this,
                "Sorry, this button doesn't work yet!", Toast.LENGTH_LONG);
        toast.show();
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                googlePlacesUrl.toString(), (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                       parseLocationResult(result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        FindPlaces.getInstance().addToRequestQueue(request);
    }

    private void parseLocationResult(JSONObject result) {

        String placeName;
        Random rand = new Random();

        try {
            JSONArray jsonArray = result.getJSONArray("results");

            if (result.getString(STATUS).equalsIgnoreCase(OK)) {
                int i = rand.nextInt(jsonArray.length() - 1);
                JSONObject place = jsonArray.getJSONObject(i);
                if (!place.isNull(NAME)) {
                    placeName = place.getString(NAME);
                    this.name = placeName;
                }
            } else if (result.getString(STATUS).equalsIgnoreCase(ZERO_RESULTS)) {
                Toast.makeText(getBaseContext(), "No Restaurants found in 5KM radius!",
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getBaseContext(), "Parsing error",
                    Toast.LENGTH_LONG).show();
        }
    }
}
