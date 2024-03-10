package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchLogin(View view){
        Intent loginIntent = new Intent(this, LoginActivity.class); // Points to the login activity
        startActivity(loginIntent);
    }

    public void signUpButtonOnClick(View view){
        EditText findUsername = findViewById(R.id.signUpUsername);
        String username = findUsername.getText().toString();
        EditText findPassword = findViewById(R.id.signUpPassword);
        String password = findPassword.getText().toString();

        // Check if both fields are filled
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both username and password fields", Toast.LENGTH_SHORT).show();
        } else {
            String out = String.format("Successfully registered %s!", username);
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
            // Go to login page
            launchLogin(view);
        }
    }
}