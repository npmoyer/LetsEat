package com.labs.nipamo.letseat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import static com.labs.nipamo.letseat.Constants.PlacesAPI.GEOMETRY;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.LATITUDE;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.LOCATION;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.LONGITUDE;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.NAME;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.PRICE;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.RATING;
import static com.labs.nipamo.letseat.Constants.PlacesAPI.VICINITY;
import static com.labs.nipamo.letseat.Constants.Request.OK;
import static com.labs.nipamo.letseat.Constants.Request.RESULTS;
import static com.labs.nipamo.letseat.Constants.Request.STATUS;
import static com.labs.nipamo.letseat.Constants.Request.ZERO_RESULTS;
import static com.labs.nipamo.letseat.Constants.Settings.CURRENTSAVE;
import static com.labs.nipamo.letseat.Constants.Settings.CUSTOMSAVE;
import static com.labs.nipamo.letseat.Constants.Settings.PREFERENCES;

public class MainActivity extends AppCompatActivity {

    private String url;
    private int prev, count;
    private InterstitialAd interstitial;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ad banner
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

        // Set up the interstitial ad
        count = 0;
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(BuildConfig.IntID);
        interstitial.loadAd(new AdRequest.Builder().build());
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
            case R.id.options:
                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                return true;

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

    /* Called when the user taps the 4 main buttons
       Displays an ad if loaded and increments the counter */
    private void interstitialAd(){
        // Show interstitial ad
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        // Increment the counter
        count++;

        // Load a new ad every 7 clicks
        if (count % 7 == 0){
            interstitial.loadAd(new AdRequest.Builder().build());
        }
    }

    /* Called when the user taps the "Show Map" button */
    public void viewMap(View view){
        interstitialAd();

        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if (!sharedPreferences.getBoolean(CUSTOMSAVE, false)) {
            FindLocation loc = new FindLocation();

            //Check for permission
            if (!loc.checkForPermission(MainActivity.this)) {
                // Permission is not granted so request it
                loc.promptForPermission(MainActivity.this);

                // Save in shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
                editor.putBoolean(CURRENTSAVE, true);
                editor.commit();
            } else {
                // Start the Maps Activity
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            }
        }else{
            // Start the Maps Activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    /* Called when the user taps the "View List" button */
    public void viewList(View view){
        interstitialAd();

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the "Details" button */
    public void showDetails(View view){
        interstitialAd();

        Intent intent = new Intent (this, DetailsActivity.class);
        startActivity(intent);
    }

    /* Called when the user taps the "Let's Eat" button */
    public void letsEat(View view){
        double latitude;
        double longitude;

        interstitialAd();

        // Set up shared preferences
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        // Check if user selected a custom location
        if (!sharedPreferences.getBoolean(CUSTOMSAVE, false)){
            Toast toast = Toast.makeText(MainActivity.this,
                    "Using current location", Toast.LENGTH_LONG);
            toast.show();

            FindLocation loc = new FindLocation();

            //Check for permission
            if (!loc.checkForPermission(MainActivity.this)) {
                // Permission is not granted so request it
                loc.promptForPermission(MainActivity.this);

                // Save in shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
                editor.putBoolean(CURRENTSAVE, true);
                editor.commit();
            }else{
                // Use FindLocation to get the latitude and longitude
                ((FindLocation) getApplicationContext()).setLocation();
                latitude = ((FindLocation) getApplicationContext()).getLatitude();
                longitude = ((FindLocation) getApplicationContext()).getLongitude();

                // Create the url and execute the async task
                loadNearbyPlaces(latitude, longitude);
                JSONTaskMain json = new JSONTaskMain();
                json.execute();
            }
        }else {
            Toast toast = Toast.makeText(MainActivity.this,
                    "Using custom location", Toast.LENGTH_LONG);
            toast.show();

            // Use FindLocation to get the latitude and longitude
            ((FindLocation) getApplicationContext()).setLocation();
            latitude = ((FindLocation) getApplicationContext()).getLatitude();
            longitude = ((FindLocation) getApplicationContext()).getLongitude();

            // Create the url and execute the async task
            loadNearbyPlaces(latitude, longitude);
            JSONTaskMain json = new JSONTaskMain();
            json.execute();
        }
    }

    /* Creates a url to be passed to the async task */
    private void loadNearbyPlaces(double latitude, double longitude){
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
        googlePlacesUrl.append("&key=" + BuildConfig.PlacesAPI);

        this.url = googlePlacesUrl.toString();
    }

    /* Async task for getting the JSON result */
    private class JSONTaskMain extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String googlePlacesUrl = MainActivity.this.url;
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

        @Override
        protected void onPostExecute(String status) {
        }

        /* Get the place name, location, rating and price */
        private void parseLocationResult(JSONObject result) {
            // Set up local variables
            String placeName,
                    placeLocation = "?",
                    placeRating = "?",
                    placePrice = "?",
                    placeLat = "0",
                    placeLng = "0";

            Random rand = new Random();

            try {
                JSONArray jsonArray = result.getJSONArray(RESULTS);

                if (result.getString(STATUS).equalsIgnoreCase(OK)) {
                    // Pick a random place
                    int i = MainActivity.this.prev;
                    if (jsonArray.length() == 1){
                        // Only one result
                        Toast.makeText(getBaseContext(), "Not other restaurant found",
                                Toast.LENGTH_LONG).show();
                    }else {
                        while (i == MainActivity.this.prev) {
                            i = rand.nextInt(jsonArray.length());
                        }
                    }
                    MainActivity.this.prev = i;
                    JSONObject place = jsonArray.getJSONObject(i);
                    if (!place.isNull(NAME)) {
                        placeName = place.getString(NAME);
                        if (place.has(VICINITY))
                            placeLocation = place.getString(VICINITY);
                        if (place.has(RATING))
                            placeRating = place.getString(RATING);
                        if (place.has(PRICE))
                            placePrice = place.getString(PRICE);
                        if (place.has(GEOMETRY)) {
                            placeLat = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                                    .getString(LATITUDE);
                            placeLng = place.getJSONObject(GEOMETRY).getJSONObject(LOCATION)
                                    .getString(LONGITUDE);
                        }

                        ((FindPlaces) getApplicationContext()).setAll(placeName,placeLocation,
                                placeRating,placePrice, placeLat, placeLng);

                        // Set up the local variables
                        TextView resultText = findViewById(R.id.result);
                        Button details = findViewById(R.id.details_button);

                        // Display the result
                        if (placeName != null){
                            resultText.setText(placeName);
                            resultText.setVisibility(View.VISIBLE);
                            details.setVisibility(View.VISIBLE);
                        }

                    }
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
