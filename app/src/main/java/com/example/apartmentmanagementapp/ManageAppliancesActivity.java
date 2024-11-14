package com.example.apartmentmanagementapp;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ManageAppliancesActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ApplianceAdapter adapter;
    private ListView listViewAppliances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_appliances);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI components
        listViewAppliances = findViewById(R.id.listViewAppliances);

        // Load appliances and set up adapter
        loadAppliances();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload appliances data when returning to this activity
        loadAppliances();
    }

    private void loadAppliances() {
        // Load appliances from the database
        ArrayList<Appliance> applianceList = dbHelper.getAllAppliances();

        // If the adapter is null, create a new one; otherwise, just update the data
        if (adapter == null) {
            adapter = new ApplianceAdapter(this, applianceList, dbHelper);
            listViewAppliances.setAdapter(adapter);
        } else {
            // Update the adapter data and refresh ListView
            adapter.clear();
            adapter.addAll(applianceList);
            adapter.notifyDataSetChanged();
        }
    }
}
