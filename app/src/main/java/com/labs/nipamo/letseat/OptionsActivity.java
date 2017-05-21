package com.labs.nipamo.letseat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static com.labs.nipamo.letseat.R.menu.other;

public class OptionsActivity extends AppCompatActivity {

    static final String SPINNER1SAVE = "s1Save";
    static final String SPINNER2SAVE = "s2Save";
    static final String SPINNER3SAVE = "s3Save";
    static final String SPINNER4SAVE = "s4Save";
    static final String PREFERENCES = "Prefs";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Set up distance spinner
        Spinner spinner1 = (Spinner) findViewById(R.id.distanceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.options_distance_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        // Set up category spinner
        Spinner spinner2 = (Spinner) findViewById(R.id.categorySpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.options_category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        // Set up rating spinner
        Spinner spinner3 = (Spinner) findViewById(R.id.ratingSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.options_rating_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner3.setAdapter(adapter3);

        // Set up price spinner
        Spinner spinner4 = (Spinner) findViewById(R.id.priceSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.options_price_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner4.setAdapter(adapter4);

        // Set uo the app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            int s1Save, s2Save, s3Save, s4Save;
            s1Save = sharedPreferences.getInt(SPINNER1SAVE, 0);
            s2Save = sharedPreferences.getInt(SPINNER2SAVE, 0);
            s3Save = sharedPreferences.getInt(SPINNER3SAVE, 0);
            s4Save = sharedPreferences.getInt(SPINNER4SAVE, 0);

            spinner1.setSelection(s1Save);
            spinner2.setSelection(s2Save);
            spinner3.setSelection(s3Save);
            spinner4.setSelection(s4Save);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(other, menu);
        return true;
    }

    /* Called when the user pressed the "Apply" button */
    public void apply(View view){
        // Set up spinner variables
        Spinner spinner1 = (Spinner) findViewById(R.id.distanceSpinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.categorySpinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.ratingSpinner);
        Spinner spinner4 = (Spinner) findViewById(R.id.priceSpinner);

        setOptions(spinner1, spinner2, spinner3, spinner4);
        // Go back to the Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /* Called when the user pressed the "Reset" button */
    public void reset(View view) {
        // Set up spinner variables
        Spinner spinner1 = (Spinner) findViewById(R.id.distanceSpinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.categorySpinner);
        Spinner spinner3 = (Spinner) findViewById(R.id.ratingSpinner);
        Spinner spinner4 = (Spinner) findViewById(R.id.priceSpinner);

        // Reset the spinners to default values
        spinner1.setSelection(0);
        spinner2.setSelection(0);
        spinner3.setSelection(0);
        spinner4.setSelection(0);
    }

    private void setOptions(Spinner s1, Spinner s2, Spinner s3, Spinner s4){
        int distance = s1.getSelectedItemPosition();
        switch (distance){
            case 1:
                ((FindPlacesConfig) getApplicationContext()).setDistance(1609);
                break;
            case 2:
                ((FindPlacesConfig) getApplicationContext()).setDistance(3219);
                break;
            case 3:
                ((FindPlacesConfig) getApplicationContext()).setDistance(4828);
                break;
            case 4:
                ((FindPlacesConfig) getApplicationContext()).setDistance(6437);
                break;
            case 5:
                ((FindPlacesConfig) getApplicationContext()).setDistance(8047);
                break;
            case 6:
                ((FindPlacesConfig) getApplicationContext()).setDistance(32190);
                break;
            default:
                break;
        }

        String category = s2.getSelectedItem().toString();
        switch (category){
            case "Pizza":
                ((FindPlacesConfig) getApplicationContext()).setCategory(category);
                break;
            default:
                break;
        }

        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putInt(SPINNER1SAVE, s1.getSelectedItemPosition());
        editor.putInt(SPINNER2SAVE, s2.getSelectedItemPosition());
        editor.putInt(SPINNER3SAVE, s3.getSelectedItemPosition());
        editor.putInt(SPINNER4SAVE, s4.getSelectedItemPosition());
        editor.commit();
    }
}
