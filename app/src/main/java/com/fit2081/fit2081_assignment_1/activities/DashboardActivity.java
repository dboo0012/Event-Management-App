package com.fit2081.fit2081_assignment_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.fragments.FragmentEventForm;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllCategory;
import com.fit2081.fit2081_assignment_1.providers.CategoryViewModel;
import com.fit2081.fit2081_assignment_1.providers.Event;
import com.fit2081.fit2081_assignment_1.providers.EventCategory;
import com.fit2081.fit2081_assignment_1.providers.EventViewModel;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventSharedPref;
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
    public static FragmentListAllCategory fragmentListAllCategory;
    FragmentEventForm fragmentEventForm;
    Gson gson = new Gson();
    CategoryViewModel categoryViewModel;
    EventViewModel eventViewModel;
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
        setFab_save();

        // Create the fragments
        fragmentListAllCategory = new FragmentListAllCategory();
        getSupportFragmentManager().beginTransaction().replace(R.id.categoryListFragment, fragmentListAllCategory).commit(); // Set the adapter to the fragment

        fragmentEventForm = new FragmentEventForm();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_event, fragmentEventForm).commit(); // Set the adapter to the fragment

        // Initialise view models
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        // Initialise the shared preference lists at launch
//        initialiseSharedPrefLists();

        // Debugging
        Log.d(key, "launched dashboard activity");
    }

    public void initialiseSharedPrefLists(){
        // Event Category List
        // Grab the array list stored as String in SharedPreferences
        String categoryArrayListString = new SharedPrefRestore(this).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
        // Convert the restored string back to ArrayList
        Type categoryType = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        ArrayList<EventCategory> categoryList = gson.fromJson(categoryArrayListString,categoryType);

        // Initialize and save the list if it has not been
        if (categoryList == null) {
            categoryList = new ArrayList<EventCategory>();
            SharedPreferences sharedPreferences = getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, gson.toJson(categoryList));
            editor.apply();
            Log.d("list", String.format("new list created at fragment: %s",categoryList));
        }

        // Event List
        // restore list data from SharedPreferences
        String eventListString = new SharedPrefRestore(this).restoreData(EventSharedPref.FILE_NAME, EventSharedPref.KEY_EVENT_LIST);
        // Convert the restored string back to ArrayList
        Type eventType = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> eventList = gson.fromJson(eventListString,eventType);
        Log.d("fragment_event form", String.format("event fragment %s",eventListString));

        if (eventList == null) {
            eventList = new ArrayList<Event>();
            SharedPreferences sharedPreferences = getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventSharedPref.KEY_EVENT_LIST, gson.toJson(eventList));
            editor.apply();
            Log.d("list", String.format("new list created at fragment: %s",eventList));
        }
    }

    public void setFab_save(){
        fab_save = findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isEventSaved = fragmentEventForm.saveEventButtonOnClick();
                if (isEventSaved) {
                    showFABSnackbarMessageAction(view, "Saved Event");
                }
            }
        });
    }

    private void launchIntent(Class<?> targetClass){
        // Creates a new intent instance to the target activity class and launch it
        Intent newIntent = new Intent(this, targetClass);
        startActivity(newIntent);
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
            Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
//            Log.d("list", String.format("Size: %d, dashboard Array: %s", categoryList.size() , categoryList.toString()));
        } else if (itemId == R.id.option_clear) {
            // clear fields here
            fragmentEventForm.clearFields();
        } else if (itemId == R.id.option_delete_categories) {
            // clear categories shared pref list here
            categoryViewModel.deleteAllEventCategory();
            Toast.makeText(this, "All Event Categories Deleted.", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.option_delete_events) {
            // clear events shared pref list here
            eventViewModel.deleteAllEvents();
            Toast.makeText(this, "All Events Deleted.", Toast.LENGTH_SHORT).show();
        }
        // tell the OS
        return true;
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
                launchIntent(ViewAllEventActivity.class);
            } else if (itemId == R.id.drawer_logout) {
                finish();
                launchIntent(LoginActivity.class);
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
                // undo the action
                fragmentEventForm.removeLastAddedItem();
            }
        });
        snackbar.show();
    }

}
