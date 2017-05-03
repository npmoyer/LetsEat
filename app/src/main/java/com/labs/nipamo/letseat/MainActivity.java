package com.labs.nipamo.letseat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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

       if (((FindLocation) getApplicationContext()).getCurrent() ||
               ((FindLocation) getApplicationContext()).getCustom()){
            // Start the Maps Activity
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } else{
           Toast toast = Toast.makeText(MainActivity.this,
                   "Select a location setting in the Settings screen", Toast.LENGTH_LONG);
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
        result.setText("It worked!");
        result.setVisibility(View.VISIBLE);
    }
}
