package com.fit2081.fit2081_assignment_1;

import androidx.appcompat.app.AppCompatActivity;

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
    
    public void signUpButtonOnClick(View view){
        EditText findUsername = findViewById(R.id.signUpUsername);
        EditText findPassword = findViewById(R.id.signUpPassword);

        String username = findUsername.getText().toString();
        String password = findPassword.getText().toString();

        // Check if both fields are filled
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill both username and password fields", Toast.LENGTH_SHORT).show();
        } else {
            String out = String.format("Username: %s has been registered\nPassword: %s", username, password);
            Toast.makeText(this, out, Toast.LENGTH_SHORT).show();
        }
    }
}