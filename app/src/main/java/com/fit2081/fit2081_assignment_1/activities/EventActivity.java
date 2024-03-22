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
import com.fit2081.fit2081_assignment_1.utilities.SMSReceiver;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventSharedPref;
import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;
import com.fit2081.fit2081_assignment_1.utilities.GenerateRandomId;

import java.util.StringTokenizer;

public class EventActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    TextView findEventId;
    EditText findCategoryId;
    EditText findEventName;
    EditText findTicketsAvailable;
    Switch findEventIsActive;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Assign the attributes to its respective views
        findEventId = findViewById(R.id.tv_eventIdValue);
        findCategoryId = findViewById(R.id.et_eventCategoryId);
        findEventName = findViewById(R.id.et_eventName);
        findTicketsAvailable = findViewById(R.id.et_ticketsAvailable);
        findEventIsActive = findViewById(R.id.switch_isEventActive);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        Log.d(LOG_KEY, "event broadcast receiver registered? " + BroadcastTracker.isBroadcastActive(this.getClass()));
        // Only instantiate a new broadcast receiver if there is none registered
        if (!BroadcastTracker.isBroadcastActive(this.getClass())) {
            eventBroadcastReceiver myBroadCastReceiver = new eventBroadcastReceiver();
            registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.EVENT_SMS_FILTER));
            BroadcastTracker.createBroadcastReceiver(this.getClass());
            Log.d(LOG_KEY, "new event broadcast receiver registered");
        }
        Log.d(LOG_KEY, "launched Event Activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventActivityTracker.activityResumed();
        Log.d(LOG_KEY, "Event Activity Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventActivityTracker.activityPaused();
        Log.d(LOG_KEY, "Event Activity Paused");
    }

    public void saveEventButtonOnClick(View view){
        String categoryId = findCategoryId.getText().toString();
        String eventName = findEventName.getText().toString();
        int ticketsAvailable;
        boolean isEventActive = findEventIsActive.isChecked();
//        findEventIsActive.setChecked(isEventActive);

        try{
            ticketsAvailable = Integer.parseInt(findTicketsAvailable.getText().toString());
        } catch (Exception e){
            ticketsAvailable = 0;
        }

        // form validation
        if (categoryId.isEmpty()){
            Toast.makeText(this, "Category ID required", Toast.LENGTH_SHORT).show();
        } else if (eventName.isEmpty()) {
            Toast.makeText(this, "Event name is required", Toast.LENGTH_SHORT).show();
        } else {
            String generatedEventId;

            // Verify categoryId format
            if (validateCategoryId(categoryId)) {
                generatedEventId = generateEventId();
                // save attributes to shared preferences
                saveEventAttributeToSharedPreferences(generatedEventId, categoryId, eventName,
                        ticketsAvailable, isEventActive);

                // Successful
                // show the generated event ID
                findEventId.setText(generatedEventId);
                String out = String.format("Event saved: %s to %s", generatedEventId, categoryId);
                Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Category ID does not match format.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String generateEventId(){
        return String.format("E%s-%s", GenerateRandomId.generateRandomUpperString(2), GenerateRandomId.generateRandomInt(5));
    }

    private void saveEventAttributeToSharedPreferences(String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isEventActive){
        // Get the destination to save the event attributes
        SharedPreferences sharedPreferences = getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EventSharedPref.KEY_EVENT_ID, eventId);
        editor.putString(EventSharedPref.KEY_EVENT_CATEGORY_ID, categoryId);
        editor.putString(EventSharedPref.KEY_EVENT_NAME, eventName);
        editor.putInt(EventSharedPref.KEY_TICKETS_AVAILABLE, ticketsAvailable);
        editor.putBoolean(EventSharedPref.KEY_IS_EVENT_ACTIVE, isEventActive);

        editor.apply();
    }

    private boolean validateCategoryId(String categoryId){
        boolean valid = false;

        String pattern = "C[A-Z]{2}-\\d{4}";
        return categoryId.matches(pattern);

//        return valid;
    }

    // Receive intercepted messages from SMSmessanger
    class eventBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // Only run the SMS receiver if the user is on this activity
            if (EventActivityTracker.isActivityVisible()) {
                String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

                StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

                String eventName = null;
                String categoryId = null;
                int ticketsAvailable = 0;
                boolean isActive = false;
                boolean isMessageValid = false;

                if (tokenizeMessage.countTokens() == 4) {
                    try {
                        // Validate SMS to only accept "event:"
                        eventName = tokenizeMessage.nextToken();
                        if (!eventName.startsWith("event:")) {
//                            throw new IllegalArgumentException("Invalid");
                            return;
                        }

                        categoryId = tokenizeMessage.nextToken();

                        // Validate event count to be positive integer
                        ticketsAvailable = Integer.parseInt(tokenizeMessage.nextToken());
                        if (ticketsAvailable<=0){
                            throw new IllegalArgumentException("Invalid");
//                            return;
                        }

                        String isActiveString = tokenizeMessage.nextToken();
                        // SMS Category ID validation
                        if (!validateCategoryId(categoryId)) {
                            String errorMsg = "Invalid Category ID format";
//                            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                            throw new IllegalArgumentException(errorMsg);
//                            return;
                        }

                        // SMS Boolean validation
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
                    Toast.makeText(context, "Incorrect format (event)", Toast.LENGTH_SHORT).show();
                }

                // Set the fields to respective values if the message is valid
                if (isMessageValid) {
                    findEventName.setText(ExtractStringAfterColon.extract(eventName));
                    findCategoryId.setText(categoryId);
                    findTicketsAvailable.setText(String.valueOf(ticketsAvailable));
                    findEventIsActive.setChecked(isActive);
                }

                Log.d(LOG_KEY, "launched Event Broadcast Receiver " + EventActivityTracker.isActivityVisible());
            }
        }
    }
}
