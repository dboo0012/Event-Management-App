package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

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

    }

    private void generateEventId(){

    }

    private void saveEventAttributeToSharedPreferences(){

    }
}