package com.fit2081.fit2081_assignment_1.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.activities.EventActivity;
import com.fit2081.fit2081_assignment_1.objects.Event;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventSharedPref;
import com.fit2081.fit2081_assignment_1.utilities.GenerateRandomId;
import com.fit2081.fit2081_assignment_1.utilities.SMSReceiver;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEventForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEventForm extends Fragment {
    TextView findEventId;
    EditText findCategoryId;
    EditText findEventName;
    EditText findTicketsAvailable;
    Switch findEventIsActive;
    ArrayList<Event> eventList;
    Gson gson = new Gson();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentEventForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEventForm newInstance(String param1, String param2) {
        FragmentEventForm fragment = new FragmentEventForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        // Assign the attributes to its respective views
        findEventId = view.findViewById(R.id.tv_eventIdValue);
        findCategoryId = view.findViewById(R.id.et_eventCategoryId);
        findEventName = view.findViewById(R.id.et_eventName);
        findTicketsAvailable = view.findViewById(R.id.et_ticketsAvailable);
        findEventIsActive = view.findViewById(R.id.switch_isEventActive);

        // restore list data from SharedPreferences
        String arrayListStringRestored = new SharedPrefRestore(getActivity()).restoreData(EventSharedPref.FILE_NAME, EventSharedPref.KEY_EVENT_LIST);
        // Convert the restored string back to ArrayList
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        eventList = gson.fromJson(arrayListStringRestored,type);
        Log.d("fragment_event form", String.format("event fragment %s",arrayListStringRestored));

        if (eventList == null) {
            eventList = new ArrayList<Event>();
            String eventListString = gson.toJson(eventList);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventSharedPref.KEY_EVENT_LIST, eventListString);
            editor.apply();
            Log.d("list", String.format("new list created at fragment: %s",eventList));
        }

        return view;
    }

    public boolean saveEventButtonOnClick(){
        Log.d("fab", "event fab clicked");
        boolean saveEvent = false;
        String categoryId = findCategoryId.getText().toString();
        String eventName = findEventName.getText().toString();
        int ticketsAvailable;
        boolean isEventActive = findEventIsActive.isChecked();

        // Default value for available tickets
        try{
            ticketsAvailable = Integer.parseInt(findTicketsAvailable.getText().toString());
        } catch (Exception e){
            ticketsAvailable = 0;
        }

        // form validation
        if (categoryId.isEmpty()){
            Toast.makeText(getActivity(), "Category ID required", Toast.LENGTH_SHORT).show();
        } else if (eventName.isEmpty()) {
            Toast.makeText(getActivity(), "Event name is required", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), out, Toast.LENGTH_SHORT).show();
                saveEvent = true;
            } else {
                Toast.makeText(getActivity(), "Category ID does not match format.", Toast.LENGTH_SHORT).show();
            }
        }
        return saveEvent;
    }

    private String generateEventId(){
        return String.format("E%s-%s", GenerateRandomId.generateRandomUpperString(2), GenerateRandomId.generateRandomInt(5));
    }

    private void saveEventAttributeToSharedPreferences(String eventId, String categoryId, String eventName, int ticketsAvailable, boolean isEventActive){
        // Get the destination to save the event attributes
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Add to shared pref list of events
        addItemToEventList(new Event(eventId, categoryId, eventName, ticketsAvailable, isEventActive));

        editor.putString(EventSharedPref.KEY_EVENT_LIST, gson.toJson(eventList));
        editor.putString(EventSharedPref.KEY_EVENT_ID, eventId);
        editor.putString(EventSharedPref.KEY_EVENT_CATEGORY_ID, categoryId);
        editor.putString(EventSharedPref.KEY_EVENT_NAME, eventName);
        editor.putInt(EventSharedPref.KEY_TICKETS_AVAILABLE, ticketsAvailable);
        editor.putBoolean(EventSharedPref.KEY_IS_EVENT_ACTIVE, isEventActive);

        editor.apply();
    }

    private void addItemToEventList(Event newEvent){
        // Add the new event to the list
        eventList.add(newEvent);
        Log.d("list", String.format("Added item to event list Size: %d, event Array: %s", eventList.size(), eventList.toString()));
    }

    private boolean validateCategoryId(String categoryId){
        String pattern = "C[A-Z]{2}-\\d{4}";
        return categoryId.matches(pattern);
    }

    public void clearFields(){
        findCategoryId.setText("");
        findEventName.setText("");
        findTicketsAvailable.setText("");
        findEventIsActive.setChecked(false);
    }
}