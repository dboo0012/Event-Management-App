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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.fragments.FragmentEventForm;
import com.fit2081.fit2081_assignment_1.objects.Event;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;
import com.fit2081.fit2081_assignment_1.utilities.SMSReceiver;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class EventActivity extends AppCompatActivity {
    TextView findEventId;
    EditText findCategoryId;
    EditText findEventName;
    EditText findTicketsAvailable;
    Switch findEventIsActive;
    eventBroadcastReceiver myBroadCastReceiver;
    ArrayList<Event> eventList;
    Gson gson = new Gson();
    FragmentEventForm fragmentEventForm;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Log.d(LOG_KEY,""+getLifecycle().getCurrentState());

        // Assign the attributes to its respective views
        findEventId = findViewById(R.id.tv_eventIdValue);
        findCategoryId = findViewById(R.id.et_eventCategoryId);
        findEventName = findViewById(R.id.et_eventName);
        findTicketsAvailable = findViewById(R.id.et_ticketsAvailable);
        findEventIsActive = findViewById(R.id.switch_isEventActive);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

        // Event fragment
        fragmentEventForm = new FragmentEventForm();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentEventView, fragmentEventForm).commit();

        Log.d(LOG_KEY, "launched Event Activity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_KEY, "Broadcast receiver unregistered (event)");
        unregisterReceiver(myBroadCastReceiver); // Kill the receiver when the activity is closed
        Log.d(LOG_KEY, "ACTIVITY DESTROYED");
    }

    public void saveEventButtonOnClick(View view){
        Log.d("boom", "boom");
        fragmentEventForm.saveEventButtonOnClick();
    }

//    public void saveEventButtonOnClick(View view){
//        String categoryId = findCategoryId.getText().toString();
//        String eventName = findEventName.getText().toString();
//        int ticketsAvailable;
//        boolean isEventActive = findEventIsActive.isChecked();
//
//        // Default value for available tickets
//        try{
//            ticketsAvailable = Integer.parseInt(findTicketsAvailable.getText().toString());
//        } catch (Exception e){
//            ticketsAvailable = 0;
//        }
//
//        // form validation
//        if (categoryId.isEmpty()){
//            Toast.makeText(this, "Category ID required", Toast.LENGTH_SHORT).show();
//        } else if (eventName.isEmpty()) {
//            Toast.makeText(this, "Event name is required", Toast.LENGTH_SHORT).show();
//        } else {
//            String generatedEventId;
//
//            // Verify categoryId format
//            if (validateCategoryId(categoryId)) {
//                generatedEventId = generateEventId();
//                // save attributes to shared preferences
//                saveEventAttributeToSharedPreferences(generatedEventId, categoryId, eventName,
//                        ticketsAvailable, isEventActive);
//
//                // Successful
//                // show the generated event ID
//                findEventId.setText(generatedEventId);
//                String out = String.format("Event saved: %s to %s", generatedEventId, categoryId);
//                Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
//
//                // Return to dashboard activity
//                finish();
//            } else {
//                Toast.makeText(this, "Category ID does not match format.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private String generateEventId(){
//        return String.format("E%s-%s", GenerateRandomId.generateRandomUpperString(2), GenerateRandomId.generateRandomInt(5));
//    }
//
//    private void saveEventAttributeToSharedPreferences(String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isEventActive){
//        // Get the destination to save the event attributes
//        SharedPreferences sharedPreferences = getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.putString(EventSharedPref.KEY_EVENT_ID, eventId);
//        editor.putString(EventSharedPref.KEY_EVENT_CATEGORY_ID, categoryId);
//        editor.putString(EventSharedPref.KEY_EVENT_NAME, eventName);
//        editor.putInt(EventSharedPref.KEY_TICKETS_AVAILABLE, ticketsAvailable);
//        editor.putBoolean(EventSharedPref.KEY_IS_EVENT_ACTIVE, isEventActive);
//
//        editor.apply();
//    }

    private boolean validateCategoryId(String categoryId){
        String pattern = "C[A-Z]{2}-\\d{4}";
        return categoryId.matches(pattern);
    }

    // Receive intercepted messages from SMSmessanger
    class eventBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_KEY, "launched Event Broadcast Receiver");
            String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

            String eventName = null;
            String categoryId = null;
            int ticketsAvailable = 0;
            boolean isEventActive = false;
            boolean isMessageValid = false;

            if (tokenizeMessage.countTokens() == 4) {
                try {
                    eventName = tokenizeMessage.nextToken();
                    categoryId = tokenizeMessage.nextToken();
                    ticketsAvailable = Integer.parseInt(tokenizeMessage.nextToken());
                    String isActiveString = tokenizeMessage.nextToken();
                    // Validate SMS to only accept "event:"
                    if (!eventName.startsWith("event:")) {
                        throw new IllegalArgumentException("Invalid");
                    }

                    // Validate event count to be positive integer
                    if (ticketsAvailable<0){
                        throw new IllegalArgumentException("Invalid");
                    }

                    // SMS Category ID validation
                    if (!validateCategoryId(categoryId)) {
                        throw new IllegalArgumentException("Invalid Category ID format");
                    }

                    // SMS Boolean validation
                    if (!isActiveString.equalsIgnoreCase("TRUE") && !isActiveString.equalsIgnoreCase("FALSE")) {
                        throw new IllegalArgumentException("Invalid boolean value");
                    }
                    isEventActive = Boolean.parseBoolean(isActiveString);

                    // Valid token inputs
                    isMessageValid = true;
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid message format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Incorrect message format", Toast.LENGTH_SHORT).show();
                Log.d(LOG_KEY, "Event message INVALID");
            }

            // Set the fields to respective values if the message is valid
            if (isMessageValid) {
                findCategoryId.setText(categoryId);
                findEventName.setText(ExtractStringAfterColon.extract(eventName));
                findTicketsAvailable.setText(String.valueOf(ticketsAvailable));
                findEventIsActive.setChecked(isEventActive);
                Log.d(LOG_KEY, "Event message VALID");
            }
        }
    }
}