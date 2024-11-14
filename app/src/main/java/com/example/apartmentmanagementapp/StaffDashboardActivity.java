package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;


public class StaffDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
    }

    public void onAssignedMaintenanceTasksClick(View view) {
        // Handle the Apartment Complex Management click
        Intent intent = new Intent(this, AssignedMaintenanceTasksActivity.class);
        startActivity(intent);
    }


    public void onReportMaintenanceIssueClick(View view) {
        // Handle the Apartment Complex Management click
        Intent intent = new Intent(this, ReportMaintenanceIssueActivity.class);
        startActivity(intent);
    }

    public void onSubmitWorkCompletionClick(View view) {
        // Handle the Apartment Management click
        Intent intent = new Intent(this, SubmitWorkCompletionActivity.class);
        startActivity(intent);
    }


    public void onViewNotificationsClick(View view) {
        // Handle the Rent Payment Management click
        Intent intent = new Intent(this, ViewNotificationsActivity.class);
        startActivity(intent);
    }


}

