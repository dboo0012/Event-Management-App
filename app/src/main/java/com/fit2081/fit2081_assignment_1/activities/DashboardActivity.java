package com.fit2081.fit2081_assignment_1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class DashboardActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    Toolbar toolbar;
    NavigationView navView;
    DrawerLayout drawerLayout;
    FloatingActionButton fab_save;
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

        // Flaoting action button
        fab_save = findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbarMessage(view, "Saved Event");
            }
        });

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

    class NavigationHandler implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.drawer_view_category) {
                Snackbar.make(navView, "View Category", Snackbar.LENGTH_SHORT).show();
            } else if (itemId == R.id.drawer_add) {
                launchIntent(EventCategoryActivity.class);
                Snackbar.make(navView, "Add category", Snackbar.LENGTH_SHORT).show();
            } else if (itemId == R.id.drawer_view_events) {
                Snackbar.make(navView, "View All Events", Snackbar.LENGTH_SHORT).show();
            } else if (itemId == R.id.drawer_logout) {
                Snackbar.make(navView, "Logout", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        }
    }
    private void showSnackbarMessage(View view, String message) {
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
