package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.adapters.ListViewRecyclerAdapter;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllCategory;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;
import com.fit2081.fit2081_assignment_1.sharedPreferences.EventCategorySharedPref;
import com.fit2081.fit2081_assignment_1.utilities.SharedPrefRestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewAllCategoryActivity extends AppCompatActivity {
    ArrayList<EventCategory> categoryList;
    Gson gson = new Gson();
    ListViewRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_category);

        // Grab the array list stored as String in SharedPreferences
//        String arrayListStringRestored = new SharedPrefRestore(this).restoreData(EventCategorySharedPref.FILE_NAME, EventCategorySharedPref.KEY_CATEGORY_LIST);
//        // Convert the restored string back to ArrayList
//        Type type = new TypeToken<ArrayList<EventCategory>>() {}.getType();
//        categoryList = gson.fromJson(arrayListStringRestored,type);
//
//        adapter = new ListViewRecyclerAdapter(categoryList);

        // Display the list of categories using a fragment
        FragmentListAllCategory fragment = new FragmentListAllCategory();
//        fragment.setAdapter(adapter);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewAllCategory, fragment).commit(); // Set the adapter to the fragment
        Log.d("viewAllCategory", "launched view all category activity");
    }
}