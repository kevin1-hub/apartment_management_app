package com.example.apartmentmanagementapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditAppliancesActivity extends AppCompatActivity {

    private EditText editTextApplianceName;
    private Spinner spinnerApplianceType;
    private DatabaseHelper dbHelper;
    private int applianceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appliances);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Get appliance ID from intent
        applianceId = getIntent().getIntExtra("appliance_id", -1);

        // Initialize UI components
        editTextApplianceName = findViewById(R.id.editTextApplianceName);
        spinnerApplianceType = findViewById(R.id.spinnerApplianceType);

        // Set up spinner with appliance types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appliance_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApplianceType.setAdapter(adapter);

        // Load appliance details for editing
        loadApplianceDetails();

        // Save button click listener
        Button buttonSave = findViewById(R.id.buttonSave); // Local variable now
        buttonSave.setOnClickListener(v -> saveAppliance());
    }

    private void loadApplianceDetails() {
        // Get appliance from the database by ID
        Appliance appliance = dbHelper.getAppliance(applianceId);
        if (appliance != null) {
            // Load appliance name and type into the UI
            editTextApplianceName.setText(appliance.getName());
            String type = appliance.getType();

            // Get the spinner items (appliance types)
            String[] applianceTypes = getResources().getStringArray(R.array.appliance_types);

            // Find the position of the appliance type in the array
            int position = -1;
            for (int i = 0; i < applianceTypes.length; i++) {
                if (applianceTypes[i].equals(type)) {
                    position = i;
                    break;
                }
            }

            // Set the spinner selection to the correct position
            if (position != -1) {
                spinnerApplianceType.setSelection(position);
            } else {
                Toast.makeText(this, "Appliance type not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Appliance not found!", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveAppliance() {
        String name = editTextApplianceName.getText().toString().trim();
        String type = spinnerApplianceType.getSelectedItem().toString();

        if (name.isEmpty()) {
            editTextApplianceName.setError("Please enter appliance name");
            return;
        }

        // Check if appliance ID is valid
        if (applianceId != -1) {
            // Update appliance in the database
            boolean isUpdated = dbHelper.updateAppliance(applianceId, name, type);

            if (isUpdated) {
                Toast.makeText(this, "Appliance updated successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Close activity and return to manage screen
            } else {
                Toast.makeText(this, "Error updating appliance!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error updating appliance!", Toast.LENGTH_SHORT).show();
        }
    }
}
