package com.example.apartmentmanagementapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditApartmentComplexActivity extends AppCompatActivity {

    EditText etComplexName, etFloors, etParkingSlots;
    SwitchCompat swSecurityPersonnel, swCCTV;
    Button btnSave;
    DatabaseHelper db;
    String originalComplexName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apartment_complex);

        db = new DatabaseHelper(this);

        // Initialize the views
        etComplexName = findViewById(R.id.etComplexName);
        etFloors = findViewById(R.id.etFloors);
        etParkingSlots = findViewById(R.id.etParkingSlots);
        swSecurityPersonnel = findViewById(R.id.swSecurityPersonnel);
        swCCTV = findViewById(R.id.swCCTV);
        btnSave = findViewById(R.id.btnSave);

        // Get the complex name passed from the previous activity
        originalComplexName = getIntent().getStringExtra("complexName");

        if (originalComplexName != null) {
            loadApartmentData(originalComplexName);
        } else {
            Toast.makeText(this, "No complex name provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnSave.setOnClickListener(v -> saveApartmentComplex());
    }

    private void loadApartmentData(String complexName) {
        ApartmentComplex complex = db.getApartmentComplexDetails(complexName);
        if (complex != null) {
            etComplexName.setText(complex.getName());
            etFloors.setText(String.valueOf(complex.getFloors()));
            etParkingSlots.setText(String.valueOf(complex.getParkingSlots()));
            swSecurityPersonnel.setChecked(complex.isSecurityPersonnel());
            swCCTV.setChecked(complex.isCCTV());
        } else {
            Toast.makeText(this, "No data found for this complex", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveApartmentComplex() {
        // Get values from input fields
        String newName = etComplexName.getText().toString().trim();

        // Validate input for floors and parking slots
        if (newName.isEmpty() || etFloors.getText().toString().isEmpty() || etParkingSlots.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int floors;
        int parkingSlots;

        try {
            floors = Integer.parseInt(etFloors.getText().toString());
            parkingSlots = Integer.parseInt(etParkingSlots.getText().toString());

            if (floors < 0 || parkingSlots < 0) {
                Toast.makeText(this, "Floors and parking slots must be non-negative", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean securityPersonnel = swSecurityPersonnel.isChecked();
        boolean cctv = swCCTV.isChecked();  // Make sure this value is correctly retrieved
        Log.d("DatabaseDebug", "CCTV checked: " + cctv);


        // Save to database
        boolean result = db.updateApartmentComplex(originalComplexName, newName, floors, parkingSlots, securityPersonnel, cctv);

        if (result) {
            Toast.makeText(this, "Apartment complex updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating complex", Toast.LENGTH_SHORT).show();
        }
    }
}
