package com.example.apartmentmanagementapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
public class ApartmentManagementActivity extends AppCompatActivity {

    private final String TAG = "ApartmentManagement";

    private LinearLayout estatesLayout, furnitureLayout, electronicsLayout;
    private TextView noRecordsMessage;
    private Spinner complexNameSpinner, floorSpinner, furnitureSpinner, electronicsSpinner, statusSpinner; // Added statusSpinner
    private DatabaseHelper databaseHelper;
    private final List<String> selectedFurniture = new ArrayList<>();
    private final List<String> selectedElectronics = new ArrayList<>();
    private TextView furnitureStatusTextView;
    private TextView electronicsStatusTextView;
    private EditText apartmentDescriptionEditText; // Add apartmentDescriptionEditText
    // Add missing field declarations


    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView kitchenImageView, bedroomImageView, livingRoomImageView, bathroomImageView,
            exteriorImageView, diningRoomImageView, balconyImageView, parkingImageView,
            hallwayImageView, storageroomImageView;

    private final Uri bedroomImageUri = null;
    private final Uri livingRoomImageUri = null;
    private final Uri bathroomImageUri = null;
    private final Uri exteriorImageUri = null;
    private final Uri diningRoomImageUri = null;
    private final Uri balconyImageUri = null;
    private final Uri parkingImageUri = null;
    private final Uri hallwayImageUri = null;
    private final Uri storageroomImageUri = null;


    private String currentImageType;// Define currentImageType as String
    private ImageView currentImageView;  // Define currentImageView for the selected ImageView
    private final ImageData imageData = new ImageData();  // Initialize ImageData


    public ApartmentManagementActivity(Uri kitchenImageUri) {
        // Declare image URI fields as final and initialize to null
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_management);

        // Initialize Views
        estatesLayout = findViewById(R.id.estatesLayout);
        noRecordsMessage = findViewById(R.id.noRecordsMessage);
        complexNameSpinner = findViewById(R.id.complexNameSpinner);
        floorSpinner = findViewById(R.id.floorSpinner);
        furnitureSpinner = findViewById(R.id.furnitureSpinner);
        electronicsSpinner = findViewById(R.id.electronicsSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        apartmentDescriptionEditText = findViewById(R.id.apartmentDescriptionEditText);

        // ImageView References
        kitchenImageView = findViewById(R.id.kitchenImageView);
        bedroomImageView = findViewById(R.id.bedroomImageView);
        livingRoomImageView = findViewById(R.id.LivingRoomImageView);
        bathroomImageView = findViewById(R.id.BathroomImageView);
        exteriorImageView = findViewById(R.id.ExteriorImageView);
        diningRoomImageView = findViewById(R.id.DiningRoomImageView);
        balconyImageView = findViewById(R.id.BalconyImageView);
        parkingImageView = findViewById(R.id.parkingImageView);
        hallwayImageView = findViewById(R.id.hallwayImageView);
        storageroomImageView = findViewById(R.id.StorageroomImageView);

        // Database Helper Initialization
        databaseHelper = new DatabaseHelper(this);

        // Set click listeners for image upload buttons
        findViewById(R.id.kitchenImageButton).setOnClickListener(v -> openImagePicker(kitchenImageView, "kitchen"));
        findViewById(R.id.bedroomImageButton).setOnClickListener(v -> openImagePicker(bedroomImageView, "bedroom"));
        findViewById(R.id.LivingRoomImageButton).setOnClickListener(v -> openImagePicker(livingRoomImageView, "livingRoom"));
        findViewById(R.id.BathroomImageButton).setOnClickListener(v -> openImagePicker(bathroomImageView, "bathroom"));
        findViewById(R.id.ExteriorImageButton).setOnClickListener(v -> openImagePicker(exteriorImageView, "exterior"));
        findViewById(R.id.DiningRoomImageButton).setOnClickListener(v -> openImagePicker(diningRoomImageView, "diningRoom"));
        findViewById(R.id.BalconyImageButton).setOnClickListener(v -> openImagePicker(balconyImageView, "balcony"));
        findViewById(R.id.ParkingImageButton).setOnClickListener(v -> openImagePicker(parkingImageView, "parking"));
        findViewById(R.id.HallwayImageButton).setOnClickListener(v -> openImagePicker(hallwayImageView, "hallway"));
        findViewById(R.id.StorageroomImageButton).setOnClickListener(v -> openImagePicker(storageroomImageView, "storageroom"));


        // Load various dropdowns and apartment details
        loadApartmentComplexes();
        loadEstates();
        loadFurnitureAndElectronics();
        loadApartmentDetails();
    }


