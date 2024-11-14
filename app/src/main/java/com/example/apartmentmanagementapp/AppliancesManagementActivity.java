package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AppliancesManagementActivity extends AppCompatActivity {

    private EditText editTextApplianceName;
    private Spinner spinnerApplianceType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appliances_management);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        editTextApplianceName = findViewById(R.id.editTextApplianceName);
        spinnerApplianceType = findViewById(R.id.spinnerApplianceType);

        // Set up spinner with appliance types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.appliance_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApplianceType.setAdapter(adapter);

        // Add appliance button
        Button buttonAddAppliance = findViewById(R.id.buttonAddAppliance);
        buttonAddAppliance.setOnClickListener(v -> addAppliance());

        // Manage appliances button to go to ManageAppliancesActivity
        Button buttonManageAppliances = findViewById(R.id.buttonManageAppliances);
        buttonManageAppliances.setOnClickListener(v -> {
            Intent intent = new Intent(AppliancesManagementActivity.this, ManageAppliancesActivity.class);
            startActivity(intent);
        });
    }

    private void addAppliance() {
        String name = editTextApplianceName.getText().toString().trim();
        String type = spinnerApplianceType.getSelectedItem().toString();

        if (name.isEmpty()) {
            editTextApplianceName.setError("Please enter appliance name");
            return;
        }

        // Add appliance to database
        dbHelper.addAppliance(name, type);
        Toast.makeText(this, "Appliance added successfully!", Toast.LENGTH_SHORT).show();

        // Clear input for the next entry
        editTextApplianceName.setText("");
        spinnerApplianceType.setSelection(0);
    }
}
