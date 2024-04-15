package com.fit2081.fit2081_assignment_1.fragments;

import static android.content.Context.MODE_PRIVATE;

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
import com.fit2081.fit2081_assignment_1.objects.Event;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventSharedPref;
import com.fit2081.fit2081_assignment_1.utilities.GenerateRandomId;
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
    ArrayList<EventCategory> categoryList;
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
        loadCategoryList();
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
        }else if(!validateEventName(eventName)){
            Toast.makeText(getActivity(), "Invalid Event Name", Toast.LENGTH_SHORT).show();
        } else {
            String generatedEventId;

            // Verify categoryId format
            if (!validateCategoryId(categoryId)) {
                Toast.makeText(getActivity(), "Category ID does not match format.", Toast.LENGTH_SHORT).show();
            } else if (!validateCategoryIdInList(categoryId)) {
                Toast.makeText(getActivity(), "Category ID does not exist.", Toast.LENGTH_SHORT).show();
            } else if (validateCategoryId(categoryId) && validateCategoryIdInList(categoryId)) {
                generatedEventId = generateEventId();

                // save attributes to shared preferences
                saveEventAttributeToSharedPreferences(generatedEventId, categoryId, eventName,
                        ticketsAvailable, isEventActive);

                // update the event count in the category
                updateEventCount(categoryId);

                // Successful
                // show the generated event ID
                findEventId.setText(generatedEventId);
                String out = String.format("Event saved: %s to %s", generatedEventId, categoryId);
                Toast.makeText(getActivity(), out, Toast.LENGTH_SHORT).show();
                saveEvent = true;
            }
        }
        return saveEvent;
    }

    private void loadCategoryList(){
        // Restore the list of event categories from SharedPreferences
        String categoryListString = new SharedPrefRestore(getActivity()).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        categoryList = gson.fromJson(categoryListString, type);
    }

    private void updateEventCount(String categoryId) {
        if (categoryList != null) {
            // Iterate over the list to find the matching category
            for (EventCategory category : categoryList) {
                if (category.getCategoryId().equals(categoryId)) {
                    // Increment the event count by 1
                    category.setEventCount(category.getEventCount() + 1);
                    Log.d("count", "Event count updated by 1");
                    break;
                }
            }

            // Save the updated list back to SharedPreferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, gson.toJson(categoryList));
            editor.apply();
        }
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
        // Instantiate the list if it is null
        if (eventList == null) {
            eventList = new ArrayList<>();
            updateEventListSharedPref();
        }
        // Add the new event to the list
        eventList.add(newEvent);
        Log.d("list", String.format("Added item to event list Size: %d, event Array: %s", eventList.size(), eventList.toString()));
    }

    public void removeLastAddedItem(){
        if (eventList != null && !eventList.isEmpty()) {
            eventList.remove(eventList.size() - 1);
            updateEventListSharedPref();
            Toast.makeText(getActivity(), "Last event removed", Toast.LENGTH_SHORT).show();
            Log.d("list", String.format("Removed last item from event, list Size: %d, event Array: %s", eventList.size(), eventList.toString()));
        } else {
            Toast.makeText(getActivity(), "No events to remove", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteListData(){
        // Clear the list of events
        eventList.clear();
        updateEventListSharedPref();
        Toast.makeText(getActivity(), "All events deleted", Toast.LENGTH_SHORT).show();
        Log.d("list", String.format("list data cleared"));
    }

    private void updateEventListSharedPref(){
        // Get the destination to save the event attributes
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EventSharedPref.KEY_EVENT_LIST, gson.toJson(eventList));
        editor.apply();
    }

    private boolean validateCategoryId(String categoryId){
        String pattern = "C[A-Z]{2}-\\d{4}";
        return categoryId.matches(pattern);
    }

    private boolean validateCategoryIdInList(String categoryId){
        // Check if the categoryId exists in the categoryList
        if (categoryList != null) {
            for (EventCategory category : categoryList) {
                if (category.getCategoryId().equals(categoryId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateEventName(String eventName){
        String pattern = "^[a-zA-Z0-9]*$"; // ^: start of string; []: match any character in the set; *: zero or more times; $: end of string
        return eventName.matches(pattern);
    }

    public void clearFields(){
        findCategoryId.setText("");
        findEventName.setText("");
        findTicketsAvailable.setText("");
        findEventIsActive.setChecked(false);
    }
}