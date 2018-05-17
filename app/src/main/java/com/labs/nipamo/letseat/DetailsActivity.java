package com.labs.nipamo.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.labs.nipamo.letseat.R.menu.other;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Set up the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        String name, location, rating, price;

        name = ((FindPlaces) getApplicationContext()).getNameText();
        location = ((FindPlaces) getApplicationContext()).getLocationText();
        rating = ((FindPlaces) getApplicationContext()).getRatingText();
        price = ((FindPlaces) getApplicationContext()).getPriceText();
        rating = rating + "/5";
        price = price + "/4";

        TextView nameText = findViewById(R.id.result);
        TextView locationText = findViewById(R.id.locationValue);
        TextView ratingText = findViewById(R.id.ratingValue);
        TextView priceText = findViewById(R.id.priceValue);


        nameText.setText(name);
        locationText.setText(location);
        ratingText.setText(rating);
        priceText.setText(price);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(other, menu);
        return true;
    }

    /* Called when the user taps the "Show on Map" button */
    public void viewMap(View view) {
        // Start the Maps Activity
        Intent intent = new Intent(this, DetailedMapActivity.class);
        startActivity(intent);

    }

    public void notImplemented(View view){
        Toast toast = Toast.makeText(DetailsActivity.this,
                "Button has not been implemented yet", Toast.LENGTH_LONG);
        toast.show();
    }
}
