package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        fetchDataFromSharedPreferences();

        // Debugging
        Log.d(key, "launched Login activity");
    }

    private String getStoredUsername(){
        // Initialise shared preference variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("DatabaseStore.java", MODE_PRIVATE);

        // Get the username key from sharedPreferences
        return sharedPreferences.getString(UserSharedPref.KEY_USERNAME, "");
    }

    private String getStoredPassword(){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("DatabaseStore.java", MODE_PRIVATE);

        // Get the username key from sharedPreferences
        return sharedPreferences.getString(UserSharedPref.KEY_PASSWORD, "");
    }

    public void fetchDataFromSharedPreferences(){
        // Prefill the username field by getting shared preference username value
        findUsername.setText(getStoredUsername());
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
                Toast.makeText(this, "Oops! Username or Password incorrect.", Toast.LENGTH_SHORT).show();
                // Reset the username and password fields
                findPassword.setText("");
            }
        }
    }

    private boolean verifyCredentials(String username, String password){
        // Verify the user with the sharedPreferences
        return username.equals(getStoredUsername()) && password.equals(getStoredPassword());
    }
}