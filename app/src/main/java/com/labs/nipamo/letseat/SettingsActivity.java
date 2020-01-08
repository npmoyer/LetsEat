package com.labs.nipamo.letseat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.labs.nipamo.letseat.R.id.zipcode;
import static com.labs.nipamo.letseat.R.menu.other;

import static com.labs.nipamo.letseat.Constants.Settings.*;

public class SettingsActivity extends AppCompatActivity {
    public String ZIPCODE;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the back button
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (sharedPreferences != null){
            boolean customSave, currentSave;
            customSave = sharedPreferences.getBoolean(CUSTOMSAVE, false);
            currentSave = sharedPreferences.getBoolean(CURRENTSAVE, false);
            ZIPCODE = sharedPreferences.getString(ZIPCODESAVE, null);
            if (customSave){
                findViewById(R.id.customLocation).performClick();
            }
            if (currentSave){
                findViewById(R.id.currentLocation).performClick();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(other, menu);
        return true;
    }

    /* Called when the user presses the "Current Location" radio button */
    public void setCurrent(View view){
        // Set local variables
        RadioButton custom = findViewById(R.id.customLocation);
        TextView zipCode = findViewById(zipcode);
        EditText zipCodeText = findViewById(R.id.zipcodeText);

        // Hide custom location stuff
        custom.setChecked(false);
        zipCode.setVisibility(View.INVISIBLE);
        zipCodeText.setVisibility(View.INVISIBLE);

        FindLocation loc = new FindLocation();

        //Check for permission
        if (!loc.checkForPermission(SettingsActivity.this)) {
            // Permission is not granted so request it
            loc.promptForPermission(SettingsActivity.this);
            // Check if user pressed Deny
            if (!loc.checkForPermission(SettingsActivity.this)) {
                // Permission is denied :(
                Toast toast = Toast.makeText(SettingsActivity.this,
                        "Allow permissions to use current location!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        // Set shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean(CURRENTSAVE, true);
        editor.putBoolean(CUSTOMSAVE, false);
        editor.commit();
    }

    /* Called when the user presses the "Custom Location" radio button */
    public void setCustom(View view){
        // Set local variables
        RadioButton current = findViewById(R.id.currentLocation);
        TextView zipCode = findViewById(zipcode);
        EditText zipCodeText = findViewById(R.id.zipcodeText);

        // Hide current location stuff
        current.setChecked(false);
        zipCode.setVisibility(View.VISIBLE);
        zipCodeText.setVisibility(View.VISIBLE);

        if (ZIPCODE != null){
            zipCodeText.setText(ZIPCODE);
        }

        // Set shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        editor.putBoolean(CURRENTSAVE, false);
        editor.putBoolean(CUSTOMSAVE, true);
        editor.commit();
    }

    /* Called when the user pressed the "Apply" button */
    public void apply(View view){
        // Set local variables
        EditText zipCodeText = findViewById(R.id.zipcodeText);

        // Restore the saved data
        sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        boolean customSave;
        customSave = sharedPreferences.getBoolean(CUSTOMSAVE, false);

        // Check if zipcode should be applied
        if (customSave){
            ZIPCODE = zipCodeText.getText().toString();
            ((FindLocation) getApplicationContext()).setZip(ZIPCODE);
            SharedPreferences.Editor editor = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
            editor.putString(ZIPCODESAVE, ZIPCODE);
            editor.commit();
        }

        // Go back to the Main Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}