    private void loadApartmentComplexes() {
        try {
            List<String> complexNames = new ArrayList<>();
            List<Integer> floorsList = new ArrayList<>();

            Cursor cursor = databaseHelper.getComplexNamesAndFloors();
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex("complex_name");
                int floorIndex = cursor.getColumnIndex("floors");

                while (cursor.moveToNext()) {
                    complexNames.add(cursor.getString(nameIndex));
                    floorsList.add(cursor.getInt(floorIndex));
                }
                cursor.close();
            }

            if (complexNames.isEmpty()) {
                noRecordsMessage.setVisibility(View.VISIBLE);
            } else {
                noRecordsMessage.setVisibility(View.GONE);
                setupComplexSpinner(complexNames, floorsList);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading complexes", e);
        }
    }

    private void setupComplexSpinner(List<String> complexNames, List<Integer> floorsList) {
        ArrayAdapter<String> complexAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, complexNames);
        complexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        complexNameSpinner.setAdapter(complexAdapter);

        complexNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int floors = floorsList.get(position);
                loadFloorSpinner(floors);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadFloorSpinner(int floors) {
        List<String> floorOptions = new ArrayList<>();
        for (int i = 1; i <= floors; i++) {
            floorOptions.add("Floor " + i);
        }

        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floorOptions);
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);
    }

    private void loadEstates() {
        try {
            List<String> estateList = databaseHelper.getAllEstates();

            estatesLayout.removeAllViews();

            if (estateList != null && !estateList.isEmpty()) {
                noRecordsMessage.setVisibility(View.GONE);

                for (String estateName : estateList) {
                    TextView estateTextView = new TextView(this);
                    estateTextView.setText(estateName);
                    estateTextView.setTextSize(18);
                    estateTextView.setPadding(10, 10, 10, 10);
                    estatesLayout.addView(estateTextView);
                }
            } else {
                noRecordsMessage.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading estates", e);
        }
    }

    private void loadFurnitureAndElectronics() {
        try {
            List<Appliance> furnitureAppliances = databaseHelper.getAppliancesByType("Furniture");
            List<String> furnitureList = new ArrayList<>();
            furnitureList.add("Please choose furniture"); // Placeholder

            for (Appliance appliance : furnitureAppliances) {
                furnitureList.add(appliance.getName());
            }

            List<Appliance> electronicsAppliances = databaseHelper.getAppliancesByType("Electronics");
            List<String> electronicsList = new ArrayList<>();
            electronicsList.add("Please choose electronics"); // Placeholder

            for (Appliance appliance : electronicsAppliances) {
                electronicsList.add(appliance.getName());
            }

            setupFurnitureSpinner(furnitureList);
            setupElectronicsSpinner(electronicsList);

        } catch (Exception e) {
            Log.e(TAG, "Error loading appliances", e);
        }
    }

    private void setupFurnitureSpinner(List<String> furnitureList) {
        ArrayAdapter<String> furnitureAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, furnitureList);
        furnitureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        furnitureSpinner.setAdapter(furnitureAdapter);

        furnitureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Skip placeholder
                    String selectedFurnitureItem = furnitureList.get(position);
                    if (!selectedFurniture.contains(selectedFurnitureItem)) {
                        selectedFurniture.add(selectedFurnitureItem);

                        // Dynamically add the item view with remove button
                        addFurnitureItemView(selectedFurnitureItem);


                    }

                    // Reset spinner to placeholder
                    furnitureSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupElectronicsSpinner(List<String> electronicsList) {
        ArrayAdapter<String> electronicsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, electronicsList);
        electronicsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        electronicsSpinner.setAdapter(electronicsAdapter);

        electronicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) { // Skip placeholder
                    String selectedElectronicsItem = electronicsList.get(position);
                    if (!selectedElectronics.contains(selectedElectronicsItem)) {
                        selectedElectronics.add(selectedElectronicsItem);

                        addElectronicsItemView(selectedElectronicsItem);
                    }

                    // Reset spinner to placeholder
                    electronicsSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addFurnitureItemView(String furnitureItem) {
        if (selectedFurniture != null) {
            // Create a new LinearLayout to hold the item and the remove button
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(1, 1, 1, 1);

            TextView itemTextView = new TextView(this);
            itemTextView.setText(furnitureItem); // Display the actual furniture item text
            itemTextView.setPadding(1, 1, 1, 1);

            TextView removeButton = new TextView(this);
            removeButton.setText(" X");
            removeButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            removeButton.setPadding(10, 0, 10, 0);
            removeButton.setOnClickListener(v -> {
                // Remove the item from the selectedFurniture list and update the layout
                removeItemFromList(selectedFurniture, furnitureItem);
                furnitureLayout.removeView(itemLayout); // Remove the item layout entirely
                updateFurnitureStatus(); // Update the furniture status text
            });

            itemLayout.addView(itemTextView);
            itemLayout.addView(removeButton);

            // Add itemLayout to the main furnitureLayout
            furnitureLayout.addView(itemLayout);

            updateFurnitureStatus(); // Update the furniture status text after adding an item
        }
    }

    private void addElectronicsItemView(String electronicsItem) {
        if (selectedElectronics != null) {
            // Create a new LinearLayout to hold the item and the remove button
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(1, 1, 1, 1);

            TextView itemTextView = new TextView(this);
            itemTextView.setText(electronicsItem); // Display the actual electronics item text
            itemTextView.setPadding(1, 1, 1, 1);

            // Create the "X" button to remove the item from the layout
            TextView removeButton = new TextView(this);
            removeButton.setText(" X");
            removeButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            removeButton.setPadding(1, 0, 1, 0);
            removeButton.setOnClickListener(v -> {
                // Remove the item from the selectedElectronics list and update the layout
                removeItemFromList(selectedElectronics, electronicsItem);
                electronicsLayout.removeView(itemLayout); // Remove the item layout entirely
                updateElectronicsStatus(); // Update the electronics status text
            });

            itemLayout.addView(itemTextView);
            itemLayout.addView(removeButton);

            electronicsLayout.addView(itemLayout);

            updateElectronicsStatus(); // Update the electronics status text after adding an item
        }
    }

    private void updateFurnitureStatus() {
        // Check if furniture status text is already added to the layout
        if (selectedFurniture.isEmpty()) {
            if (furnitureStatusTextView == null) {
                furnitureStatusTextView = new TextView(this);
                furnitureStatusTextView.setPadding(1, 1, 1, 1);
                furnitureStatusTextView.setText(getString(R.string.selected_no_furniture));
                furnitureLayout.addView(furnitureStatusTextView, 0);  // Add at the top
            } else {
                furnitureStatusTextView.setText(getString(R.string.selected_no_furniture));
            }
        } else {
            if (furnitureStatusTextView == null) {
                furnitureStatusTextView = new TextView(this);
                furnitureStatusTextView.setPadding(1, 1, 1, 1);
                furnitureStatusTextView.setText(getString(R.string.selected_furniture));
                furnitureLayout.addView(furnitureStatusTextView, 0);  // Add at the top
            } else {
                furnitureStatusTextView.setText(getString(R.string.selected_furniture));
            }
        }
    }

    private void updateElectronicsStatus() {
        // Check if electronics status text is already added to the layout
        if (selectedElectronics.isEmpty()) {
            if (electronicsStatusTextView == null) {
                electronicsStatusTextView = new TextView(this);
                electronicsStatusTextView.setPadding(1, 1, 1, 1);
                electronicsStatusTextView.setText(getString(R.string.selected_no_electronics));
                electronicsLayout.addView(electronicsStatusTextView, 0);  // Add at the top
            } else {
                electronicsStatusTextView.setText(getString(R.string.selected_no_electronics));
            }
        } else {
            if (electronicsStatusTextView == null) {
                electronicsStatusTextView = new TextView(this);
                electronicsStatusTextView.setPadding(1, 1, 1, 1);
                electronicsStatusTextView.setText(getString(R.string.selected_electronics));
                electronicsLayout.addView(electronicsStatusTextView, 0);  // Add at the top
            } else {
                electronicsStatusTextView.setText(getString(R.string.selected_electronics));
            }
        }
    }

    private void removeItemFromList(List<String> itemList, String item) {
        if (itemList != null && item != null) {
            itemList.remove(item);
        }
    }

    private void loadApartmentStatuses() {
        String[] statuses = {"Click to select status", "OCCUPIED", "VACANT", "UNDER_MAINTENANCE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Set a listener to handle hint functionality
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Check if the user selected the hint (position 0)
                if (position == 0) {
                    ((TextView) view).setTextColor(Color.GRAY);
                } else {
                    ((TextView) view).setTextColor(Color.BLACK);

                    String selectedStatus = parent.getItemAtPosition(position).toString();

                    switch (selectedStatus) {
                        case "OCCUPIED":
                            // Action for OCCUPIED status
                            Log.d("ApartmentStatus", "Apartment is OCCUPIED");
                            break;
                        case "VACANT":
                            // Action for VACANT status
                            Log.d("ApartmentStatus", "Apartment is VACANT");
                            break;
                        case "UNDER_MAINTENANCE":
                            // Action for UNDER_MAINTENANCE status
                            Log.d("ApartmentStatus", "Apartment is UNDER_MAINTENANCE");
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadApartmentDetails() {

        String description = apartmentDescriptionEditText.getText().toString();

        Log.d("ApartmentDetails", "Description: " + description);

    }
    // ... (rest of the methods remain the same) ...

    private void openImagePicker(ImageView imageView, String imageType) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

        this.currentImageView = imageView; // Use 'this' to refer to the instance variable
        this.currentImageType = imageType; // Use 'this' to refer to the instance variable
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Update the corresponding image URI in imageData
            if (this.currentImageType != null) {
                if (this.currentImageType.equals("kitchen")) {
                    this.imageData.setKitchenImageUri(selectedImageUri);
                    // ... (similar cases for other image types) ...
                }
            }

            // Update the ImageView using Glide
            if (this.currentImageView != null) {
                Glide.with(this).load(selectedImageUri).into(this.currentImageView);
            } else {
                Toast.makeText(this, "Error selecting image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Separate ImageData class for managing image URIs
    static class ImageData {
        private Uri kitchenImageUri, bedroomImageUri, livingRoomImageUri, bathroomImageUri,
                exteriorImageUri, diningRoomImageUri, balconyImageUri, parkingImageUri,
                hallwayImageUri, storageroomImageUri;

        // Setter methods for each image URI
        public void setKitchenImageUri(Uri uri) {
            kitchenImageUri = uri;
        }

        public void setBedroomImageUri(Uri uri) {
            bedroomImageUri = uri;
        }

        public void setLivingRoomImageUri(Uri uri) {
            livingRoomImageUri = uri;
        }

        public void setBathroomImageUri(Uri uri) {
            bathroomImageUri = uri;
        }

        public void setExteriorImageUri(Uri uri) {
            exteriorImageUri = uri;
        }

        public void setDiningRoomImageUri(Uri uri) {
            diningRoomImageUri = uri;
        }

        public void setBalconyImageUri(Uri uri) {
            balconyImageUri = uri;
        }

        public void setParkingImageUri(Uri uri) {
            parkingImageUri = uri;
        }

        public void setHallwayImageUri(Uri uri) {
            hallwayImageUri = uri;
        }

    }
}




