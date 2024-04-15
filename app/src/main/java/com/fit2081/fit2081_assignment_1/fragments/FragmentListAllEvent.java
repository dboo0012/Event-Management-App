package com.fit2081.fit2081_assignment_1.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.adapters.EventListRecyclerAdapter;
import com.fit2081.fit2081_assignment_1.objects.Event;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventSharedPref;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListAllEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListAllEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    EventListRecyclerAdapter adapter;
    ArrayList<Event> eventList;
    Gson gson = new Gson();

    public FragmentListAllEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListAllEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListAllEvent newInstance(String param1, String param2) {
        FragmentListAllEvent fragment = new FragmentListAllEvent();
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
        View view = inflater.inflate(R.layout.fragment_list_all_event, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewEvent);

        // Set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        restoreListData();
        // Create the adapter with the shared pref list data
        adapter = new EventListRecyclerAdapter(eventList);
        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void restoreListData(){
        // restore list data from SharedPreferences
        String arrayListStringRestored = new SharedPrefRestore(getActivity()).restoreData(EventSharedPref.FILE_NAME, EventSharedPref.KEY_EVENT_LIST);
        // Convert the restored string back to ArrayList
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        eventList = gson.fromJson(arrayListStringRestored,type);

        Log.d("fragment_event", String.format("event fragment %s",arrayListStringRestored));


        // Initialize and save the list if user enter view all events before creating any events
        if (eventList == null) {
            eventList = new ArrayList<Event>();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventSharedPref.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EventSharedPref.KEY_EVENT_LIST, gson.toJson(eventList));
            editor.apply();
            Log.d("list", String.format("new list created at fragment: %s",eventList));
        }
//        notifyAdapter();

        // Initializes a category list if it has not been
        Log.d("fragment_event", String.format("list restored at event fragment, size: %d, %s",eventList.size(),eventList));
//        Log.d("fragment_event", String.format("list restored at event fragment: %s",eventList));
    }

    public void notifyAdapter() {
        if (adapter != null) {
            restoreListData();
            // Update the data in the adapter
            adapter.updateData(eventList);
            adapter.notifyDataSetChanged();
            Log.d("adapter", "Adapter notified");
        }
    }
}