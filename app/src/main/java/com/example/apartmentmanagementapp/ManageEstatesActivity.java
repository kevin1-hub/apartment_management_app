package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ManageEstatesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout estatesLayout;
    private TextView noRecordsMessage; // Declare the TextView for no records message

    // Declare the ActivityResultLauncher
    private ActivityResultLauncher<Intent> editEstateLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_estates);

        estatesLayout = findViewById(R.id.estatesLayout);
        noRecordsMessage = findViewById(R.id.noRecordsMessage); // Initialize the TextView
        databaseHelper = DatabaseHelper.getInstance(this);

        loadEstates();

        // Initialize the ActivityResultLauncher for handling results from EditEstateActivity
        editEstateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Estate was updated, reload the estates list
                        loadEstates();
                    }
                }
        );
    }

    private void loadEstates() {
        try {
            List<String> estateList = databaseHelper.getAllEstates();

            // Clear existing views
            estatesLayout.removeAllViews();

            if (estateList != null && !estateList.isEmpty()) {
                noRecordsMessage.setVisibility(View.GONE); // Hide no records message

                for (String estateName : estateList) {
                    View estateView = getLayoutInflater().inflate(R.layout.estate_item, estatesLayout, false);

                    TextView estateTextView = estateView.findViewById(R.id.estateNameTextView);
                    Button editButton = estateView.findViewById(R.id.editButton);
                    Button deleteButton = estateView.findViewById(R.id.deleteButton);

                    estateTextView.setText(estateName);

                    editButton.setOnClickListener(v -> openEditEstateActivity(estateName));

                    deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(estateName));

                    estatesLayout.addView(estateView);
                }
            } else {
                noRecordsMessage.setVisibility(View.VISIBLE); // Show no records message
            }
        } catch (Exception e) {
            // Log the exception message and stack trace
            Log.e("ManageEstatesActivity", "Error loading estates: " + e.getMessage(), e);
            Toast.makeText(this, "Error loading estates. Check logs for details.", Toast.LENGTH_LONG).show();
        }
    }

    private void openEditEstateActivity(String estateName) {
        Intent intent = new Intent(this, EditEstateActivity.class);
        intent.putExtra("estateName", estateName);
        // Launch EditEstateActivity and wait for the result
        editEstateLauncher.launch(intent);
    }

    private void showDeleteConfirmationDialog(String estateName) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Estate")
                .setMessage("Are you sure you want to delete this estate?")
                .setPositiveButton("Yes", (dialog, which) -> deleteEstate(estateName))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteEstate(String estateName) {
        boolean isDeleted = databaseHelper.deleteEstate(estateName);

        if (isDeleted) {
            Toast.makeText(this, "Estate deleted successfully.", Toast.LENGTH_SHORT).show();

            // Find the estate view to remove it from the layout
            View estateViewToRemove = null;
            for (int i = 0; i < estatesLayout.getChildCount(); i++) {
                View estateView = estatesLayout.getChildAt(i);
                TextView estateTextView = estateView.findViewById(R.id.estateNameTextView);
                String currentEstateName = estateTextView.getText().toString();
                if (currentEstateName.equals(estateName)) {
                    estateViewToRemove = estateView;
                    break;
                }
            }

            // Remove the view from the layout if found
            if (estateViewToRemove != null) {
                estatesLayout.removeView(estateViewToRemove);
            }

            // Check if the layout is empty after deletion
            if (estatesLayout.getChildCount() == 0) {
                noRecordsMessage.setVisibility(View.VISIBLE); // Show no records message
            }
        } else {
            Toast.makeText(this, "Failed to delete estate.", Toast.LENGTH_SHORT).show();
        }
    }
}
