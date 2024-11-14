package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditEstateActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText estateNameEditText;
    private String originalEstateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_estate);

        estateNameEditText = findViewById(R.id.estateNameEditText);
        Button saveButton = findViewById(R.id.saveButton);

        databaseHelper = DatabaseHelper.getInstance(this);

        originalEstateName = getIntent().getStringExtra("estateName");
        estateNameEditText.setText(originalEstateName);

        saveButton.setOnClickListener(v -> saveUpdatedEstate());
    }

    private void saveUpdatedEstate() {
        String newEstateName = estateNameEditText.getText().toString().trim();
        if (!newEstateName.isEmpty()) {
            boolean isUpdated = databaseHelper.updateEstate(originalEstateName, newEstateName);
            if (isUpdated) {
                Toast.makeText(this, "Estate updated successfully.", Toast.LENGTH_SHORT).show();

                // Send back the result to ManageEstatesActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("estateUpdated", true); // You can send more data if needed
                setResult(RESULT_OK, resultIntent);
                finish(); // Close EditEstateActivity and go back to ManageEstatesActivity
            } else {
                Toast.makeText(this, "Failed to update estate.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Estate name cannot be empty.", Toast.LENGTH_SHORT).show();
        }
    }
}
