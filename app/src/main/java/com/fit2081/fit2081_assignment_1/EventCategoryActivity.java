package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class EventCategoryActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    EditText findCategoryName;
    EditText findEventCount;
    Switch findIsCategoryActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // Generate category ID (with method)

        // Assign the attributes
        findCategoryName = findViewById(R.id.et_categoryName);
        findEventCount = findViewById(R.id.et_eventCount);
        findIsCategoryActive = findViewById(R.id.switch_isCategoryActive);
//        findIsCategoryActive.setChecked(false);

        // Debugging
        Log.d(key, "launched event category activity");
    }

    public void createEventCategoryButtonOnClick(View view){
        // Parse the values
        String categoryName = findCategoryName.getText().toString();
        int eventCount = Integer.parseInt(findEventCount.getText().toString());
        boolean isCategoryActive = findIsCategoryActive.isChecked();

        // form validation
        if (categoryName.isEmpty()){ // check that event category name is filled
            Toast.makeText(this, "Event category name required", Toast.LENGTH_SHORT).show();
        } else {
//            saveAttributesToSharedPreferences(<?>,categoryName, eventCount, isCategoryActive);

            String out = "Category saved successfully: CME-1084"; // Show event category ID
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
        }

    }

    public void saveAttributesToSharedPreferences(String categoryId, String categoryName, int eventCount, boolean isActive){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("CategorySharedPref.java", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Add all attributes to SharedPreferences
        editor.putString(CategorySharedPref.categoryId, categoryId);
        editor.putString(CategorySharedPref.categoryName, categoryName);
        editor.putInt(CategorySharedPref.eventCount, eventCount);
        editor.putBoolean(CategorySharedPref.isActive, isActive);

        editor.apply();
    }
}