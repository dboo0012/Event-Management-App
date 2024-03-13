package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class EventCategoryActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    EditText findEventName;
    EditText findEventCount;
    Switch isEventActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // Assign the attributes
        findEventName = findViewById(R.id.et_categoryName);
        findEventCount = findViewById(R.id.et_eventCount);
        isEventActive = findViewById(R.id.switch_isCategoryActive);

        // Debugging
        Log.d(key, "launched main activity");
    }

    public void createEventCategoryButtonOnClick(View view){
        // form validation
        // check that event category name is filled
    }

    public void saveAttributesToSharedPreferences(String username, String password){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("DatabaseStore.java", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Update the values of the shared preference
        editor.putString(DatabaseStore.KEY_USERNAME, username);
        editor.putString(DatabaseStore.KEY_PASSWORD, password);
        editor.apply();

//        findUsername.setText(username);
    }

}