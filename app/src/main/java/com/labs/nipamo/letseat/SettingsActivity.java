package com.labs.nipamo.letseat;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /* Called when the user presses the "Current Location" radio button */
    public void setCurrent(View view){
        RadioButton custom = (RadioButton) findViewById(R.id.customLocation);
        TextView zipCode = (TextView) findViewById(R.id.zipcode);
        EditText zipCodeText = (EditText) findViewById(R.id.zipcodeText);
        Button apply = (Button) findViewById(R.id.applyButton);

        custom.setChecked(false);
        zipCode.setVisibility(View.INVISIBLE);
        zipCodeText.setVisibility(View.INVISIBLE);
        apply.setVisibility(View.INVISIBLE);
    }

    /* Called when the user presses the "Custom Location" radio button */
    public void setCustom(View view){
        RadioButton current = (RadioButton) findViewById(R.id.currentLocation);
        TextView zipCode = (TextView) findViewById(R.id.zipcode);
        EditText zipCodeText = (EditText) findViewById(R.id.zipcodeText);
        Button apply = (Button) findViewById(R.id.applyButton);

        current.setChecked(false);
        zipCode.setVisibility(View.VISIBLE);
        zipCodeText.setVisibility(View.VISIBLE);
        apply.setVisibility(View.VISIBLE);
    }
}