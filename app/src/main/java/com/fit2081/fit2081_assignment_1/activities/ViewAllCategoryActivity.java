package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.adapters.CategoryListRecyclerAdapter;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllCategory;
import com.fit2081.fit2081_assignment_1.providers.EventCategory;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ViewAllCategoryActivity extends AppCompatActivity {
    ArrayList<EventCategory> categoryList;
    Gson gson = new Gson();
    CategoryListRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_category);

        // Set the toolbar as the app bar
        Toolbar toolbar = findViewById(R.id.category_back_bar);
        setSupportActionBar(toolbar);

        // Enable the back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("View All Categories");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Display the list of categories using a fragment
        FragmentListAllCategory fragment = new FragmentListAllCategory();
//        fragment.setAdapter(adapter);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewAllCategory, fragment).commit(); // Set the adapter to the fragment
        Log.d("viewAllCategory", "launched view all category activity");
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