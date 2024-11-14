package com.example.apartmentmanagementapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ManageApartmentComplexActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> apartmentList;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_apartment_complex);

        listView = findViewById(R.id.listViewApartments);
        db = new DatabaseHelper(this);
        apartmentList = new ArrayList<>();

        loadApartmentComplexes();

        listView.setOnItemClickListener((parent, view, position, id) -> confirmDelete(position)); // Add item click listener
    }

    // Load apartment complexes from the database
    private void loadApartmentComplexes() {
        Cursor cursor = db.getAllApartmentComplexes(); // Make sure this matches the method name in your DatabaseHelper
        apartmentList.clear(); // Clear the list before loading new data

        if (cursor != null) {
            int columnIndex = cursor.getColumnIndex("complex_name");

            while (cursor.moveToNext()) { // Iterate through all rows
                if (columnIndex != -1) {
                    apartmentList.add(cursor.getString(columnIndex));
                }
            }
            cursor.close(); // Don't forget to close the cursor
        }

        // Set the custom adapter after loading data
        ApartmentComplexAdapter adapter = new ApartmentComplexAdapter(this, apartmentList);
        listView.setAdapter(adapter);
    }

    // Confirm deletion with an AlertDialog
    public void confirmDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Apartment Complex")
                .setMessage("Are you sure you want to delete this apartment complex?")
                .setPositiveButton("Yes", (dialog, which) -> deleteApartmentComplex(position))
                .setNegativeButton("No", null)
                .show();
    }

    // Delete the selected apartment complex from the database and update the list
    public void deleteApartmentComplex(int position) {
        String complexName = apartmentList.get(position);
        boolean result = db.deleteApartmentComplex(complexName);

        if (result) {
            apartmentList.remove(position); // Remove from the list
            ((ApartmentComplexAdapter) listView.getAdapter()).notifyDataSetChanged();  // Notify the adapter to refresh the list view
            Toast.makeText(this, "Apartment Complex Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error deleting complex", Toast.LENGTH_SHORT).show();
        }
    }
}
