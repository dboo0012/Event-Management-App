package com.fit2081.fit2081_assignment_1;

import static com.fit2081.fit2081_assignment_1.MainActivity.LOG_KEY;

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

import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;

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

        /* Request permissions to access SMS */
//        ActivityCompat.requestPermissions(this, new String[]{
//                Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        eventBroadcastReceiver myBroadCastReceiver = new eventBroadcastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
        Log.d(LOG_KEY, "launched SMS Receiver");
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
            String generatedEventId = generateEventId();

            // save attributes to shared preferences
            saveEventAttributeToSharedPreferences(generatedEventId, categoryId, eventName,
                    ticketsAvailable, isEventActive);

            // toast
            String out = String.format("Event saved: %s to %s", generatedEventId, categoryId);
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();

            // show the generated event ID
            findEventId.setText(generatedEventId);
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

    // Receive intercepted messages from SMSmessanger
    class eventBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

            String eventName = null;
            String categoryId = null;
            int ticketsAvailable = 0;
            boolean isActive = false;
            boolean isMessageValid = false;

            if (tokenizeMessage.countTokens() == 4) {
                try {
                    eventName = tokenizeMessage.nextToken();
                    categoryId = tokenizeMessage.nextToken();
                    ticketsAvailable = Integer.parseInt(tokenizeMessage.nextToken());
                    String isActiveString = tokenizeMessage.nextToken();
                    if (!isActiveString.equalsIgnoreCase("TRUE") && !isActiveString.equalsIgnoreCase("FALSE")) {
                        throw new IllegalArgumentException("Invalid boolean value");
                    }
                    isActive = Boolean.parseBoolean(isActiveString);
                    isMessageValid = true;
                } catch (Exception e){
                    Toast.makeText(context, "Invalid message format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Incorrect format", Toast.LENGTH_SHORT).show();
            }

            // Set the fields to respective values if the message is valid
            if(isMessageValid){
                findEventName.setText(ExtractStringAfterColon.extract(eventName));
                findCategoryId.setText(categoryId);
                findTicketsAvailable.setText(String.valueOf(ticketsAvailable));
                findEventIsActive.setChecked(isActive);
            }

            Log.d(LOG_KEY, "launched Event Broadcast Receiver");
        }
    }
}
