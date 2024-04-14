package com.fit2081.fit2081_assignment_1.activities;

import static com.fit2081.fit2081_assignment_1.activities.MainActivity.LOG_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.utilities.SMSReceiver;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;
import com.fit2081.fit2081_assignment_1.utilities.GenerateRandomId;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EventCategoryActivity extends AppCompatActivity {
    TextView findCategoryId;
    EditText findCategoryName;
    EditText findEventCount;
    Switch findCategoryIsActive;
    categoryBroadcastReceiver myBroadCastReceiver;
    ArrayList<EventCategory> categoryList;
    Gson gson = new Gson();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // Assign the attributes
        findCategoryName = findViewById(R.id.et_eventCategoryId);
        findEventCount = findViewById(R.id.et_eventName);
        findCategoryIsActive = findViewById(R.id.switch_isCategoryActive);
        findCategoryId = findViewById(R.id.tv_eventIdValue);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // Register a BroadCastReceiver to listen for incoming SMS
        myBroadCastReceiver = new categoryBroadcastReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.EVENT_CATEGORY_SMS_FILTER));

        // restore list data from SharedPreferences
        String arrayListStringRestored = new SharedPrefRestore(this).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
        // Convert the restored string back to ArrayList
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        categoryList = gson.fromJson(arrayListStringRestored,type);

        // Debugging
        Log.d(LOG_KEY, "launched category SMS Receiver");
        Log.d(LOG_KEY, "launched event category activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCastReceiver); // Kill the receiver when the activity is closed
        Log.d(LOG_KEY, "Broadcast receiver unregistered (category)");
    }

    public void createEventCategoryButtonOnClick(View view){
        // Parse the values
        String categoryName = findCategoryName.getText().toString();
        boolean isCategoryActive = findCategoryIsActive.isChecked();
        int eventCount;

        // Default value for event count
        try{
            eventCount = Integer.parseInt(findEventCount.getText().toString());
        } catch (Exception e){
            eventCount = 0;
        }

        // form validation
        if (categoryName.isEmpty()){ // check that event category name is filled
            Toast.makeText(this, "Event category name required", Toast.LENGTH_SHORT).show();
        } else {
            String categoryId = generateCategoryID();
            saveCategoryAttributesToSharedPreferences(categoryId, categoryName, eventCount, isCategoryActive);
            findCategoryId.setText(categoryId);

            String out = String.format("Category saved successfully: %s", categoryId); // Show event category ID
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();

            // Return to dashboard activity
            // or create intent to dash?
            finish();
        }
    }


    public void saveCategoryAttributesToSharedPreferences(String categoryId, String categoryName, int eventCount, boolean isActive){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Temporary adding to list
        addItemToCategoryList(new EventCategory(categoryId, categoryName, eventCount, isActive));

        // Add all attributes to SharedPreferences
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, gson.toJson(categoryList));
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_ID, categoryId);
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_NAME, categoryName);
        editor.putInt(EventCategorySharedPref.KEY_EVENT_COUNT, eventCount);
        editor.putBoolean(EventCategorySharedPref.KEY_IS_CATEGORY_ACTIVE, isActive);

        // Apply the changes
        editor.apply();
    }

    public void addItemToCategoryList(EventCategory newEventCategory){
        categoryList.add(newEventCategory);
        Log.d("list", String.format("Added item to category list, Size: %d, category Array: %s",categoryList.size(), categoryList.toString()));
    }

    private String generateCategoryID(){
        // Call the helper class to generate Category ID
        return String.format("C%s-%s", GenerateRandomId.generateRandomUpperString(2), GenerateRandomId.generateRandomInt(4));
    }

    class categoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_KEY, "launched Category Broadcast Receiver");
            String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

            String categoryName = null;
            int eventCount = 0;
            boolean isActive = false;
            boolean isMessageValid = false;

            if (tokenizeMessage.countTokens() == 3) {
                try {
                    categoryName = tokenizeMessage.nextToken();
                    eventCount = Integer.parseInt(tokenizeMessage.nextToken());
                    String isActiveString = tokenizeMessage.nextToken();
                    isActive = Boolean.parseBoolean(isActiveString);

                    // Validate SMS to only accept "category:"
                    if (!categoryName.startsWith("category:")) {
                        throw new IllegalArgumentException("Invalid");
                    }

                    // Validate event count to be positive integer
                    if (eventCount<0){
                        throw new IllegalArgumentException("Invalid");
                    }

                    // Checks that the value tokenized is only "TRUE" or "FALSE" not including casing
                    if (!isActiveString.equalsIgnoreCase("TRUE") && !isActiveString.equalsIgnoreCase("FALSE")) {
                        throw new IllegalArgumentException("Invalid boolean value");
                    }

                    // Valid token inputs
                    isMessageValid = true;
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid message format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Incorrect message format", Toast.LENGTH_SHORT).show();
                Log.d(LOG_KEY, "Category message INVALID");
            }

            // Set the fields to respective values if the message is valid
            if (isMessageValid) {
                findCategoryName.setText(ExtractStringAfterColon.extract(categoryName));
                findEventCount.setText(String.valueOf(eventCount));
                findCategoryIsActive.setChecked(isActive);
                Log.d(LOG_KEY, "Category message VALID");
            }
        }
    }
}