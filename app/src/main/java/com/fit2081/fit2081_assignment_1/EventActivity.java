package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
        findEventId = findViewById(R.id.tv_eventId);
        findCategoryId = findViewById(R.id.et_eventCategoryId);
        findEventName = findViewById(R.id.et_eventName);
        findTicketsAvailable = findViewById(R.id.et_ticketsAvailable);
        findEventIsActive = findViewById(R.id.switch_isEventActive);
    }

    public void saveEventButtonOnClick(View view){
        String categoryId = findCategoryId.getText().toString();
        String eventName = findEventName.getText().toString();
        int ticketsAvailable;
        boolean isEventActive = findEventIsActive.isChecked();

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

        }

    }

    private String generateEventId(){
        String eventId = "EME-10776";

        return eventId;
    }

    private void saveEventAttributeToSharedPreferences(String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isEventActive){
        // Get the destination to save the event attributes
        SharedPreferences sharedPreferences = getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(EventSharedPref.KEY_EVENT_ID, eventId);

        editor.apply();
    }
}