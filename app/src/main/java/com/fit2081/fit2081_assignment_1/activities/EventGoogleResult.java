package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fit2081.fit2081_assignment_1.R;

public class EventGoogleResult extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);

        // toolbar
        Toolbar toolbar = findViewById(R.id.web_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Google Search Results");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        webView = findViewById(R.id.webView);

        String eventName = getIntent().getExtras().getString("eventName");

        String eventSearchURL = "https://www.google.com/search?q=" + eventName;

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(eventSearchURL);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        // If the "up" button is clicked, finish the current activity
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}