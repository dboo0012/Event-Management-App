package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginButtonOnClick(View view) {
        EditText findUsername = findViewById(R.id.loginUsername);
        String username = findUsername.getText().toString();
        EditText findPassword = findViewById(R.id.loginPassword);
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
                findUsername.setText("");
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