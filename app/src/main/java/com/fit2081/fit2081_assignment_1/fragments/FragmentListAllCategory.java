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
import com.fit2081.fit2081_assignment_1.adapters.CategoryListRecyclerAdapter;
import com.fit2081.fit2081_assignment_1.models.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListAllCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListAllCategory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    CategoryListRecyclerAdapter adapter;
    ArrayList<EventCategory> categoryList;
    Gson gson = new Gson();

    public FragmentListAllCategory() {
        // Required empty public constructor
    }

//    public void setAdapter(ListViewRecyclerAdapter adapter) {
//        this.adapter = adapter;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentListCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListAllCategory newInstance(String param1, String param2) {
        FragmentListAllCategory fragment = new FragmentListAllCategory();
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
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // Set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        restoreListData();
        // Create the adapter with the shared pref list data
        adapter = new CategoryListRecyclerAdapter(categoryList);
        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void restoreListData(){
        // Grab the array list stored as String in SharedPreferences
        String arrayListStringRestored = new SharedPrefRestore(getActivity()).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
        // Convert the restored string back to ArrayList
        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
        categoryList = gson.fromJson(arrayListStringRestored,type);

        // Initializes a category list if it has not been
        Log.d("restore", String.format("list restored at fragment: %s",categoryList));
    }

    public void notifyAdapter() {
        if (adapter != null) {
            restoreListData();
            // Update the data in the adapter
            adapter.updateData(categoryList);
            adapter.notifyDataSetChanged();
            Log.d("adapter", "Adapter notified");
        }
    }

    public void deleteListData(){
        // Clear the list of categories
        categoryList.clear();

        // Save the empty list to SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(EventCategorySharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String emptyListJson = gson.toJson(categoryList);
        editor.putString(EventCategorySharedPref.KEY_CATEGORY_LIST, emptyListJson);
        editor.apply();
        Log.d("list", String.format("category list data cleared, current list: %s",categoryList));

        // Update the adapter with the new empty list
        notifyAdapter();
    }
}