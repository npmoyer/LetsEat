package com.labs.nipamo.letseat;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        String name, location, rating, price;

        name = ((FindPlaces) getApplicationContext()).getName();
        location = ((FindPlaces) getApplicationContext()).getLocation();
        rating = ((FindPlaces) getApplicationContext()).getRating();
        price = ((FindPlaces) getApplicationContext()).getPrice();
        rating = rating + "/5";
        price = price + "/4";

        TextView nameText = (TextView) findViewById(R.id.result);
        TextView locationText = (TextView) findViewById(R.id.locationValue);
        TextView ratingText = (TextView) findViewById(R.id.ratingValue);
        TextView priceText = (TextView) findViewById(R.id.priceValue);


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

    public void notImplemented(View view){
        Toast toast = Toast.makeText(DetailsActivity.this,
                "Button has not been implemented yet", Toast.LENGTH_LONG);
        toast.show();
    }
}
