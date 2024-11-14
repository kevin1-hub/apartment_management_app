package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
    }

    public void onApartmentEstateManageClick(View view) {
        // Handle the Apartment Complex Management click
        Intent intent = new Intent(this, ApartmentEstateManagementActivity.class);
        startActivity(intent);
    }


    public void onApartmentComplexManageClick(View view) {
        // Handle the Apartment Complex Management click
        Intent intent = new Intent(this, ApartmentComplexManagementActivity.class);
        startActivity(intent);
    }

    public void onApartmentManageClick(View view) {
        // Handle the Apartment Management click
        Intent intent = new Intent(this, ApartmentManagementActivity.class);
        startActivity(intent);
    }

    public void onTenantManageClick(View view) {
        // Handle the Tenant Management click
        Intent intent = new Intent(this, TenantManagementActivity.class);
        startActivity(intent);
    }

    public void onRentPaymentManageClick(View view) {
        // Handle the Rent Payment Management click
        Intent intent = new Intent(this, RentPaymentManagementActivity.class);
        startActivity(intent);
    }

    public void onMaintenanceRequestManageClick(View view) {
        // Handle the Maintenance Request Management click
        Intent intent = new Intent(this, MaintenanceRequestManagementActivity.class);
        startActivity(intent);
    }

    public void onUserManageClick(View view) {
        // Handle the User Management click
        Intent intent = new Intent(this, UserManagementActivity.class);
        startActivity(intent);
    }

    public void onLeaseAgreementManageClick(View view) {
        // Handle the Lease Agreement Management click
        Intent intent = new Intent(this, LeaseAgreementManagementActivity.class);
        startActivity(intent);
    }

    public void onLeaseTerminationManageClick(View view) {
        // Handle the Lease Termination Requests click
        Intent intent = new Intent(this, LeaseTerminationRequestsActivity.class);
        startActivity(intent);
    }

    public void onReportsManageClick(View view) {
        // Handle the Reports and Analytics click
        Intent intent = new Intent(this, ReportsAnalyticsActivity.class);
        startActivity(intent);
    }


    public void onAppliancesManageClick(View view) {
        // Handle the Reports and Analytics click
        Intent intent = new Intent(this, AppliancesManagementActivity.class);
        startActivity(intent);
    }

    public void onSettingsManageClick(View view) {
        // Handle the Settings click
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
