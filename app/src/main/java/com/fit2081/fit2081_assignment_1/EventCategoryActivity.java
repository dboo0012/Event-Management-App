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

import com.fit2081.fit2081_assignment_1.sharedPreferences.CategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.ExtractStringAfterColon;

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

        /* Request permissions to access SMS */
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS, android.Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        /* Create and instantiate the local broadcast receiver
           This class listens to messages come from class SMSReceiver
         */
        categoryBroadcastReceiver myBroadCastReceiver = new categoryBroadcastReceiver();

        /*
         * Register the broadcast handler with the intent filter that is declared in
         * class SMSReceiver @line 11
         * */
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.EVENT_CATEGORY_SMS_FILTER), RECEIVER_EXPORTED);
        Log.d(LOG_KEY, "launched category SMS Receiver");

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

//        eventCount = findEventCount.getText().toString().isEmpty() ? 0 : Integer.parseInt(findEventCount.getText().toString());


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

    class categoryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String interceptedMessage = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);

            // Preliminary check: if the SMS does not start with "category:", return immediately
//            if (interceptedMessage==null || !interceptedMessage.startsWith("category:")) {
//                Toast.makeText(context, "Incorrect Message context", Toast.LENGTH_SHORT).show();
//                return;
//            }

            StringTokenizer tokenizeMessage = new StringTokenizer(interceptedMessage, ";");

            String categoryName = null;
            int eventCount = 0;
            boolean isActive = false;
            boolean isMessageValid = false;

            if (tokenizeMessage.countTokens() == 3) {
                try {
                    categoryName = tokenizeMessage.nextToken();
                    eventCount = Integer.parseInt(tokenizeMessage.nextToken());
                    // Checks that the value tokenized is only "TRUE" or "FALSE" not including casing
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
                Toast.makeText(context, "Incorrect f", Toast.LENGTH_SHORT).show();
            }

            // Set the fields to respective values if the message is valid
            if (isMessageValid){
                findCategoryName.setText(ExtractStringAfterColon.extract(categoryName));
                findEventCount.setText(String.valueOf(eventCount));
                findCategoryIsActive.setChecked(isActive);
            }

            Log.d(LOG_KEY, "launched Category Broadcast Receiver");
        }
    }
}