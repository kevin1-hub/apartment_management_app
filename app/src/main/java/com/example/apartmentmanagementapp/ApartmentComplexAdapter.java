// ApartmentComplexAdapter.java

package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;  // Don't forget to import ViewGroup
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ApartmentComplexAdapter extends ArrayAdapter<String> {

    private final ManageApartmentComplexActivity context;
    private final ArrayList<String> apartmentList;

    public ApartmentComplexAdapter(ManageApartmentComplexActivity context, ArrayList<String> apartmentList) {
        super(context, R.layout.list_item_apartment_complex, apartmentList);
        this.context = context;
        this.apartmentList = apartmentList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout if it's not provided
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_apartment_complex, parent, false);
        }

        // Get the view elements
        TextView complexNameText = convertView.findViewById(R.id.complexNameText);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        // Set the complex name in the TextView
        complexNameText.setText(apartmentList.get(position));

        // Set the Edit button functionality
        btnEdit.setOnClickListener(v -> {
            String selectedComplex = apartmentList.get(position);
            Intent intent = new Intent(context, EditApartmentComplexActivity.class);
            intent.putExtra("complexName", selectedComplex);
            context.startActivity(intent);
        });

        // Set the Delete button functionality
        btnDelete.setOnClickListener(v -> context.confirmDelete(position));

        return convertView;
    }
}
