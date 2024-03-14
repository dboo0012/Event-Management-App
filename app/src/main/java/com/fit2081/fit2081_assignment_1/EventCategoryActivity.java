package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class EventCategoryActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    TextView findCategoryId;
//    EditText findCategoryId;
    EditText findCategoryName;
    EditText findEventCount;
    Switch findCategoryIsActive;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // Generate category ID (with method)

        // Assign the attributes
//        findCategoryId = findViewById(R.id.et_categoryId);
        findCategoryName = findViewById(R.id.et_eventCategoryId);
        findEventCount = findViewById(R.id.et_eventName);
        findCategoryIsActive = findViewById(R.id.switch_isCategoryActive);
        findCategoryId = findViewById(R.id.tv_eventIdValue);
//        findIsCategoryActive.setChecked(false);

        // set category id field to default every time
//        findCategoryId.setText("(auto generated upon creation");

        // Debugging
        Log.d(key, "launched event category activity");
    }

    public void createEventCategoryButtonOnClick(View view){
        // Parse the values
        String categoryName = findCategoryName.getText().toString();
        boolean isCategoryActive = findCategoryIsActive.isChecked();
        int eventCount;

        // TO BE VERIFIED if it can be used
        try{
            eventCount = Integer.parseInt(findEventCount.getText().toString());
        } catch (Exception e){
            eventCount = 0;
        }

        // form validation
        if (categoryName.isEmpty()){ // check that event category name is filled
            Toast.makeText(this, "Event category name required", Toast.LENGTH_SHORT).show();
            Log.d(key, "name not entered");
        } else {
            String categoryId = generateCategoryID();

            saveCategoryAttributesToSharedPreferences(categoryId, categoryName, eventCount, isCategoryActive);

            String out = String.format("Category saved successfully: %s", categoryId); // Show event category ID
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();

            findCategoryId.setText(categoryId);
        }

    }

    private String generateCategoryID(){
        String id = "CME-1084";

        return id;
    }

    public void saveCategoryAttributesToSharedPreferences(String categoryId, String categoryName, int eventCount, boolean isActive){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences(CategorySharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Add all attributes to SharedPreferences
        editor.putString(CategorySharedPref.KEY_CATEGORY_ID, categoryId);
        editor.putString(CategorySharedPref.KEY_CATEGORY_NAME, categoryName);
        editor.putInt(CategorySharedPref.KEY_EVENT_COUNT, eventCount);
        editor.putBoolean(CategorySharedPref.KEY_IS_CATEGORY_ACTIVE, isActive);

        // Apply the changes
        editor.apply();
    }
}