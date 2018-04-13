package com.labs.nipamo.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static com.labs.nipamo.letseat.R.menu.other;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Set up the app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Call the function to populate the list of places
        // populateListView();
        // TODO: Fix list view population problem
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(other, menu);
        return true;
    }


    private void populateListView(){
        // Get list of places
         String[] places = ((FindPlacesConfig) getApplicationContext()).getPlaces();
         // String[] places = {"Test1", "Test2", "Test3"};

         if (places.length < 1){
             Intent intent = new Intent (this, MapsActivity.class);
             startActivity(intent);
         }

        // Build the adaptor
        ArrayAdapter<String> adaptor = new ArrayAdapter<>(this,
                R.layout.list_text,
                places);

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.list_view);
        list.setAdapter(adaptor);
    }
}
