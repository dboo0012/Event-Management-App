package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllEvent;

public class ViewAllEventActivity extends AppCompatActivity {
    public static FragmentListAllEvent fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_event);

        // toolbar
        Toolbar toolbar = findViewById(R.id.event_back_bar);
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("View All Events");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // fragment
        fragment = new FragmentListAllEvent();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewAllEvent, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        // If the "up" button is clicked, finish the current activity
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}