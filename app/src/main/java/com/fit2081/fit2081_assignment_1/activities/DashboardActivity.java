package com.fit2081.fit2081_assignment_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;

public class DashboardActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_layout);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Debugging
        Log.d(key, "launched dashboard activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.option_refresh) {
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_clear) {
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_delete_categories) {
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_delete_events) {
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        }
        // tell the OS
        return true;
    }

    private void launchIntent(Class<?> targetClass){
        // Creates a new intent instance to the target activity class and launch it
        Intent newIntent = new Intent(this, targetClass);
        startActivity(newIntent);
    }

    public void eventCategoryButtonOnClick(View view){
        launchIntent(EventCategoryActivity.class);
    }

    public void addEventButtonOnClick(View view){
        launchIntent(EventActivity.class);
    }
}