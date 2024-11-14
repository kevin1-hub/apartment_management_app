package com.example.apartmentmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ApartmentAdapter extends ArrayAdapter<String> {

    private final List<String> complexNames;
    private final List<Integer> floorsList;
    private final Context context;

    public ApartmentAdapter(Context context, List<String> complexNames, List<Integer> floorsList) {
        super(context, 0, complexNames);
        this.context = context;
        this.complexNames = complexNames;
        this.floorsList = floorsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.apartment_item, parent, false);
        }

        TextView complexNameText = convertView.findViewById(R.id.complexNameText);
        Spinner floorSpinner = convertView.findViewById(R.id.floorSpinner);

        complexNameText.setText(complexNames.get(position));

        // Generate floors based on the floor count for each complex
        int floors = floorsList.get(position);
        List<String> floorOptions = new ArrayList<>();
        for (int i = 1; i <= floors; i++) {
            floorOptions.add(String.valueOf(i));
        }

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, floorOptions);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        return convertView;
    }
}
