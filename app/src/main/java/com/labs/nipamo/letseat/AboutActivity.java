package com.labs.nipamo.letseat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import static com.labs.nipamo.letseat.R.menu.other;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set up the app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        inflater.inflate(other, menu);
        return true;
    }

    /* Called when the user taps the "Send Feedback" button */
    public void sendFeedback(View view){
        Uri uri = Uri.parse("mailto:moyer.n.p@gmail.com?subject=Let's Eat App Feedback\n");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setData(uri);
        startActivity(intent);
    }

    public void openBrowser(View view){
        // Get url from tag
        String url = (String)view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        // Pass the url to intent data
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }
}