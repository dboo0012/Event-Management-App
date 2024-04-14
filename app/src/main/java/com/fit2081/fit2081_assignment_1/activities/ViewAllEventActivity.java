package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.fragments.FragmentListAllEvent;

public class ViewAllEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_event);
        FragmentListAllEvent fragment = new FragmentListAllEvent();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewAllEvent, fragment).commit();
    }
}