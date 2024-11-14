package com.example.apartmentmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

public class ApplianceAdapter extends ArrayAdapter<Appliance> {

    private final DatabaseHelper dbHelper;

    public ApplianceAdapter(Context context, ArrayList<Appliance> appliances, DatabaseHelper dbHelper) {
        super(context, 0, appliances);
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_appliance, parent, false);
        }

        Appliance appliance = getItem(position);

        TextView applianceName = convertView.findViewById(R.id.textViewApplianceName);
        TextView applianceType = convertView.findViewById(R.id.textViewApplianceType);
        Button buttonEdit = convertView.findViewById(R.id.buttonEdit);
        Button buttonDelete = convertView.findViewById(R.id.buttonDelete);

        assert appliance != null;
        applianceName.setText(appliance.getName());
        applianceType.setText(appliance.getType());

        // Edit button action
        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditAppliancesActivity.class);
            intent.putExtra("appliance_id", appliance.getId());
            getContext().startActivity(intent);
        });

        // Delete button with confirmation dialog
        buttonDelete.setOnClickListener(v -> new AlertDialog.Builder(getContext())
                .setTitle("Delete Appliance")
                .setMessage("Are you sure you want to delete this appliance?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteAppliance(appliance.getId());
                    remove(appliance);
                    notifyDataSetChanged();
                })
                .setNegativeButton("No", null)
                .show());

        return convertView;
    }
}
