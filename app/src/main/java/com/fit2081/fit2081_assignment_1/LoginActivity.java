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
    public static String LOG_KEY = "Launched";
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
        getDataFromSharedPreferences();

        // Debugging
        Log.d(LOG_KEY, "launched main activity");
    }

    public void getDataFromSharedPreferences(){
        // initialise shared preference class variable to access Android's persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences("DatabaseStore.java", MODE_PRIVATE);

        // save key-value pairs to the shared preference file
        String studentNameRestored = sharedPreferences.getString(DatabaseStore.KEY_USERNAME, "");

        // update the UI using retrieved values
        findUsername.setText(studentNameRestored);
    }

    public void loginButtonOnClick(View view) {
        // Parsing the username and password
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

    public boolean verifyCredentials(String username, String password){
        boolean isVerifiedUser = false;

        // Verify the user with the database

        return isVerifiedUser;
    }
}