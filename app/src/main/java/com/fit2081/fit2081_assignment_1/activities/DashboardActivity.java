package com.fit2081.fit2081_assignment_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.adapters.ListViewRecyclerAdapter;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllCategory;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    FloatingActionButton fab_save;
    ListViewRecyclerAdapter adapter;
    ArrayList<EventCategory> categoryList;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Using the toolbar as action bar

        // Nav drawer
        navView = findViewById(R.id.nav_view);
        NavigationHandler navigationHandler = new NavigationHandler();
        navView.setNavigationItemSelectedListener(navigationHandler); // Listens to navigation item selection

        // Sync
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Floating action button
        fab_save = findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFABSnackbarMessageAction(view, "Saved Event");
            }
        });

        // Initialize the category list at launch
        Log.d("restore", String.format("at onCreate: %s",categoryList));
        restoreListData();
        Log.d("restore", String.format("after at onCreate: %s",categoryList));

        // Create the adapter with the shared pref list data
        adapter = new ListViewRecyclerAdapter(categoryList);

        // Create the fragment
        FragmentListAllCategory fragment = new FragmentListAllCategory();
        fragment.setAdapter(adapter);
        getSupportFragmentManager().beginTransaction().replace(R.id.categoryListFragment, fragment).commit(); // Set the adapter to the fragment


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.option_refresh) {
            // notify adapter of changes here
            notifyAdapter();
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
            Log.d("list", String.format("Size: %d, dashboard Array: %s", categoryList.size() , categoryList.toString()));
        } else if (itemId == R.id.option_clear) {
            // clear fields here
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_delete_categories) {
            // clear categories shared pref list here
            deleteListData();
            Toast.makeText(this, "All Event Categories Wiped.", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_delete_events) {
            // clear events shared pref list here
            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
        }
        // tell the OS
        return true;
    }

    public void notifyAdapter() {
        if (adapter != null) {
            restoreListData();
            // Update the data in the adapter
            adapter.updateData(categoryList);
            adapter.notifyDataSetChanged();
            Log.d("adapter", "Adapter notified");
        }
    }

    private void restoreListData(){
        // Grab the array list stored as String in SharedPreferences
        String arrayListStringRestored = new SharedPrefRestore(this).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
        // Convert the restored string back to ArrayList
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        categoryList = gson.fromJson(arrayListStringRestored,type);

        // Initialize and save the list if it has not been
        if (categoryList == null) {
            categoryList = new ArrayList<EventCategory>();
            String categoryListString = gson.toJson(categoryList);
            SharedPreferences sharedPreferences = getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, categoryListString);
            editor.apply();
        }

        // Initializes a category list if it has not been
        Log.d("restore", String.format("%s",categoryList));
    }

    private void deleteListData(){
        // Clear the list of categories
        categoryList.clear();

        // Save the empty list to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String emptyListJson = gson.toJson(categoryList);
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, emptyListJson);
        editor.apply();

        // Update the adapter with the new empty list
        notifyAdapter();
    }

    class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.drawer_view_category) {
                Snackbar.make(navView, "View Category", Snackbar.LENGTH_SHORT).show();
                launchIntent(ViewAllCategoryActivity.class);
            } else if (itemId == R.id.drawer_add) {
                Snackbar.make(navView, "Add category", Snackbar.LENGTH_SHORT).show();
                launchIntent(EventCategoryActivity.class);
            } else if (itemId == R.id.drawer_view_events) {
                Snackbar.make(navView, "View All Events", Snackbar.LENGTH_SHORT).show();
            } else if (itemId == R.id.drawer_logout) {
                finish();
                launchIntent(MainActivity.class);
                Snackbar.make(navView, "Logout", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        }
    }
    private void showFABSnackbarMessageAction(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "undo clicked", Toast.LENGTH_SHORT).show();
            }
        });
        snackbar.show();
    }

}
