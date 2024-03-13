package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Debugging
        Log.d(key, "launched dashboard activity");
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