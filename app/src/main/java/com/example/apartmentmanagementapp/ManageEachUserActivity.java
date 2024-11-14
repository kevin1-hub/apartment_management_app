package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ManageEachUserActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LinearLayout usersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_each_user);

        usersContainer = findViewById(R.id.usersContainer);
        db = new DatabaseHelper(this);

        loadUsers();
    }

    private void loadUsers() {
        usersContainer.removeAllViews(); // Clear existing views before loading new users

        List<User> userList = db.getAllUsers();
        for (User user : userList) {
            addUserEntry(user);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers(); // Reload users to reflect any updates made
    }

    private void addUserEntry(User user) {
        LinearLayout userLayout = new LinearLayout(this);
        userLayout.setOrientation(LinearLayout.VERTICAL);
        userLayout.setPadding(10, 10, 10, 10);

        TextView nameTextView = new TextView(this);
        nameTextView.setText(getString(R.string.user_name, user.getFirstName(), user.getLastName()));

        TextView roleTextView = new TextView(this);
        roleTextView.setText(getString(R.string.user_role, user.getRole()));

        // Edit button to navigate to EditUserActivity
        Button editButton = new Button(this);
        editButton.setText(R.string.edit_user);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageEachUserActivity.this, EditUserActivity.class);
            intent.putExtra("USER_ID", user.getId()); // Pass the user ID to EditUserActivity
            startActivity(intent);
        });

        // Delete button to remove user entry
        Button deleteButton = new Button(this);
        deleteButton.setText(R.string.delete_user);
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(user));

        userLayout.addView(nameTextView);
        userLayout.addView(roleTextView);
        userLayout.addView(editButton);
        userLayout.addView(deleteButton);

        usersContainer.addView(userLayout);
    }

    private void showDeleteConfirmationDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Yes", (dialog, which) -> deleteUser(user))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss()) // Use lambda instead of method reference
                .show();
    }

    private void deleteUser(User user) {
        boolean isDeleted = db.deleteUser(user.getId()); // Ensure db.deleteUser is implemented in DatabaseHelper
        if (isDeleted) {
            Toast.makeText(this, "User deleted successfully.", Toast.LENGTH_SHORT).show();
            loadUsers(); // Refresh the list after deletion
        } else {
            Toast.makeText(this, "Failed to delete user.", Toast.LENGTH_SHORT).show();
        }
    }
}
