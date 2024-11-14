package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApartmentEstateManagementActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private EditText estateNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_estate_management);

        databaseHelper = DatabaseHelper.getInstance(this);
        estateNameInput = findViewById(R.id.estateNameInput);

        Button buttonAddEstate = findViewById(R.id.buttonAddEstate);
        Button buttonManageEstates = findViewById(R.id.buttonManageEstates);

        buttonAddEstate.setOnClickListener(v -> addEstate());
        buttonManageEstates.setOnClickListener(v -> manageEstates());
    }

    private void addEstate() {
        String estateName = estateNameInput.getText().toString().trim();

        if (!estateName.isEmpty()) {
            // Check if an estate already exists
            if (databaseHelper.getEstateCount() >= 1) {
                Toast.makeText(this, "Only one estate record is allowed.", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = databaseHelper.addEstate(estateName);
            if (result != -1) {
                Toast.makeText(this, getString(R.string.estate_added_success), Toast.LENGTH_SHORT).show();
                estateNameInput.setText("");
            } else {
                Toast.makeText(this, getString(R.string.error_adding_estate), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.empty_estate_name), Toast.LENGTH_SHORT).show();
        }
    }

    private void manageEstates() {
        Intent intent = new Intent(this, ManageEstatesActivity.class);
        startActivity(intent);
    }
}
