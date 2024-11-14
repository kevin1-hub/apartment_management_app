package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);

        // Handle login button click
        loginButton.setOnClickListener(view -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            // Get the singleton instance of DatabaseHelper
            DatabaseHelper databaseHelper = DatabaseHelper.getInstance(LoginActivity.this);

            // Validate user credentials
            User user = databaseHelper.validateUser(email, password);
            if (user != null) {
                // Redirect to the appropriate dashboard based on the role
                Intent intent;
                switch (user.getRole()) {
                    case "Admin":
                        intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                        break;
                    case "Staff":
                        intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                        break;
                    case "Caretaker":
                        intent = new Intent(LoginActivity.this, CaretakerDashboardActivity.class);
                        break;
                    default:
                        Toast.makeText(LoginActivity.this, "Role not recognized", Toast.LENGTH_SHORT).show();
                        return;
                }
                intent.putExtra("user_email", email); // Pass the user's email
                startActivity(intent);
                finish();
            } else {
                // Show error message if login fails
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
