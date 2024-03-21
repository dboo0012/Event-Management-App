package com.fit2081.fit2081_assignment_1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.sharedPreferences.UserSharedPref;

public class LoginActivity extends AppCompatActivity {
    String key = MainActivity.LOG_KEY;
    EditText findUsername;
    EditText findPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assign the username and password values
        findUsername = findViewById(R.id.loginUsername);
        findPassword = findViewById(R.id.loginPassword);

        // Load shared preferences here to prefill username
        findUsername.setText(fetchDataFromSharedPreferences(UserSharedPref.FILE_NAME, UserSharedPref.KEY_USERNAME));

        // Debugging
        Log.d(key, "launched Login activity");
    }

    private String fetchDataFromSharedPreferences(String destination, String key){
        SharedPreferences sharedPreferences = getSharedPreferences(destination, MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void loginButtonOnClick(View view) {
        // Parsing the username and password of the EditText fields (login activity)
        String username = findUsername.getText().toString();
        String password = findPassword.getText().toString();

        // Verification process
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both username and password fields", Toast.LENGTH_SHORT).show();
        } else {
            // verify that password and username matches that of persistent storage
            boolean isVerified = verifyCredentials(username, password);

            if (isVerified) {
                // Username and password verified
                String out = "Successfully logged in!";
                Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
                // Go to dashboard
                Intent dashboardIntent = new Intent(this, DashboardActivity.class); // Points to the dahsboard activity
                startActivity(dashboardIntent);
            } else {
                Toast.makeText(this, "Authentication failure: Username or Password incorrect", Toast.LENGTH_SHORT).show();
                // Reset the username and password fields
                findPassword.setText("");
            }
        }
    }

    private boolean verifyCredentials(String username, String password){
        // Verify the user with the sharedPreferences
        return username.equals(fetchDataFromSharedPreferences(UserSharedPref.FILE_NAME, UserSharedPref.KEY_USERNAME))
                && password.equals(fetchDataFromSharedPreferences(UserSharedPref.FILE_NAME, UserSharedPref.KEY_PASSWORD));
    }
}