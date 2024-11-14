package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ApartmentComplexManagementActivity extends AppCompatActivity {

    EditText etComplexName, etFloors, etParkingSlots;
    SwitchCompat swSecurityPersonnel, swCCTV;
    Button btnSave, btnManageComplex;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_complex_management);

        // Initialize the views
        db = new DatabaseHelper(this);
        etComplexName = findViewById(R.id.etComplexName);
        etFloors = findViewById(R.id.etFloors);
        etParkingSlots = findViewById(R.id.etParkingSlots);
        swSecurityPersonnel = findViewById(R.id.swSecurityPersonnel);
        swCCTV = findViewById(R.id.swCCTV);
        btnSave = findViewById(R.id.btnSave);
        btnManageComplex = findViewById(R.id.btnManageComplex);

        // Handle the save button click
        btnSave.setOnClickListener(v -> saveApartmentComplex());

        // Handle the manage complex button click
        btnManageComplex.setOnClickListener(v -> {
            Intent intent = new Intent(ApartmentComplexManagementActivity.this, ManageApartmentComplexActivity.class);
            startActivity(intent);
        });
    }

    // Method to save apartment complex data
    // Method to save apartment complex data
    private void saveApartmentComplex() {
        String complexName = etComplexName.getText().toString();
        int floors = Integer.parseInt(etFloors.getText().toString());
        int parkingSlots = Integer.parseInt(etParkingSlots.getText().toString());
        String securityPersonnel = swSecurityPersonnel.isChecked() ? "Yes" : "No";
        String cctvPresent = swCCTV.isChecked() ? "Yes" : "No";

        try {
            boolean result = db.insertApartmentComplex(complexName, floors, parkingSlots, securityPersonnel, cctvPresent);
            if (result) {
                Toast.makeText(this, "Apartment Complex Saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting apartment complex", e);
            Toast.makeText(this, "An error occurred while saving data", Toast.LENGTH_SHORT).show();
        }
    }

}
