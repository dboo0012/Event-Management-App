package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String LOG_KEY = "Launched";
    EditText findUsername;
    EditText findPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_KEY, "launched main activity");

        // Assign the username and password values
        findUsername = findViewById(R.id.signUpUsername);
        findPassword = findViewById(R.id.signUpPassword);
    }

    public void signUpButtonOnClick(View view){
        String username = findUsername.getText().toString();
        String password = findPassword.getText().toString();

        // Check if both fields are filled
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both username and password fields", Toast.LENGTH_SHORT).show();
        } else {
            saveDataToSharedPreferences(username, password);
            String out = String.format("Successfully registered %s!", username);
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
            // Go to login page
            launchLogin(view);
        }
    }

    public void launchLogin(View view){
        Intent loginIntent = new Intent(this, LoginActivity.class); // Points to the login activity
        startActivity(loginIntent);
    }

    public void saveDataToSharedPreferences(String username, String password){
        // Shared prefereces (test ONLY)
        SharedPreferences sharedPreferences = getSharedPreferences("DatabaseStore.java", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Create a static key
        editor.putString(DatabaseStore.KEY_USERNAME, username);
        editor.putString(DatabaseStore.KEY_PASSWORD, password);
        editor.apply();
        // Do set text to save previous
//        findUsername.setText(username);
    }
}