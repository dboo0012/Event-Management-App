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
import com.fit2081.fit2081_assignment_1.Trackers.BroadcastTracker;
import com.fit2081.fit2081_assignment_1.Trackers.EventActivityTracker;
import com.fit2081.fit2081_assignment_1.Trackers.EventCategoryActivityTracker;
import com.fit2081.fit2081_assignment_1.utilities.SMSReceiver;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;
import com.fit2081.fit2081_assignment_1.utilities.GenerateRandomId;

import java.util.StringTokenizer;

public class EventCategoryActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    TextView findCategoryId;
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
        findCategoryName = findViewById(R.id.et_eventCategoryId);
        findEventCount = findViewById(R.id.et_eventName);
        findCategoryIsActive = findViewById(R.id.switch_isCategoryActive);
        findCategoryId = findViewById(R.id.tv_eventIdValue);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        Log.d(LOG_KEY, "category broadcast receiver registered? " + BroadcastTracker.isBroadcastActive(this.getClass()));
        // Only instantiate a new broadcast receiver if there is none registered
        if (!BroadcastTracker.isBroadcastActive(this.getClass())){
            categoryBroadcastReceiver myBroadCastReceiver = new categoryBroadcastReceiver();
            registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.EVENT_CATEGORY_SMS_FILTER));
            BroadcastTracker.createBroadcastReceiver(this.getClass());
            Log.d(LOG_KEY, "new category broadcast receiver registered");
        }

        Log.d(LOG_KEY, "launched category SMS Receiver");

        // Debugging
        Log.d(key, "launched event category activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventCategoryActivityTracker.activityResumed();
        Log.d(LOG_KEY, "Event Category Activity Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventCategoryActivityTracker.activityPaused();
        Log.d(LOG_KEY, "Event Category Activity Paused");
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
        // Call the helper class to generate Category ID
        return String.format("C%s-%s", GenerateRandomId.generateRandomUpperString(2), GenerateRandomId.generateRandomInt(4));
    }

    public void saveCategoryAttributesToSharedPreferences(String categoryId, String categoryName, int eventCount, boolean isActive){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Add all attributes to SharedPreferences
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_ID, categoryId);
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_NAME, categoryName);
        editor.putInt(EventCategorySharedPref.KEY_EVENT_COUNT, eventCount);
        editor.putBoolean(EventCategorySharedPref.KEY_IS_CATEGORY_ACTIVE, isActive);

        // Apply the changes
        editor.apply();
    }

    class categoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only run the SMS receiver if the user is on this activity
            if (EventCategoryActivityTracker.isActivityVisible()) {
                String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

                StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

                String categoryName = null;
                int eventCount = 0;
                boolean isActive = false;
                boolean isMessageValid = false;

                if (tokenizeMessage.countTokens() == 3) {
                    try {
                        // Validate SMS to only accept "category:"
                        categoryName = tokenizeMessage.nextToken();
                        if (!categoryName.startsWith("category:")) {
//                            throw new IllegalArgumentException("Invalid");
                            return;
                        }

                        // Validate event count to be positive integer
                        eventCount = Integer.parseInt(tokenizeMessage.nextToken());
                        if (eventCount<=0){
                            throw new IllegalArgumentException("Invalid");
//                            return;
                        }

                        // Checks that the value tokenized is only "TRUE" or "FALSE" not including casing
                        String isActiveString = tokenizeMessage.nextToken();
                        if (!isActiveString.equalsIgnoreCase("TRUE") && !isActiveString.equalsIgnoreCase("FALSE")) {
                            throw new IllegalArgumentException("Invalid boolean value");
//                            return;
                        }
                        isActive = Boolean.parseBoolean(isActiveString);
                        isMessageValid = true;
                    } catch (Exception e) {
                        Toast.makeText(context, "Invalid message format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Incorrect format (category)", Toast.LENGTH_SHORT).show();
                }

                // Set the fields to respective values if the message is valid
                if (isMessageValid) {
                    findCategoryName.setText(ExtractStringAfterColon.extract(categoryName));
                    findEventCount.setText(String.valueOf(eventCount));
                    findCategoryIsActive.setChecked(isActive);
                }

                Log.d(LOG_KEY, "launched Category Broadcast Receiver " + EventActivityTracker.isActivityVisible());
            }
        }
    }
}