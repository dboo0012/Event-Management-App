package com.fit2081.fit2081_assignment_1.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fit2081.fit2081_assignment_1.R;

public class WebViewActivity extends AppCompatActivity {
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
}