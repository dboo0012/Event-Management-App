package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

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
    EditText findCategoryName;
    EditText findEventCount;
    Switch findIsCategoryActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // Generate category ID (with method)

        // Assign the attributes
//        findCategoryId = findViewById(R.id.tv_categoryId);
        findCategoryName = findViewById(R.id.et_categoryName);
        findEventCount = findViewById(R.id.et_eventCount);
        findIsCategoryActive = findViewById(R.id.switch_isCategoryActive);
//        findIsCategoryActive.setChecked(false);

        // set category id field to default every time
//        findCategoryId.setText("(auto generated upon creation");

        // Debugging
        Log.d(key, "launched event category activity");
    }

    public void createEventCategoryButtonOnClick(View view){
        // Parse the values
        String categoryName = findCategoryName.getText().toString();
        boolean isCategoryActive = findIsCategoryActive.isChecked();
        int eventCount;

        // TO BE VERIFIED
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

            saveAttributesToSharedPreferences(categoryId, categoryName, eventCount, isCategoryActive);

            String out = String.format("Category saved successfully: %s", categoryId); // Show event category ID
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
        }

    }

    private String generateCategoryID(){
        String id = "CME-1084";

        return id;
    }

    public void saveAttributesToSharedPreferences(String categoryId, String categoryName, int eventCount, boolean isActive){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("CategorySharedPref.java", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Add all attributes to SharedPreferences
        editor.putString(CategorySharedPref.KEY_CATEGORY_ID, categoryId);
        editor.putString(CategorySharedPref.KEY_CATEGORY_NAME, categoryName);
        editor.putInt(CategorySharedPref.KEY_EVENT_COUNT, eventCount);
        editor.putBoolean(CategorySharedPref.KEY_IS_ACTIVE, isActive);

        // Apply the changes
        editor.apply();
    }
}