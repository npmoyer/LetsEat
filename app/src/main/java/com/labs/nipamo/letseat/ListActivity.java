package com.labs.nipamo.letseat;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.labs.nipamo.letseat.Constants.PlacesAPI.NAME;
import static com.labs.nipamo.letseat.Constants.Request.OK;
import static com.labs.nipamo.letseat.Constants.Request.RESULTS;
import static com.labs.nipamo.letseat.Constants.Request.STATUS;
import static com.labs.nipamo.letseat.Constants.Request.ZERO_RESULTS;
import static com.labs.nipamo.letseat.R.menu.other;

public class ListActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Set up the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Call the function to populate the list of places
        populateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(other, menu);
        return true;
    }

    private void populateListView(){
        // Get list of places
        // Use FindLocation to get the latitude and longitude
        ((FindLocation) getApplicationContext()).setLocation();
        double latitude = ((FindLocation) getApplicationContext()).getLatitude();
        double longitude = ((FindLocation) getApplicationContext()).getLongitude();

        // Create the url and execute the async task
        loadNearbyPlaces(latitude, longitude);
        JSONTaskList json = new JSONTaskList();
        json.execute();
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
        googlePlacesUrl.append("&key=" + BuildConfig.APIKey);

        this.url = googlePlacesUrl.toString();
    }

    /* Async task for getting the JSON result */
    private class JSONTaskList extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String googlePlacesUrl = url;
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
            String placeName = "?";

            try {
                ArrayList<String> places = new ArrayList<>();

                JSONArray jsonArray = result.getJSONArray(RESULTS);

                if (result.getString(STATUS).equalsIgnoreCase(OK)) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject place = jsonArray.getJSONObject(i);
                        if (!place.isNull(NAME)) {
                            placeName = place.getString(NAME);
                        }
                        places.add(placeName);
                    }

                    // Build the adaptor
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.list_text,
                            places);

                    // Configure the list view
                    ListView list = findViewById(R.id.list_view);
                    list.setAdapter(adapter);

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
