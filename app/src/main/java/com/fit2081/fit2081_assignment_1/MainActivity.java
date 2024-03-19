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
    public static final String LOG_KEY = "Launched";
    EditText findUsername;
    EditText findPassword;
    EditText findConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign the username and password values
        findUsername = findViewById(R.id.signUpUsername);
        findPassword = findViewById(R.id.signUpPassword);
        findConfirmPassword = findViewById(R.id.signUpConfirmPassword);

        // Debugging
        Log.d(LOG_KEY, "launched main activity");

    }

    public void signUpButtonOnClick(View view){
        // Parsing the username and password of the EditText fields (signUp activity)
        String username = findUsername.getText().toString();
        String password = findPassword.getText().toString();
        String confirmPassword = findConfirmPassword.getText().toString();

        // Check if both fields are filled
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both username and password fields", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        } else {
            // Save new user sign up credentials to shared preferences
            saveDataToSharedPreferences(username, password);
            String out = String.format("Successfully registered %s!", username);
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
            // Go to login activity
            launchLogin(view);
            Log.d(LOG_KEY, "saved data to user shared preferences");
        }
    }

    public void launchLogin(View view){
        Intent loginIntent = new Intent(this, LoginActivity.class); // Points to the login activity
        startActivity(loginIntent); // Starts the new intent
    }

    public void saveDataToSharedPreferences(String username, String password){
        // Initialise shared preference class variable to access persistent storage
        SharedPreferences sharedPreferences = getSharedPreferences(UserSharedPref.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Open the shared preference editor

        // Update the values of the shared preference
        editor.putString(UserSharedPref.KEY_USERNAME, username);
        editor.putString(UserSharedPref.KEY_PASSWORD, password);
        editor.apply();

//        findUsername.setText(username);
    }
}